package com.byteshaft.shout;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class StreamService extends Service implements FFmpegMediaPlayer.OnPreparedListener {

    CustomMediaPlayer mMediaPlayer;
    private static StreamService sService;
    private boolean mIsPrepared;
    private boolean mPreparing;
    private boolean mFreshRun = true;

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
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sService = null;
    }

    void startStream() {
        if (mIsPrepared) {
            mMediaPlayer.start();
        } else if (!mPreparing){
            String url = getString(R.string.shoutcast_url);
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
}
