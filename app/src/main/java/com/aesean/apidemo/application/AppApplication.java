package com.aesean.apidemo.application;

import android.app.Application;
import android.content.res.Configuration;

/**
 * AppApplication
 *
 * @author xl
 * @version 1.0
 * @since 20/08/2017
 */

public class AppApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        App.init(this);
        App.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        App.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        App.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        App.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        App.onConfigurationChanged(newConfig);
    }
}
