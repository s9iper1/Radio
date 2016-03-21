package com.byteshaft.shout;

public class AppGlobals {
    private static boolean sNotificationVisibitlity = false;
    public static void setNotificationVisibility(boolean visibility) {
        sNotificationVisibitlity = visibility;
    }

    public static boolean isNotificationVisible() {
        return sNotificationVisibitlity;
    }
}
