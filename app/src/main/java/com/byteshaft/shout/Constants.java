package com.byteshaft.shout;

public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.byteshaft.custom_notification.action.main";
        String INIT_ACTION = "com.byteshaft.custom_notification.action.init";
        String PREV_ACTION = "com.byteshaft.custom_notification.action.prev";
        String PLAY_ACTION = "com.byteshaft.custom_notification.action.play";
        String NEXT_ACTION = "com.byteshaft.custom_notification.action.next";
        String STARTFOREGROUND_ACTION = "com.byteshaft.custom_notification.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.byteshaft.custom_notification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
