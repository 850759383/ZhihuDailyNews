package com.yininghuang.zhihudailynews.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.NavAdapter;
import com.yininghuang.zhihudailynews.comment.ZhihuThemes;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuThemeService;
import com.yininghuang.zhihudailynews.settings.SettingsActivity;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavAdapter.OnNavItemClickListener {

    private static final int LIGHT_THEME = R.style.AppTheme_NoActionBar;
    private static final int DARK_THEME = R.style.AppThemeDark_NoActionBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navRec)
    RecyclerView mNavRec;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    private int mSelectThemeId = -1;
    private String mTitle;
    private NavAdapter mNavAdapter;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_MODE)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mNavRec.setLayoutManager(new LinearLayoutManager(this));
        mNavAdapter = new NavAdapter(this);
        mNavRec.setAdapter(mNavAdapter);
        mNavAdapter.setOnNavItemClickListener(this);

        fetchThemes();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == fragment) {
            startFragment(FragmentType.TYPE_HOME, null);
        } else {
            if (fragment instanceof ZhihuDailyFragment) {
                new ZhihuDailyPresenter((ZhihuDailyContract.View) fragment, RetrofitHelper.getInstance());
            } else if (fragment instanceof ZhihuThemeFragment) {
                new ZhihuThemePresenter((ZhihuThemeContract.View) fragment, RetrofitHelper.getInstance());
            }
        }

        if (savedInstanceState == null) {
            mTitle = getString(R.string.main_page);
        } else {
            mTitle = savedInstanceState.getString("title");
            mSelectThemeId = savedInstanceState.getInt("themeId");
            mNavAdapter.setSelectThemeId(mSelectThemeId);
        }
        getSupportActionBar().setTitle(mTitle);
    }

    private void fetchThemes() {
        mSubscription = RetrofitHelper.getInstance()
                .createRetrofit(ZhihuThemeService.class, Api.ZHIHU_BASE_URL)
                .getThemes()
                .map(new Func1<ZhihuThemes, List<ZhihuThemes.OthersBean>>() {
                    @Override
                    public List<ZhihuThemes.OthersBean> call(ZhihuThemes zhihuThemes) {
                        return zhihuThemes.getOthers();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ZhihuThemes.OthersBean>>() {
                    @Override
                    public void call(List<ZhihuThemes.OthersBean> othersBeen) {
                        mNavAdapter.setThemes(othersBeen);
                        mNavAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void startFragment(FragmentType type, @Nullable Integer id) {
        if (type == FragmentType.TYPE_HOME) {
            ZhihuDailyFragment fragment = ZhihuDailyFragment.newInstance();
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame,
                    "ZhihuDailyFragment");
            new ZhihuDailyPresenter(fragment, RetrofitHelper.getInstance());
        } else if (type == FragmentType.TYPE_THEME) {
            ZhihuThemeFragment fragment = ZhihuThemeFragment.newInstance(id);
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame,
                    "ZhihuThemeFragment");
            new ZhihuThemePresenter(fragment, RetrofitHelper.getInstance());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("themeId", mSelectThemeId);
        outState.putString("title", mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onThemeChange() {
        if (UserSettingConstants.DARK_MODE) {
            setTheme(DARK_THEME);
        } else {
            setTheme(LIGHT_THEME);
        }
        recreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            }
            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawers();
        else if (mSelectThemeId != -1)
            onHomeClick();
        else
            super.onBackPressed();
    }

    @Override
    public void onHomeClick() {
        if (mSelectThemeId != -1) {
            mSelectThemeId = -1;
            startFragment(FragmentType.TYPE_HOME, null);
            mTitle = getString(R.string.main_page);
            getSupportActionBar().setTitle(mTitle);
            mNavAdapter.setSelectThemeId(-1);
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onThemeClick(int id, String name) {
        if (id != mSelectThemeId) {
            mSelectThemeId = id;
            startFragment(FragmentType.TYPE_THEME, id);
            mTitle = name;
            getSupportActionBar().setTitle(name);
            mNavAdapter.setSelectThemeId(id);
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onDestroy() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
        super.onDestroy();
    }

    public enum FragmentType {
        TYPE_HOME, TYPE_THEME
    }
}
