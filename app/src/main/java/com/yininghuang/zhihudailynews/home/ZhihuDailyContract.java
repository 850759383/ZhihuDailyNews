package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyContract {

    public interface Presenter extends BasePresenter {

        void init();

        void update();

        void queryHistoryStory(String date);
    }

    public interface View extends BaseView<Presenter> {

        void showStories(ZhihuLatestNews stories);

        void addHistoryStories(ZhihuLatestNews stories);
    }
}
