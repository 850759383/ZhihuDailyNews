package com.yininghuang.zhihudailynews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFERENCE__USER_SETTINGS = "user_settings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityUtils.replaceFragment(
                getSupportFragmentManager(),
                SettingsFragment.newInstance(),
                R.id.mainFrameLayout,
                "SettingsFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();

    }
}
