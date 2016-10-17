package com.yininghuang.zhihudailynews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.widget.PosterView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyFragment extends BaseFragment implements ZhihuDailyContract.View {

    private ZhihuDailyContract.Presenter mPresenter;

    @BindView(R.id.posterView)
    PosterView posterView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zhihudaily, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        mPresenter.init();
    }

    public static ZhihuDailyFragment newInstance(){
        return new ZhihuDailyFragment();
    }

    @Override
    public void showStories(ZhihuLatestNews stories) {
        posterView.initViews(stories.getTopStories());
    }

    @Override
    public void addHistoryStories(ZhihuLatestNews stories) {

    }

    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onStop();
    }
}
