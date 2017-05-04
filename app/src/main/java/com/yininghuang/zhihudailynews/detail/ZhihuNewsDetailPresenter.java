package com.yininghuang.zhihudailynews.detail;

import android.content.Intent;

import com.google.gson.Gson;
import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;
import com.yininghuang.zhihudailynews.model.db.Favorite;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.CacheManager;
import com.yininghuang.zhihudailynews.utils.DBManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/19.
 */

public class ZhihuNewsDetailPresenter implements ZhihuNewsDetailContract.Presenter {

    private ZhihuNewsDetailContract.View mView;
    private RetrofitHelper mRetrofitHelper;
    private DBManager mDBManager;
    private CacheManager mCacheManager;

    private SubscriptionList mSubscriptions = new SubscriptionList();
    private int mDetailId;

    private ZhihuNewsContent mContent;

    public ZhihuNewsDetailPresenter(ZhihuNewsDetailContract.View view, RetrofitHelper retrofitHelper, DBManager dbManager, CacheManager cacheManager) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        mDBManager = dbManager;
        mCacheManager = cacheManager;
        mView.setPresenter(this);
    }

    @Override
    public void init(int id) {
        mView.setLoadingStatus(true);
        mDetailId = id;
        loadCache(String.valueOf(mDetailId));
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
                .subscribe(content -> {
                    showDataInView(content);
                    saveData(String.valueOf(mDetailId), content);
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mContent == null)
                        mView.showLoadError();
                    mView.setLoadingStatus(false);
                });
        mSubscriptions.add(sb);
    }

    private void showDataInView(ZhihuNewsContent content) {
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
        mView.setLoadingStatus(false);
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

    private void loadCache(String fileName) {
        String data = mCacheManager.getData(CacheManager.SUB_DIR_NEWS, fileName);
        if (data == null)
            return;

        mContent = new Gson().fromJson(data, ZhihuNewsContent.class);
        if (mContent.getType() != 1) {
            showDataInView(mContent);
            mView.setLoadingStatus(false);
        }
    }

    private void saveData(String fileName, ZhihuNewsContent content) {
        mCacheManager.saveData(CacheManager.SUB_DIR_NEWS, fileName, new Gson().toJson(content));
    }

    private void checkStar() {
        if (mDBManager.queryFavorite(mDetailId) == null)
            mView.setStarStatus(false);
        else
            mView.setStarStatus(true);
    }

    private String convertResult(String body) {
        body = body.replace("<div class=\"img-place-holder\">", "");
        String css = "<link rel=\"stylesheet\" href=\"zhihu_daily.css\" type=\"text/css\">";
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if (UserSettingConstants.DARK_THEME)
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
