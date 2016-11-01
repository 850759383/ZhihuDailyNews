package com.yininghuang.zhihudailynews.favorite;

import com.yininghuang.zhihudailynews.model.db.Favorite;
import com.yininghuang.zhihudailynews.utils.DBManager;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View mView;
    private DBManager mDBManager;

    public FavoritePresenter(FavoriteContract.View view, DBManager dbManager) {
        mView = view;
        mDBManager = dbManager;
        mView.setPresenter(this);
    }

    @Override
    public void init() {
        mView.setResults(mDBManager.queryFavoriteList());
    }

    @Override
    public void add(Favorite favorite) {
        mDBManager.addFavorite(favorite);
    }

    @Override
    public void remove(int id) {
        mDBManager.removeFavorite(id);
    }

    @Override
    public void clearAll() {
        mDBManager.clearAllFavorite();
    }

    @Override
    public void onStop() {

    }
}
