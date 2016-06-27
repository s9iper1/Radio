package com.byteshaft.shout;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.pits.library.radio.RadioListener;
import com.pits.library.radio.RadioManager;

public class StreamService extends Service implements RadioListener {

    public RadioManager mRadioManager;
    private static StreamService sService;
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
        mRadioManager = RadioManager.with(this, MainActivity.class);
        mRadioManager.registerListener(this);
        mRadioManager.connect();
        return START_STICKY;
    }

    public void playStream() {
        mRadioManager.startRadio("http://198.178.123.5:8476/stream/1/");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("service", "onTaskRemoved");
        mRadioManager.closeNotification();
        sService = null;
        stopSelf();
    }

    void stopStream() {
        if (AppGlobals.getSongStatus()) {
            mRadioManager.stopRadio();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "onDestroy");
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
                        mRadioManager.stopRadio();
                        mRadioManager.updateNotification();
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
                mRadioManager.stopRadio();
            }
        }
    };

    @Override
    public void onRadioLoading() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Player.getInstance().textView.setText("Loading...");
                Player.getInstance().updateProgressBar();
            }
        });
    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AppGlobals.setSongPlaying(true);
                mRadioManager.updateNotification();
                Player.getInstance().stopProgressBar();
                Helpers.updateMainViewButton();
                Player.getInstance().textView.setText("Playing");
                if (Player.getInstance().mProgressBar.getVisibility() == View.VISIBLE) {
                    Player.getInstance().stopProgressBar();
                }
            }
        });

    }

    @Override
    public void onRadioStopped() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AppGlobals.setSongPlaying(false);
                mRadioManager.updateNotification();
                Helpers.updateMainViewButton();
                Player.getInstance().textView.setText("Stopped");
                if (Player.getInstance().mProgressBar.getVisibility() == View.VISIBLE) {
                    Player.getInstance().stopProgressBar();
                }

            }
        });
    }

    @Override
    public void onMetaDataReceived(String s, String s2) {

    }

    @Override
    public void onError() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("Error", "onerror called");
                if (Player.getInstance().mProgressBar.getVisibility() == View.VISIBLE) {
                    Player.getInstance().stopProgressBar();
                    Helpers.updateMainViewButton();
                }
            }
        });
    }

    @Override
    public void songInfo(String s) {

    }
}


