package com.yininghuang.zhihudailynews.home;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.utils.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    private SubscriptionList subscriptions = new SubscriptionList();
    private ZhihuDailyService mService;
    private CacheManager mCacheManager;
    private ZhihuDailyContract.View mView;

    public ZhihuDailyPresenter(ZhihuDailyContract.View view, ZhihuDailyService service, CacheManager cacheManager) {
        this.mView = view;
        this.mService = service;
        this.mCacheManager = cacheManager;
        view.setPresenter(this);
    }

    @Override
    public void init() {
        queryReadId();
        String data = getData(CacheManager.SUB_DIR_THEMES, "home");
        if (data != null) {
            ZhihuLatestNews news = new Gson().fromJson(data, ZhihuLatestNews.class);
            mView.showStories(news);
        }
        fetchStory();
    }

    @Override
    public void reload() {
        queryReadId();
        fetchStory();
    }

    public void fetchStory() {
        mView.setLoadingStatus(true);
        Subscription sb = mService.getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(zhihuLatestNews -> {
                    mView.showStories(zhihuLatestNews);
                    mView.setLoadingStatus(false);
                    saveData(CacheManager.SUB_DIR_THEMES, "home", new Gson().toJson(zhihuLatestNews));
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setLoadingStatus(false);
                    mView.showLoadError();
                });
        subscriptions.add(sb);
    }

    @Override
    public void queryReadId() {
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
    public void queryHistoryStory(String date) {
        mView.setHistoryLoadingStatus(true);
        if (date == null)
            return;
        Subscription sb = mService.getHistoryNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(zhihuLatestNews -> {
                    mView.addHistoryStories(zhihuLatestNews);
                    mView.setHistoryLoadingStatus(false);
                    if (zhihuLatestNews.getStories().size() == 0)
                        mView.setLoadingComplete();
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setHistoryLoadingStatus(false);
                });
        subscriptions.add(sb);
    }

    private void saveData(String dir, String name, String data) {
        mCacheManager.saveData(dir, name, data);
    }

    @Nullable
    private String getData(String dir, String name) {
        return mCacheManager.getData(dir, name);
    }

    @Override
    public void onStop() {
        subscriptions.unsubscribe();
    }
}
