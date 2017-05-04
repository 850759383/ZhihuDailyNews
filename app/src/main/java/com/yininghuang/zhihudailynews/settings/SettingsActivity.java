package com.yininghuang.zhihudailynews.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class SettingsActivity extends BaseActivity {

    private static final int LIGHT_THEME = R.style.PreferenceTheme;
    private static final int DARK_THEME = R.style.PreferenceThemeDark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_THEME)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout) == null) {

            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    SettingsFragment.newInstance(),
                    R.id.mainFrameLayout,
                    "SettingsFragment",
                    false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onThemeChange() {
        if (UserSettingConstants.DARK_THEME) {
            setTheme(DARK_THEME);
        } else {
            setTheme(LIGHT_THEME);
        }
        Intent intent = new Intent(this, getClass());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();

    }
}
