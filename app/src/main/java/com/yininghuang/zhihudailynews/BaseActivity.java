package com.yininghuang.zhihudailynews;

import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

/**
 * Created by Yining Huang on 2016/10/24.
 */

abstract public class BaseActivity extends AppCompatActivity {

    public static final int DARK_THEME = 0;
    public static final int LIGHT_THEME = 1;

    @Override
    protected void onResume() {
        super.onResume();
        if ((getThemeId() == DARK_THEME && !UserSettingConstants.DARK_THEME) || (getThemeId() == LIGHT_THEME && UserSettingConstants.DARK_THEME)) {
            onThemeChange();
        }
    }

    public void onThemeChange() {

    }

    public int getThemeId() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.themeName, typedValue, true);
        if ("dark".equals(typedValue.string))
            return DARK_THEME;
        return LIGHT_THEME;
    }
}
