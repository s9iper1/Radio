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
        AppGlobals.setSongPlaying(true);
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        System.out.println("LOGhhhhhhh stopped");
//        Helpers.updateMainViewButton();
//        NotificationService.getsInstance().stopSelf();
//        AppGlobals.setSongPlaying(false);
//        AppGlobals.setNotificationVisibility(false);
        AppGlobals.setSongPlaying(false);
        Helpers.updateMainViewButton();
        NotificationService.getsInstance().showNotification();
    }


}
