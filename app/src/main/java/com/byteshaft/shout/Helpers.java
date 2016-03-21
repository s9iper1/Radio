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
        Player player = Player.getInstance();
        if (player != null && player.mPlaybackButton != null) {
            if (player.sMediaPlayer.isPlaying()) {
                player.mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_pause);
            } else {
                player.mPlaybackButton.setBackgroundResource(R.drawable.apollo_holo_dark_play);
            }
        }
    }
}
