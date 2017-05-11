package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuThemeService;
import com.yininghuang.zhihudailynews.utils.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class ZhihuThemePresenter implements ZhihuThemeContract.Presenter {

    private ZhihuThemeService mService;
    private ZhihuThemeContract.View mView;
    private CacheManager mCacheManager;
    private SubscriptionList mSubscriptions = new SubscriptionList();
    private int mThemeId = -1;

    public ZhihuThemePresenter(ZhihuThemeContract.View view, ZhihuThemeService service, CacheManager cacheManager) {
        this.mService = service;
        this.mView = view;
        this.mCacheManager = cacheManager;
        mView.setPresenter(this);
    }

    @Override
    public void setThemeId(int id) {
        mThemeId = id;
    }

    @Override
    public void init() {
        queryReadIdList();
        fetchTheme();
    }

    @Override
    public void reload() {
        queryReadIdList();
        fetchTheme();
    }

    private void fetchTheme() {
        mView.setLoadingStatus(true);
        Subscription sb = mService.getTheme(mThemeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(theme -> {
                    mView.showStories(theme);
                    mView.setLoadingStatus(false);
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setLoadingStatus(false);
                    mView.showLoadError();
                });
        mSubscriptions.add(sb);
    }

    @Override
    public void queryReadIdList() {
        File file = new File(mCacheManager.getSubCacheDir(CacheManager.SUB_DIR_NEWS));
        if (!file.exists())
            return;

        List<String> list = new ArrayList<>();
        for (File f : file.listFiles()) {
            list.add(f.getName());
        }
        mView.setReadIdList(list);
    }

    @Override
    public void queryHistoryStory(int id) {
        mView.setHistoryLoadingStatus(true);
        Subscription sb = mService.getHistoryThemeStory(mThemeId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(theme -> {
                    mView.addHistoryStories(theme);
                    mView.setHistoryLoadingStatus(false);
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setHistoryLoadingStatus(false);
                });
        mSubscriptions.add(sb);
    }

    @Override
    public void onStop() {
        mSubscriptions.unsubscribe();
    }
}
