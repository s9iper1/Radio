package com.byteshaft.shout;

import wseemann.media.FFmpegMediaPlayer;

public class CustomMediaPlayer extends FFmpegMediaPlayer {

    static CustomMediaPlayer sCustomMediaPlayer;

    static CustomMediaPlayer getInstance() {
        if (sCustomMediaPlayer == null) {
            sCustomMediaPlayer = new CustomMediaPlayer();
            return sCustomMediaPlayer;
        } else {
            return sCustomMediaPlayer;
        }
    }

    static CustomMediaPlayer getInsance(boolean forceConstruct) {
        if (forceConstruct) {
            sCustomMediaPlayer = null;
            return getInstance();
        } else {
            return getInstance();
        }
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        AppGlobals.setSongPlaying(true);
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        AppGlobals.setSongPlaying(false);
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }


}
