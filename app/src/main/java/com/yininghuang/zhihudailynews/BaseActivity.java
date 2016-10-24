package com.yininghuang.zhihudailynews;

import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

/**
 * Created by Yining Huang on 2016/10/24.
 */

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.themeName, typedValue, true);
        if (("dark".equals(typedValue.string) && !UserSettingConstants.DARK_MODE)
                || ("light".equals(typedValue.string) && UserSettingConstants.DARK_MODE)) {
            onThemeChange(UserSettingConstants.DARK_MODE);
        }
    }

    public void onThemeChange(Boolean isDarkTheme) {

    }
}
