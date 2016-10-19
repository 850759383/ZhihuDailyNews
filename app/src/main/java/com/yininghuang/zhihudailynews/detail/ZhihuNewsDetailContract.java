package com.yininghuang.zhihudailynews.detail;

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

        void showLoadError();

        void setTitle(String title);
    }

    interface Presenter extends BasePresenter {

        void init(int id);

        void reload(int id);
    }
}
