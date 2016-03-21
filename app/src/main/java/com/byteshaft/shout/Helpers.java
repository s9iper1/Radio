package com.byteshaft.shout;

import android.content.Context;
import android.content.ContextWrapper;
import android.telephony.TelephonyManager;

public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    static void updateMainViewButton() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null && mainActivity.mPlaybackButton != null) {
            if (mainActivity.sMediaPlayer.isPlaying()) {
                mainActivity.mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_pause);
            } else {
                mainActivity.mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_play);
            }
        }
    }
}
