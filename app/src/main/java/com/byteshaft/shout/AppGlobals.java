package com.byteshaft.shout;

import android.app.Application;
import android.content.Context;

public class AppGlobals extends Application{
    private static boolean sNotificationVisibility = false;
    public static final String READY_STREAM = "ready_stream";
    private static  boolean songPlaying = false;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }

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
