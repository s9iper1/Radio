package com.byteshaft.shout;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class StreamService extends Service implements FFmpegMediaPlayer.OnPreparedListener, FFmpegMediaPlayer.OnErrorListener {


    private CustomMediaPlayer mMediaPlayer;
    private static StreamService sService;
    private boolean mIsPrepared;
    private boolean mPreparing;
    private boolean mFreshRun = true;
    private boolean startOnPrepare;

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
        mMediaPlayer = CustomMediaPlayer.getInstance();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        startOnPrepare = intent.getBooleanExtra(AppGlobals.READY_STREAM, false);
        if (startOnPrepare) {
            readyStream();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sService = null;
    }

    void readyStream() {
        Player.getInstance().updateProgressBar();
        String url = getString(R.string.shoutcast_url);
        Toast.makeText(Player.getInstance().getActivity(), "Streaming", Toast.LENGTH_LONG).show();
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startStream() {
        if (mIsPrepared) {
            mMediaPlayer.start();
        } else if (!mPreparing && !startOnPrepare) {
            Toast.makeText(Player.getInstance().getActivity(), "Streaming...", Toast.LENGTH_SHORT).show();
            String url = getString(R.string.shoutcast_url);
            Player.getInstance().updateProgressBar();
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPreparing = true;
        }
    }

    void stopStream() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            CustomMediaPlayer.sCustomMediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopStream();
        sService = null;
        unregisterReceiver(mOutGoingCallListener);
    }

    @Override
    public void onPrepared(FFmpegMediaPlayer fFmpegMediaPlayer) {
        mIsPrepared = true;
        if (Player.getInstance().mProgressBar.getVisibility() == View.VISIBLE) {
            Player.getInstance().stopProgressBar();
        }
        mMediaPlayer.start();
    }

    private PhoneStateListener mCallStateListener = new PhoneStateListener() {
        boolean songWasOn = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mMediaPlayer.isPlaying() && AppGlobals.getSongStatus()) {
                        mMediaPlayer.pause();
                        songWasOn = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (!mFreshRun && songWasOn) {
                        mMediaPlayer.start();
                    }
                    mFreshRun = false;
                    break;
            }
        }
    };

    private BroadcastReceiver mOutGoingCallListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    };

    @Override
    public boolean onError(FFmpegMediaPlayer fFmpegMediaPlayer, int i, int i1) {
        System.out.println(i);
        System.out.println(i1);
        stopSelf();
        return true;
    }
}
