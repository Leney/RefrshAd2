package com.xd.refresh.manager.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.xd.refresh.AppApplication;
import com.xd.refresh.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地数据库文件操作类
 * Created by llj on 2017/7/18.
 */

public class DbManager {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "device.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.xd.refresh";//包名
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() +
            "/" + PACKAGE_NAME;   //数据库的绝对路径( /data/data/com.*.*(package name))
    private SQLiteDatabase db;

    public DbManager() {
    }

    //对外提供的打开数据库接口
    public void openDataBase() {
        this.db = this.openDataBase(DB_PATH + "/" + DB_NAME);
    }

    //获取打开后的数据库
    public SQLiteDatabase getDb() {
        return this.db;
    }

    // 本地打开数据方法
    private SQLiteDatabase openDataBase(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) { //判断文件是否存在
                //通过输入流和输出流，把数据库拷贝到"filePath"下
                InputStream is = AppApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.device);//获取输入流，使用R.raw.test资源
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int readCount;
                while ((readCount = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readCount);
                }
                fos.close();
                is.close();
            }
            //打开数据库
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(filePath, null);
            return db;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库
    public void closeDataBase() {
        if (this.db != null) db.close();
    }
}
