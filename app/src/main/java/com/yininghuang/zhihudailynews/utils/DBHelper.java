package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "default.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_FAVORITE = "favorite";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITE + " ("
                + "id integer primary key not null,"
                + "save_time integer not null,"
                + "content text not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
