package com.yininghuang.zhihudailynews;

import android.app.Application;
import android.content.SharedPreferences;

import com.yininghuang.zhihudailynews.settings.SettingsActivity;

/**
 * Created by Yining Huang on 2016/10/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        loadUserSettings();
    }

    private void loadUserSettings() {
        SharedPreferences preferences = getSharedPreferences(SettingsActivity.PREFERENCE__USER_SETTINGS, MODE_PRIVATE);
        Constants.NO_IMAGE_MODE = preferences.getBoolean("no_image", Constants.NO_IMAGE_MODE);
        Constants.USE_WEBVIEW = preferences.getBoolean("use_webview", Constants.USE_WEBVIEW);
    }
}
