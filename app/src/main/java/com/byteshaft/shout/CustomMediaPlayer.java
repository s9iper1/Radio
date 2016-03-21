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


    @Override
    public void start() throws IllegalStateException {
        super.start();
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().stopSelf();
        AppGlobals.setNotificationVisibility(false);
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }
}
