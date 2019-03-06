package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;

import com.yininghuang.zhihudailynews.model.db.Favorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class DBManager {

    private SQLiteDatabase db;

    public DBManager(Context context) {
        DBHelper mDBHelper = new DBHelper(context);
        db = mDBHelper.getWritableDatabase();
    }

    public void addFavorite(Favorite content) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO " + DBHelper.TABLE_FAVORITE + " VALUES(?, ?, ?)",
                    new Object[]{
                            content.getId(),
                            content.getSaveTime(),
                            content.getContent()
                    });
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void removeFavorite(int id) {
        db.delete(DBHelper.TABLE_FAVORITE, "id = ?", new String[]{String.valueOf(id)});
    }

    @Nullable
    public Favorite queryFavorite(int contentId) {
        Favorite favorite = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_FAVORITE + " WHERE id = ?",
                new String[]{String.valueOf(contentId)});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            favorite = new Favorite(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("save_time")),
                    cursor.getString(cursor.getColumnIndex("content"))
            );
        }
        cursor.close();
        return favorite;
    }

    public List<Favorite> queryFavoriteList() {
        List<Favorite> list = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_FAVORITE, null, null, null, null, null, "save_time DESC");
        if (cursor.moveToFirst()) {
            do {
                Favorite favorite = new Favorite(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("save_time")),
                        cursor.getString(cursor.getColumnIndex("content")));
                list.add(favorite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void clearAllFavorite() {
        db.delete(DBHelper.TABLE_FAVORITE, null, null);
    }

}
