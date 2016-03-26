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
    private static Bitmap smallBitMap;
    private static Bitmap bigBitMap;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        smallBitMap = decodeSampledBitmapFromResource(getResources(), R.drawable.notification,
                100, 100);
        bigBitMap = decodeSampledBitmapFromResource(getResources() ,R.drawable.notification,
                100, 100);

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

    public static Bitmap getSmallBitMap() {
        return smallBitMap;
    }

    public static Bitmap getBigBitMap() {
        return bigBitMap;
    }



}
