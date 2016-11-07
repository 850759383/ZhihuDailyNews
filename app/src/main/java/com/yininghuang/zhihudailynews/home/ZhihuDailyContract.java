package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyContract {

    public interface Presenter extends BasePresenter {

        void init();

        void reload();

        void queryHistoryStory(String date);

        void queryReadId();
    }

    public interface View extends BaseView<Presenter> {

        void setHistoryLoadingStatus(boolean status);

        void setLoadingStatus(boolean status);

        void setLoadingComplete();

        void showLoadError();

        void showStories(ZhihuLatestNews stories);

        void addHistoryStories(ZhihuLatestNews stories);

        void setReadIdList(List<String> list);
    }
}
