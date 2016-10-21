package com.yininghuang.zhihudailynews.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_page);

        ZhihuDailyFragment dailyFragment = (ZhihuDailyFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (dailyFragment == null){
            dailyFragment = ZhihuDailyFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFrame, dailyFragment)
                    .commit();
        }
        new ZhihuDailyPresenter(dailyFragment, RetrofitHelper.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
