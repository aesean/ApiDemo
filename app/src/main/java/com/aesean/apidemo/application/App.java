package com.aesean.apidemo.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

/**
 * App
 * 原则上{@link AppApplication}不会再做过多修改。Application之所以这么写主要是将Application分离出来。
 *
 * @author xl
 * @version 1.0
 * @since 20/08/2017
 */

public class App {
    private static final String TAG = "App";
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private App() {
    }

    static void init(Application application) {
        if (application == null) {
            throw new NullPointerException("application can't be null.");
        }
        if (sApplication != null) {
            throw new IllegalStateException("Application has bean initialized");
        }
        sApplication = application;
    }

    @NonNull
    public static Application currentApplication() {
        if (sApplication == null) {
            throw new NullPointerException("Application need initialize");
        }
        return sApplication;
    }

    public static String getPackageName() {
        return sApplication.getPackageName();
    }

    private static Toast mToast;

    public static void showMessage(@StringRes int msg, int duration) {
        showMessage(getString(msg), duration);
    }

    private static void showMessage(String msg, int duration) {
        Log.d(TAG, "showMessage: " + msg);
        if (mToast == null) {
            mToast = Toast.makeText(sApplication, msg, duration);
            mToast.show();
            return;
        }
        mToast.setDuration(duration);
        mToast.setText(msg);
        mToast.show();
    }

    public static void showShortMessage(String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    public static void showLongMessage(String msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    public static void showShortMessage(@StringRes int msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    public static void showLongMessage(@StringRes int msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    public static String getString(@StringRes int id) {
        return sApplication.getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return sApplication.getResources().getString(id, formatArgs);
    }

    static void onCreate() {
    }

    static void onTerminate() {
    }

    static void onLowMemory() {
    }

    static void onTrimMemory(int level) {
    }

    static void onConfigurationChanged(Configuration newConfig) {
    }
}
