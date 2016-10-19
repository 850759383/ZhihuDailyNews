package com.yininghuang.zhihudailynews.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.ZhihuLatestAdapter;
import com.yininghuang.zhihudailynews.detail.ZhihuNewsDetailActivity;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyFragment extends BaseFragment implements ZhihuDailyContract.View, ZhihuLatestAdapter.OnItemClickListener {

    private ZhihuDailyContract.Presenter mPresenter;
    private ZhihuLatestAdapter mAdapter;

    private int mLastVisiablePosition = 0;
    private Boolean isLoading = false;

    @BindView(R.id.contentRec)
    RecyclerView contentRec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zhihudaily, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        contentRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ZhihuLatestAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        contentRec.setAdapter(mAdapter);
        mPresenter.init();

        contentRec.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisiablePosition = ((LinearLayoutManager) contentRec.getLayoutManager()).findLastVisibleItemPosition();
                Log.d("hyn", mLastVisiablePosition + "--" + mAdapter.getItemCount());
                if (mLastVisiablePosition > mAdapter.getItemCount() - 2 && !isLoading && dy > 0) {
                    isLoading = true;
                    List<ZhihuLatestNews> latestNewses = mAdapter.getLatestNewsList();
                    mPresenter.queryHistoryStory(latestNewses.get(latestNewses.size() - 1).getDate());
                }
            }
        });
    }

    public static ZhihuDailyFragment newInstance() {
        return new ZhihuDailyFragment();
    }

    @Override
    public void showStories(ZhihuLatestNews stories) {
        mAdapter.addNews(stories);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addHistoryStories(ZhihuLatestNews stories) {
        isLoading = false;
        mAdapter.addNews(stories);
        mAdapter.notifyItemRangeInserted(mAdapter.getZhihuStoryList().size() - stories.getStories().size() + 1, stories.getStories().size());
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

    @Override
    public void onPosterClick(ZhihuLatestNews.ZhihuTopStory topStory) {
        Log.d("zhihu", topStory.getTitle());
        Intent intent = new Intent(getActivity(), ZhihuNewsDetailActivity.class);
        intent.putExtra("id", topStory.getId());
        startActivity(intent);
    }

    @Override
    public void onNewsClick(ZhihuLatestNews.ZhihuStory story) {
        Log.d("zhihu", story.getTitle());
        Intent intent = new Intent(getActivity(), ZhihuNewsDetailActivity.class);
        intent.putExtra("id", story.getId());
        startActivity(intent);
    }
}
