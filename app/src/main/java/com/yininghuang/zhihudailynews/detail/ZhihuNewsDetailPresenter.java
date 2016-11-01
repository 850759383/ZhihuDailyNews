package com.yininghuang.zhihudailynews.detail;

import android.content.Intent;

import com.google.gson.Gson;
import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;
import com.yininghuang.zhihudailynews.model.db.Favorite;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.DBManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/19.
 */

public class ZhihuNewsDetailPresenter implements ZhihuNewsDetailContract.Presenter {

    private ZhihuNewsDetailContract.View mView;
    private RetrofitHelper mRetrofitHelper;
    private DBManager mDBManager;

    private SubscriptionList mSubscriptions = new SubscriptionList();
    private int mDetailId;

    private ZhihuNewsContent mContent;

    public ZhihuNewsDetailPresenter(ZhihuNewsDetailContract.View view, RetrofitHelper retrofitHelper, DBManager dbManager) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        mDBManager = dbManager;
        mView.setPresenter(this);
    }

    @Override
    public void init(int id) {
        mDetailId = id;
        fetchNewsContent(id);
        checkStar();
    }

    @Override
    public void reload() {
        fetchNewsContent(mDetailId);
    }

    private void fetchNewsContent(int id) {
        Subscription sb = mRetrofitHelper.createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL)
                .getNewsContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuNewsContent>() {
                    @Override
                    public void call(ZhihuNewsContent content) {
                        mContent = content;
                        if (!UserSettingConstants.NO_IMAGE_MODE)
                            mView.setBlockImageDisplay(false);
                        else
                            mView.setBlockImageDisplay(true);

                        if (content.getType() == 0) {
                            mView.showBody(convertResult(content.getBody()));
                            mView.showAppBarImage(content.getImage());
                            mView.setImageSource(content.getImageSource());
                            mView.setTitle(content.getTitle());
                        } else if (content.getType() == 1) {
                            mView.showEmptyBody(content.getShareUrl());
                        } else {
                            mView.showBody(convertResult(content.getBody()));
                            if (content.getImages().size() > 0)
                                mView.showAppBarImage(content.getImages().get(0));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.showLoadError();
                    }
                });
        mSubscriptions.add(sb);
    }

    private void checkStar() {
        if (mDBManager.queryFavorite(mDetailId) == null)
            mView.setStarStatus(false);
        else
            mView.setStarStatus(true);
    }

    @Override
    public void star() {
        if (mContent == null)
            return;

        if (mDBManager.queryFavorite(mContent.getId()) != null) {
            mDBManager.removeFavorite(mContent.getId());
            mView.setStarStatus(false);
        } else {
            mDBManager.addFavorite(new Favorite(
                    mContent.getId(),
                    (int) (System.currentTimeMillis() / 1000),
                    new Gson().toJson(mContent)));
            mView.setStarStatus(true);
        }
    }

    @Override
    public void share() {
        if (mContent == null)
            return;

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContent.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, mContent.getShareUrl());
        shareIntent.setType("text/plain");
        mView.startShareChooser(shareIntent);
    }

    private String convertResult(String body) {
        body = body.replace("<div class=\"img-place-holder\">", "");
        String css = "<link rel=\"stylesheet\" href=\"zhihu_daily.css\" type=\"text/css\">";
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if (UserSettingConstants.DARK_MODE)
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<meta charset=\"utf-8\" />" +
                css +
                "\n</head>\n" +
                theme +
                body +
                "</body>" +
                "</html>";
    }

    @Override
    public void onStop() {
        mSubscriptions.unsubscribe();
    }
}
