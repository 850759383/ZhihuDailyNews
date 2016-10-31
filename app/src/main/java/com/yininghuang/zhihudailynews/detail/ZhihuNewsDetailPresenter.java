package com.yininghuang.zhihudailynews.detail;

import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

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

    private SubscriptionList mSubscriptions = new SubscriptionList();
    private int mDetailId;

    public ZhihuNewsDetailPresenter(ZhihuNewsDetailContract.View view, RetrofitHelper retrofitHelper) {
        mView = view;
        mRetrofitHelper = retrofitHelper;
        mView.setPresenter(this);
    }

    @Override
    public void init(int id) {
        mDetailId = id;
        fetchNewsContent(id);
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

    @SuppressWarnings("StringBufferReplaceableByString")
    private String convertResult(String preReuslt) {
        preReuslt = preReuslt.replace("<div class=\"img-place-holder\">", "");
        preReuslt = preReuslt.replace("<div class=\"headline\">", "");
        String css = "<link rel=\"stylesheet\" href=\"zhihu_daily.css\" type=\"text/css\">";
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if (UserSettingConstants.DARK_MODE)
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preReuslt)
                .append("</body></html>").toString();
    }

    @Override
    public void onStop() {
        mSubscriptions.unsubscribe();
    }
}
