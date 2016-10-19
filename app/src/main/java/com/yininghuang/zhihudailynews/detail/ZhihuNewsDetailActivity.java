package com.yininghuang.zhihudailynews.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuNewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_news_detail);

        ZhihuNewsDetailFragment fragment = (ZhihuNewsDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mainFrameLayout);

        if (fragment == null) {
            fragment = ZhihuNewsDetailFragment.newInstance(getIntent().getIntExtra("id", -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, fragment)
                    .commit();
        }

        new ZhihuNewsDetailPresenter(fragment, RetrofitHelper.getInstance());
    }
}
