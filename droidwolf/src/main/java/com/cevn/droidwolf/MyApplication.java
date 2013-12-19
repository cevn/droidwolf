package com.cevn.droidwolf;

import android.app.Application;

/**
 * Created by sameer on 12/19/13.
 */
public class MyApplication extends Application {
    private static boolean activityVisible;
    private static boolean mapVisible;

    public static boolean isMapVisible() {
        return mapVisible;
    }

    public static void pauseMap() {
        mapVisible = false;
    }

    public static void resumeMap() {
        mapVisible = true;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
