package com.yininghuang.zhihudailynews;

import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
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

    public ZhihuDailyPresenter(ZhihuDailyContract.View view, RetrofitHelper retrofitHelper){
        mView = view;
        mRetrofitHelper = retrofitHelper;
        view.setPresenter(this);
    }

    @Override
    public void init() {
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuDailyService.class, Constans.ZHIHU_BASE_URL)
                .getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuLatestNews>() {
                    @Override
                    public void call(ZhihuLatestNews zhihuLatestNews) {
                        mView.showStories(zhihuLatestNews);
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
    public void update() {
        init();
    }

    @Override
    public void queryHistoryStory(String date) {
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuDailyService.class, Constans.ZHIHU_BASE_URL)
                .getHistoryNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuLatestNews>() {
                    @Override
                    public void call(ZhihuLatestNews zhihuLatestNews) {
                        mView.addHistoryStories(zhihuLatestNews);
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
    public void onStop() {
        subscriptions.unsubscribe();
    }
}
