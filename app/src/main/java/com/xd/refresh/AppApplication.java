package com.xd.refresh;

import android.app.Application;

import com.xd.refresh.manager.Constance;
import com.xd.refresh.manager.db.DeviceDBManager;

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

        // 获取数据库数据的总条数
        Constance.DB_TOTAL_DATA_COUNT = (int) DeviceDBManager.getInstance().getTotalCount();
    }
}
