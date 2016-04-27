package com.byteshaft.shout;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AppGlobals extends Application{
    private static boolean sNotificationVisibility = false;
    public static final String READY_STREAM = "ready_stream";
    private static  boolean songPlaying = false;
    private static Context sContext;
    public static Bitmap notificationAlbumArt;
    public static Bitmap pauseButton;
    public static Bitmap playButton;
    public static final String SCHEDULE_URL = "http://8ccc.com.au/schedule";
    public static final String CONTACT_URL = "http://8ccc.com.au/contact";
    public static final String MEMBERSHIP_URL = "http://8ccc.com.au/membership";
    public static final String ABOUT_TEXT = "Formed in 1981, 102.1FM 8CCC is an outback radio " +
            "station with a proud history of unique programming &amp; regular Centralian/Barkly-inspired music."+
            " \n\n <b>8CCC’s mission:</b> \n To involve and service the Alice Springs and Tennant Creek" +
            " communities with services, programming and content that is not readily available in the" +
            " commercial broadcasting arena. \n\n <b>8CCC strives to:</b> \n \n<i>Promote and foster" +
            " a community and multicultural voice through the electronic media.</i> \n \n" +
            "<i> * Enrich people’s lives with a diversity of programs and services that inform, " +
            "educate and entertain.</i>\n \n * 8CCC is a member of the Community Broadcasters " +
            "Association of Australia (CBAA) and part of Australia’s Community Radio Network (CRN)."
            + " \n\n Get involved and be a part of it! \n";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        notificationAlbumArt = decodeSampledBitmapFromResource(getResources(),
                R.drawable.notification, 60, 60);
        pauseButton = decodeSampledBitmapFromResource(getResources(),
                R.drawable.apollo_holo_dark_pause, 32, 32);
        playButton = decodeSampledBitmapFromResource(getResources(),
                R.drawable.play, 32, 32);


    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
