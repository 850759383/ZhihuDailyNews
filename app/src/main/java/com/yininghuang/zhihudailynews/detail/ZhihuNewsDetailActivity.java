package com.yininghuang.zhihudailynews.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.CacheManager;
import com.yininghuang.zhihudailynews.utils.DBManager;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuNewsDetailActivity extends BaseActivity {

    private static final int LIGHT_THEME = R.style.AppTheme_NoActionBar_TranslucentStatusBar;
    private static final int DARK_THEME = R.style.AppThemeDark_NoActionBar_TranslucentStatusBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_THEME)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_zhihu_news_detail);

        ZhihuNewsDetailFragment fragment = (ZhihuNewsDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFrameLayout);

        if (fragment == null) {
            fragment = ZhihuNewsDetailFragment.newInstance(getIntent().getIntExtra("id", -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, fragment)
                    .commit();
        }

        new ZhihuNewsDetailPresenter(
                fragment,
                RetrofitHelper.getInstance().createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL),
                new DBManager(this),
                CacheManager.getInstance(this)
        );
    }

}
