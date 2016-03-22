package com.byteshaft.shout;

public class AppGlobals {
    private static boolean sNotificationVisibility = false;
    private static  boolean songPlaying = false;

    public static void setNotificationVisibility(boolean visibility) {
        sNotificationVisibility = visibility;
    }

    public static boolean isNotificationVisible() {
        return sNotificationVisibility;
    }

    public static void setSongPlaying(boolean status) {
        songPlaying = status;
    }

    public static boolean getSongStatus() {
        return songPlaying;
    }


}
