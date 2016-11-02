package com.yininghuang.zhihudailynews;

import android.app.Application;
import android.content.SharedPreferences;

import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

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
        SharedPreferences preferences = getSharedPreferences(UserSettingConstants.PREFERENCE__USER_SETTINGS, MODE_PRIVATE);
        UserSettingConstants.NO_IMAGE_MODE = preferences.getBoolean("no_image", UserSettingConstants.NO_IMAGE_MODE);
        UserSettingConstants.USE_WEBVIEW = preferences.getBoolean("use_webview", UserSettingConstants.USE_WEBVIEW);
        UserSettingConstants.DARK_MODE = preferences.getBoolean("dark_mode", UserSettingConstants.DARK_MODE);
        UserSettingConstants.SKIP_SPLASH = preferences.getBoolean("skip_splash", UserSettingConstants.SKIP_SPLASH);
    }
}
