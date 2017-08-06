package com.xd.refresh.manager.db;

import android.provider.BaseColumns;

/**
 * Created by dell on 2017/8/6.
 */

public class Columns implements BaseColumns {
    /**
     * 表名称
     */
    public static final String TABLE_NAME = "device_tab";

    /**
     * 横竖屏 0=竖屏，1=横屏
     */
    public static final String ORIENTATION = "orientation";
    /**
     * 操作系统版本号
     */
    public static final String OSV = "osv";
    /**
     * 网络运营商
     */
    public static final String OPERATOR = "operator";
    /**
     * 是否支持跳转到其它应用 0=不支持，1=支持
     */
    public static final String IS_SUPPORT_DEEPLINK = "is_support_deeplink";
    /**
     * 屏幕密度
     */
    public static final String DENSITY = "density";
    /**
     * 联网类型(0—未知，1—Ethernet， 2—wifi，3—蜂窝网络，未知代， 4—， 2G， 5—蜂窝网络， 3G， 6—蜂窝网络，4G)
     */
    public static final String NET = "net";
    /**
     * 使用语言
     */
    public static final String LAN = "lan";
    /**
     * 设备生产商名称
     */
    public static final String VENDOR = "vendor";
    /**
     * mac 地址
     */
    public static final String MAC = "mac";
    /**
     * android id
     */
    public static final String ADID = "adid";
    /**
     * 请求批量下发广告的数量,目前只能为”1”
     */
    public static final String BATCH_CNT = "batch_cnt";
    /**
     * imei
     */
    public static final String IMEI = "imei";

    /**
     * 设备屏幕的宽度，以像素为单位
     */
    public static final String DVW = "dvw";
    /**
     * 设备屏幕的高度，以像素为单位
     */
    public static final String DVH = "dvh";
    /**
     * 广告位的宽度，以像素为单位
     */
    public static final String ADW = "adw";
    /**
     * 广告位的高度，以像素为单位
     */
    public static final String ADH = "adh";
    /**
     * 客户端操作系统的类型 值有：Android、iOS、WP(注意大小写)
     */
    public static final String OS = "os";
    /**
     * 数据类型 html、json、htmlorjson(既支持json 又 支持html)
     */
    public static final String TRAMATERIALTYPE = "tramaterialtype";
    /**
     * 设备类型 -1-未知，0 - phone，1 - pad，2 - pc，3 - tv，4 - wap
     */
    public static final String DEVICETYPE = "devicetype";
    /**
     * 机型
     */
    public static final String MODEL = "model";
    /**
     * user_agent
     */
    public static final String UA = "ua";

}
