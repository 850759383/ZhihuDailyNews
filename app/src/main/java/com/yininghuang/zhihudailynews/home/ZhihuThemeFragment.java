package com.yininghuang.zhihudailynews.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.ZhihuThemeAdapter;
import com.yininghuang.zhihudailynews.comment.ZhihuTheme;
import com.yininghuang.zhihudailynews.utils.ItemDecoration;
import com.yininghuang.zhihudailynews.widget.AutoLoadRecyclerView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class ZhihuThemeFragment extends BaseFragment implements ZhihuThemeContract.View {

    @BindView(R.id.contentRec)
    AutoLoadRecyclerView mContentRec;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private ZhihuThemeAdapter mAdapter;
    private ZhihuThemeContract.Presenter mPresenter;
    private int mCurrentDy = 0;

    public static ZhihuThemeFragment newInstance(int themeId) {

        Bundle args = new Bundle();
        args.putInt("themeId", themeId);
        ZhihuThemeFragment fragment = new ZhihuThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        mContentRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ZhihuThemeAdapter(getActivity());
        mContentRec.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration;
        if (((BaseActivity) getActivity()).getThemeId() == BaseActivity.DARK_THEME)
            itemDecoration = new ItemDecoration(getActivity(), R.color.colorDividerDark);
        else itemDecoration = new ItemDecoration(getActivity(), R.color.colorDivider);
        mContentRec.addItemDecoration(itemDecoration);
        mPresenter.setThemeId(getArguments().getInt("themeId"));
        if (savedInstanceState != null) {
            Type type = new TypeToken<List<ZhihuTheme>>() {
            }.getType();
            List<ZhihuTheme> data = new Gson().fromJson(savedInstanceState.getString("data"), type);
            mAdapter.addThemes(data);
            mAdapter.notifyDataSetChanged();
            mCurrentDy = savedInstanceState.getInt("dy");
            mContentRec.scrollTo(0, mCurrentDy);
        } else {
            mPresenter.init();
        }

        mContentRec.setOnLoadingListener(new AutoLoadRecyclerView.OnLoadingListener() {
            @Override
            public void onLoad() {
                if (mAdapter.getStories().size() == 0)
                    return;
                mPresenter.queryHistoryStory(mAdapter.getStories().get(mAdapter.getStories().size() - 1).getId());
            }
        });

        mContentRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentDy = +dy;
            }
        });

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.reload();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter.getZhihuThemes().size() != 0) {
            outState.putString("data", new Gson().toJson(mAdapter.getZhihuThemes()));
            outState.putInt("dy", mCurrentDy);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zhihu_theme, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void showStories(ZhihuTheme theme) {
        mAdapter.getZhihuThemes().clear();
        mAdapter.getStories().clear();
        mAdapter.addTheme(theme);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addHistoryStories(ZhihuTheme theme) {
        mAdapter.addTheme(theme);
        mAdapter.notifyItemRangeInserted(mAdapter.getStories().size() - theme.getStories().size() + 1, theme.getStories().size());
    }

    @Override
    public void setHistoryLoadingStatus(boolean status) {
        mContentRec.setRefresh(status);
    }

    @Override
    public void setLoadingStatus(boolean status) {
        mSwipeLayout.setRefreshing(status);
    }

    @Override
    public void showLoadError() {
        if (getView() != null)
            Snackbar.make(getView(), R.string.load_error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.refresh, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.reload();
                        }
                    }).show();
    }

    @Override
    public void setPresenter(ZhihuThemeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
