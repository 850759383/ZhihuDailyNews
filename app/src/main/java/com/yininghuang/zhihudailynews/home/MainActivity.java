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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.NavAdapter;
import com.yininghuang.zhihudailynews.favorite.FavoriteActivity;
import com.yininghuang.zhihudailynews.model.ZhihuThemes;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.net.ZhihuThemeService;
import com.yininghuang.zhihudailynews.settings.SettingsActivity;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;
import com.yininghuang.zhihudailynews.utils.CacheManager;

import java.lang.reflect.Type;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavAdapter.OnNavItemClickListener {

    private static final int LIGHT_THEME = R.style.AppTheme_NoActionBar_TranslucentStatusBar;
    private static final int DARK_THEME = R.style.AppThemeDark_NoActionBar_TranslucentStatusBar;

    private Toolbar toolbar;
    private RecyclerView mNavRec;
    private DrawerLayout mDrawerLayout;

    private int mSelectThemeId = -1;
    private String mTitle;
    private NavAdapter mNavAdapter;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.DARK_THEME)
            setTheme(DARK_THEME);
        else setTheme(LIGHT_THEME);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavRec = (RecyclerView) findViewById(R.id.navRec);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);

        mNavRec.setLayoutManager(new LinearLayoutManager(this));
        mNavAdapter = new NavAdapter(this);
        mNavRec.setAdapter(mNavAdapter);
        mNavAdapter.setOnNavItemClickListener(this);

        fetchThemes();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null != fragment) {
            if (fragment instanceof ZhihuDailyFragment)
                new ZhihuDailyPresenter((ZhihuDailyContract.View) fragment,
                        RetrofitHelper.getInstance().createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL),
                        CacheManager.getInstance(this));
            else if (fragment instanceof ZhihuThemeFragment)
                new ZhihuThemePresenter(
                        (ZhihuThemeContract.View) fragment,
                        RetrofitHelper.getInstance().createRetrofit(ZhihuThemeService.class, Api.ZHIHU_BASE_URL),
                        CacheManager.getInstance(this));
        } else {
            startFragment(ZhihuDailyFragment.class, null);
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

    private void startFragment(Class<? extends Fragment> clazz, @Nullable Integer id) {
        String name = clazz.getName();
        if (name.equals(ZhihuDailyFragment.class.getName())) {
            ZhihuDailyFragment fragment = ZhihuDailyFragment.newInstance();
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame,
                    name);
            new ZhihuDailyPresenter(fragment,
                    RetrofitHelper.getInstance().createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL),
                    CacheManager.getInstance(this));
        } else if (name.equals(ZhihuThemeFragment.class.getName())) {
            ZhihuThemeFragment fragment = ZhihuThemeFragment.newInstance(id);
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame,
                    name);
            new ZhihuThemePresenter(
                    fragment,
                    RetrofitHelper.getInstance().createRetrofit(ZhihuThemeService.class, Api.ZHIHU_BASE_URL),
                    CacheManager.getInstance(this));
        }
    }

    public void fetchThemes() {
        String data = CacheManager.getInstance(this).getData(CacheManager.SUB_DIR_THEMES, "theme");
        if (data != null) {
            Type type = new TypeToken<List<ZhihuThemes.OthersBean>>() {
            }.getType();
            List<ZhihuThemes.OthersBean> cacheThemes = new Gson().fromJson(data, type);
            setThemeData(cacheThemes);
        }

        mSubscription = RetrofitHelper.getInstance()
                .createRetrofit(ZhihuThemeService.class, Api.ZHIHU_BASE_URL)
                .getThemes()
                .map(ZhihuThemes::getOthers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(othersBeen -> {
                    setThemeData(othersBeen);
                    CacheManager.getInstance(MainActivity.this)
                            .saveData(CacheManager.SUB_DIR_THEMES, "theme", new Gson().toJson(othersBeen));
                }, Throwable::printStackTrace);
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

    private void setThemeData(List<ZhihuThemes.OthersBean> themes) {
        mNavAdapter.setThemes(themes);
        mNavAdapter.notifyDataSetChanged();
    }

    @Override
    public void onThemeChange() {
        if (UserSettingConstants.DARK_THEME) {
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
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
            }
            case R.id.star: {
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;
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
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
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
            startFragment(ZhihuDailyFragment.class, null);
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
            startFragment(ZhihuThemeFragment.class, id);
            mTitle = name;
            getSupportActionBar().setTitle(name);
            mNavAdapter.setSelectThemeId(id);
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }
}
