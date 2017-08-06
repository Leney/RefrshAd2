package com.xd.refresh.manager.db;

import android.content.ContentValues;
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

        DeviceInfo deviceInfo;
        try {
            DbManager manager = new DbManager();
            manager.openDataBase();
            SQLiteDatabase db = manager.getDb();
            String sql = "Select * from device_tab where _id = " + id + ";";

            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            deviceInfo = getDeviceInfo(cursor);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            deviceInfo = null;
        }
        return deviceInfo;
    }

    /**
     * 从查询结果中获取出一条数据
     *
     * @param cursor
     * @return 一条数据 DeviceInfo
     * <p>
     * + DBHelp.Columns.LANGUAGE
     */
    private DeviceInfo getDeviceInfo(Cursor cursor) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.orientation = cursor.getInt(cursor.getColumnIndex(Columns.ORIENTATION));
        deviceInfo.osv = cursor.getString(cursor.getColumnIndex(Columns.OSV));
        deviceInfo.operator = cursor.getString(cursor.getColumnIndex(Columns.OPERATOR));
        deviceInfo.is_support_deeplink = cursor.getInt(cursor.getColumnIndex(Columns.IS_SUPPORT_DEEPLINK));
        deviceInfo.density = cursor.getString(cursor.getColumnIndex(Columns.DENSITY));
        deviceInfo.net = cursor.getInt(cursor.getColumnIndex(Columns.NET));
        deviceInfo.lan = cursor.getString(cursor.getColumnIndex(Columns.LAN));
        deviceInfo.vendor = cursor.getString(cursor.getColumnIndex(Columns.VENDOR));
        deviceInfo.mac = cursor.getString(cursor.getColumnIndex(Columns.MAC));
        deviceInfo.adid = cursor.getString(cursor.getColumnIndex(Columns.ADID));
        deviceInfo.batch_cnt = cursor.getString(cursor.getColumnIndex(Columns.BATCH_CNT));
        deviceInfo.imei = cursor.getString(cursor.getColumnIndex(Columns.IMEI));
        deviceInfo.dvw = cursor.getInt(cursor.getColumnIndex(Columns.DVW));
        deviceInfo.dvh = cursor.getInt(cursor.getColumnIndex(Columns.DVH));
        deviceInfo.adw = cursor.getInt(cursor.getColumnIndex(Columns.ADW));
        deviceInfo.adh = cursor.getInt(cursor.getColumnIndex(Columns.ADH));
        deviceInfo.os = cursor.getString(cursor.getColumnIndex(Columns.OS));
        deviceInfo.tramaterialtype = cursor.getString(cursor.getColumnIndex(Columns.TRAMATERIALTYPE));
        deviceInfo.devicetype = cursor.getString(cursor.getColumnIndex(Columns.DEVICETYPE));
        deviceInfo.model = cursor.getString(cursor.getColumnIndex(Columns.MODEL));
        deviceInfo.ua = cursor.getString(cursor.getColumnIndex(Columns.UA));
        return deviceInfo;
    }

    /**
     * 将AppInfo对象转换成ContentValues对象
     * @param deviceInfo
     * @return
     */
    private ContentValues getContentValues(DeviceInfo deviceInfo)
    {
        ContentValues values = new ContentValues();
        values.put(Columns.ORIENTATION,deviceInfo.orientation);
        values.put(Columns.OSV,deviceInfo.osv);
        values.put(Columns.OPERATOR,deviceInfo.operator);
        values.put(Columns.IS_SUPPORT_DEEPLINK,deviceInfo.is_support_deeplink);
        values.put(Columns.DENSITY,deviceInfo.density);
        values.put(Columns.NET,deviceInfo.net);
        values.put(Columns.LAN,deviceInfo.lan);
        values.put(Columns.VENDOR,deviceInfo.vendor);
        values.put(Columns.MAC,deviceInfo.mac);
        values.put(Columns.ADID,deviceInfo.adid);
        values.put(Columns.BATCH_CNT,deviceInfo.batch_cnt);
        values.put(Columns.IMEI,deviceInfo.imei);
        values.put(Columns.DVW,deviceInfo.dvw);
        values.put(Columns.DVH,deviceInfo.dvh);
        values.put(Columns.ADW,deviceInfo.adw);
        values.put(Columns.ADH,deviceInfo.adh);
        values.put(Columns.OS,deviceInfo.os);
        values.put(Columns.TRAMATERIALTYPE,deviceInfo.tramaterialtype);
        values.put(Columns.DEVICETYPE,deviceInfo.devicetype);
        values.put(Columns.MODEL,deviceInfo.model);
        values.put(Columns.UA,deviceInfo.ua);
        return values;
    }

}
