package com.yininghuang.zhihudailynews.home;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.utils.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    private SubscriptionList subscriptions = new SubscriptionList();
    private RetrofitHelper mRetrofitHelper;
    private CacheManager mCacheManager;
    private ZhihuDailyContract.View mView;

    public ZhihuDailyPresenter(ZhihuDailyContract.View view, RetrofitHelper retrofitHelper, CacheManager cacheManager) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        mCacheManager = cacheManager;
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

    private void fetchStory() {
        mView.setLoadingStatus(true);
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL)
                .getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuLatestNews>() {
                    @Override
                    public void call(ZhihuLatestNews zhihuLatestNews) {
                        mView.showStories(zhihuLatestNews);
                        mView.setLoadingStatus(false);
                        saveData(CacheManager.SUB_DIR_THEMES, "home", new Gson().toJson(zhihuLatestNews));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.setLoadingStatus(false);
                        mView.showLoadError();
                    }
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
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL)
                .getHistoryNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuLatestNews>() {
                    @Override
                    public void call(ZhihuLatestNews zhihuLatestNews) {
                        mView.addHistoryStories(zhihuLatestNews);
                        mView.setHistoryLoadingStatus(false);
                        if (zhihuLatestNews.getStories().size() == 0)
                            mView.setLoadingComplete();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.setHistoryLoadingStatus(false);
                    }
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
