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


//    /**
//     * 判断当前前端显示的是否是某个activity
//     * @param className
//     * @return
//     */
//    public static boolean isTopActivity(String className)
//    {
//        boolean isTop = false;
//        ActivityManager am = (ActivityManager)instance.getSystemService(ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        Log.d("llj", "isTopActivity = " + cn.getClassName());
//        if (cn.getClassName().contains(className))
//        {
//            isTop = true;
//        }
//        Log.d("llj", "isTop = " + isTop);
//        return isTop;
//    }
}
