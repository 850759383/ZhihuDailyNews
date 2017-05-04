package com.yininghuang.zhihudailynews.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;
import com.yininghuang.zhihudailynews.utils.DBManager;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoriteActivity extends AppCompatActivity {

    private static final int LIGHT_THEME = R.style.AppTheme_NoActionBar;
    private static final int DARK_THEME = R.style.AppThemeDark_NoActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_THEME)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_favorite);

        FavoriteFragment fragment = (FavoriteFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = FavoriteFragment.newInstance();
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame,
                    "FavoriteFragment",
                    false);
        }

        new FavoritePresenter(fragment, new DBManager(this));
    }
}
