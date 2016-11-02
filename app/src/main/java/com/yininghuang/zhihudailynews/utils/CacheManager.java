package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yining Huang on 2016/11/2.
 */

public class CacheManager {

    public static final String SUB_DIR_NEWS = "news";

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
}
