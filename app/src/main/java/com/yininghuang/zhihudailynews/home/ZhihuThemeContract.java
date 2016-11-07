package com.yininghuang.zhihudailynews.home;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.ZhihuTheme;
import com.yininghuang.zhihudailynews.model.ZhihuThemes;

import java.util.List;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class ZhihuThemeContract {

    interface View extends BaseView<Presenter> {

        void showStories(ZhihuTheme theme);

        void addHistoryStories(ZhihuTheme theme);

        void setHistoryLoadingStatus(boolean status);

        void setLoadingStatus(boolean status);

        void showLoadError();

        void setReadIdList(List<String> list);
    }

    interface Presenter extends BasePresenter {

        void init();

        void reload();

        void queryHistoryStory(int id);

        void queryReadIdList();

        void setThemeId(int id);
    }
}
