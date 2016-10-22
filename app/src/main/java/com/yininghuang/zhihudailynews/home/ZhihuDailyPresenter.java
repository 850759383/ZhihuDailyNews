package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.Constants;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;

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
    private ZhihuDailyContract.View mView;

    public ZhihuDailyPresenter(ZhihuDailyContract.View view, RetrofitHelper retrofitHelper) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        view.setPresenter(this);
    }

    @Override
    public void init() {
        fetchStory();
    }

    @Override
    public void reload() {
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
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        subscriptions.add(sb);
    }

    @Override
    public void queryHistoryStory(String date) {
        mView.setLoadingStatus(true);
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
                        mView.setLoadingStatus(false);
                        if (zhihuLatestNews.getStories().size() == 0)
                            mView.setLoadingComplete();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.setLoadingStatus(false);
                    }
                });
        subscriptions.add(sb);
    }

    @Override
    public void onStop() {
        subscriptions.unsubscribe();
    }
}
