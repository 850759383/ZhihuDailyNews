package com.yininghuang.zhihudailynews.detail;

import android.content.Intent;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;

/**
 * Created by Yining Huang on 2016/10/19.
 */

public class ZhihuNewsDetailContract {

    interface View extends BaseView<Presenter> {

        void showBody(String body);

        void showEmptyBody(String url);

        void showAppBarImage(String url);

        void setLoadingStatus(boolean status);

        void showLoadError();

        void setBlockImageDisplay(Boolean status);

        void setTitle(String title);

        void setImageSource(String source);

        void startShareChooser(Intent intent);

        void setStarStatus(boolean status);
    }

    interface Presenter extends BasePresenter {

        void init(int id);

        void reload();

        void share();

        void star();
    }
}
