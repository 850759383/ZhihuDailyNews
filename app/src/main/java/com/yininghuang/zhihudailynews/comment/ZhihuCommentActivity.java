package com.yininghuang.zhihudailynews.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentActivity extends BaseActivity {

    private static final int LIGHT_THEME = R.style.AppTheme_NoActionBar;
    private static final int DARK_THEME = R.style.AppThemeDark_NoActionBar;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_MODE)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_zhihu_comment);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.comment);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ZhihuCommentFragment fragment = (ZhihuCommentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = ZhihuCommentFragment.newInstance(getIntent().getIntExtra("id", -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, fragment)
                    .commit();
        }

        new ZhihuCommentPresenter(fragment, RetrofitHelper.getInstance());

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
}
