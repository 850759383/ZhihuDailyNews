package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/11/2.
 */

public class CacheManager {

    public static final String SUB_DIR_NEWS = "news";
    public static final String SUB_DIR_THEMES = "themes";

    private static CacheManager INSTANCE;

    private Context mContext;

    private CacheManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static CacheManager getInstance(Context content) {
        if (INSTANCE == null) {
            INSTANCE = new CacheManager(content);
        }
        return INSTANCE;
    }

    @Nullable
    public String getData(String subDir, String name) {
        try {
            File targetFile = new File(getSubCacheDir(subDir), name);
            if (!targetFile.exists())
                return null;

            FileInputStream is = new FileInputStream(targetFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveData(String subDir, String name, String data) {
        try {
            File targetFile = new File(getSubCacheDir(subDir), name);
            FileOutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSubCacheDir(String dirName) {
        String cacheDir = mContext.getCacheDir().getAbsolutePath();
        File subFile = new File(cacheDir, dirName);
        if (!subFile.exists())
            subFile.mkdir();
        return subFile.getAbsolutePath();
    }


    public Observable<Boolean> clearCache() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(deleteDir(mContext.getCacheDir()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> calcCacheSize() {
        return Observable.just(folderSize(mContext.getCacheDir()))
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long b) {
                        if (b < 1024) {
                            return Math.round(b * 100f) / 100.0 + "B";
                        } else if (b < 1024 * 1024) {
                            return Math.round(b / 1024f * 100) / 100.0 + "KB";
                        }
                        return Math.round(b / 1024f / 1024f * 100) / 100.0 + "KB";
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean deleteDir(File dir) {
        if (!dir.exists())
            return true;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public long folderSize(File directory) {
        long length = 0;
        if (!directory.exists())
            return length;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
}
