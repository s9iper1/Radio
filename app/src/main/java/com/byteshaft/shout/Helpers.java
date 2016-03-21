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

    static void updateMainViewButton(String text) {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null && mainActivity.mPlaybackButton != null) {
            mainActivity.mPlaybackButton.setText(text);
        }
    }
}
