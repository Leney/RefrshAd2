package com.xd.refresh.manager.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xd.refresh.bean.DeviceInfo;

/**
 * Created by llj on 2017/7/18.
 */

public class DeviceDBManager {

    private static volatile DeviceDBManager instance = null;

    public static DeviceDBManager getInstance() {
        if (instance == null) {
            synchronized (DeviceDBManager.class) {
                if (instance == null) {
                    instance = new DeviceDBManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取数据库中总的条数
     *
     * @return
     */
    public long getTotalCount() {
        DbManager manager = new DbManager();
        manager.openDataBase();
        SQLiteDatabase db = manager.getDb();
        String sql = "Select count(*) from device_tab;";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 根据id查找数据库中的一条Device数据
     *
     * @return
     */
    public DeviceInfo queryDeviceInfo(int id) {

        DeviceInfo deviceInfo = new DeviceInfo();
        try {
            DbManager manager = new DbManager();
            manager.openDataBase();
            SQLiteDatabase db = manager.getDb();
            String sql = "Select * from device_tab where _id = " + id + ";";

            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            deviceInfo.imei = cursor.getString(1);
            deviceInfo.androidId = cursor.getString(2);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            deviceInfo = null;
        }
        return deviceInfo;
    }

}
