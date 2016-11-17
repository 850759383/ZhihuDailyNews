package com.yininghuang.zhihudailynews.comment;

import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuCommentService;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentPresenter implements ZhihuCommentContract.Presenter {

    private ZhihuCommentContract.View mView;
    private RetrofitHelper mRetrofitHelper;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    private int newsId = 0;

    public ZhihuCommentPresenter(ZhihuCommentContract.View view, RetrofitHelper retrofitHelper) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        mView.setPresenter(this);
    }

    @Override
    public void init(int newsId) {
        this.newsId = newsId;
        fetchComments();
    }

    @Override
    public void reload() {
        fetchComments();
    }

    private void fetchComments() {
        mView.setLoadingStatus(true);
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuCommentService.class, Api.ZHIHU_BASE_URL)
                .getComments(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(zhihuComments -> {
                    mView.showCommentsCount(zhihuComments.getCount());
                    mView.showComments(zhihuComments.getComments());
                    mView.setLoadingStatus(false);
                    if (zhihuComments.getComments().size() < 20)
                        mView.showLoadComplete();
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.showLoadError();
                    mView.setLoadingStatus(false);
                });
        mSubscriptionList.add(sb);
    }

    @Override
    public void queryHistoryComments(int userId) {
        mView.setHistoryLoadingStatus(true);
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuCommentService.class, Api.ZHIHU_BASE_URL)
                .getHistoryComments(newsId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(zhihuComments -> {
                    mView.addHistoryComment(zhihuComments.getComments());
                    mView.setHistoryLoadingStatus(false);
                    if (zhihuComments.getComments().size() < 20)
                        mView.showLoadComplete();
                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setHistoryLoadingStatus(false);
                });
        mSubscriptionList.add(sb);
    }

    @Override
    public void onStop() {
        mSubscriptionList.unsubscribe();
    }
}
