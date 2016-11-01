package com.yininghuang.zhihudailynews.favorite;

import com.yininghuang.zhihudailynews.BasePresenter;
import com.yininghuang.zhihudailynews.BaseView;
import com.yininghuang.zhihudailynews.model.db.Favorite;

import java.util.List;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoriteContract {

    interface View extends BaseView<Presenter> {

        void setResults(List<Favorite> favorites);
    }

    interface Presenter extends BasePresenter {

        void init();

        void remove(int id);

        void add(Favorite favorite);

        void clearAll();
    }
}
