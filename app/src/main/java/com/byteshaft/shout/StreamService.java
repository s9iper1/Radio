package com.byteshaft.shout;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

public class StreamService extends Service implements ExoPlayer.Listener{


    private ExoPlayer mMediaPlayer;
    private static StreamService sService;
    private boolean mFreshRun = true;
    private MediaCodecAudioTrackRenderer audioRenderer;
    private ExtractorSampleSource sampleSource;

    static StreamService getInstance() {
        return sService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Helpers helpers = new Helpers(getApplicationContext());
        TelephonyManager telephonyManager = helpers.getTelephonyManager();
        telephonyManager.listen(mCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mOutGoingCallListener, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sService = this;
        playStream();
        return START_NOT_STICKY;
    }

    private void playStream() {
        Allocator allocator = new DefaultAllocator(300);
        DataSource dataSource = new DefaultUriDataSource(getApplicationContext(), null,
                " Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/49.0.2623.87 Safari/537.36");
        sampleSource = new ExtractorSampleSource(
                Uri.parse("http://198.178.123.5:8476/stream/1/"),
                dataSource, allocator, 300 * 300);
        audioRenderer = new MediaCodecAudioTrackRenderer(
                sampleSource, MediaCodecSelector.DEFAULT);
        mMediaPlayer = ExoPlayer.Factory.newInstance(1, 300, 300);
        mMediaPlayer.prepare(audioRenderer);
        mMediaPlayer.setPlayWhenReady(true);
        mMediaPlayer.addListener(this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sService = null;
        NotificationService.getsInstance().stopForeground(true);
        NotificationService.getsInstance().onDestroy();
        stopSelf();
    }

    void stopStream() {
        if (AppGlobals.getSongStatus()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopStream();
        sService = null;
        unregisterReceiver(mOutGoingCallListener);
    }

    private PhoneStateListener mCallStateListener = new PhoneStateListener() {
        boolean songWasOn = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (AppGlobals.getSongStatus()) {
                        mMediaPlayer.stop();
                        songWasOn = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (!mFreshRun && songWasOn) {
                        playStream();
                    }
                    mFreshRun = false;
                    break;
            }
        }
    };

    private BroadcastReceiver mOutGoingCallListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppGlobals.getSongStatus()) {
                mMediaPlayer.stop();
            }
        }
    };

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i("TAG", "update");
        System.out.println(playbackState);
        if (playbackState == 2) {
            Player.getInstance().updateProgressBar();
        }
        if (playbackState == 4) {
            AppGlobals.setSongPlaying(true);
            NotificationService.getsInstance().showNotification();
            Player.getInstance().stopProgressBar();
            Helpers.updateMainViewButton();
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        System.out.println("onPlayWhenReadyCommitted");

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        System.out.println("Error");
        AppGlobals.setSongPlaying(false);
        if (Player.getInstance().mProgressBar.getVisibility() == View.VISIBLE) {
            Player.getInstance().stopProgressBar();
            Helpers.updateMainViewButton();
        }
        stopSelf();
    }
}
