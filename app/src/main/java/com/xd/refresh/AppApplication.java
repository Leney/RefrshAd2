package com.xd.refresh;

import android.app.Application;

/**
 * Created by Karma on 2017/7/18.
 */

public class AppApplication extends Application {
    private static AppApplication instance;

    public static Application getInstance(){
        return  instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
