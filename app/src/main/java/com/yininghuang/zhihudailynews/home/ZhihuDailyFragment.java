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
import com.yininghuang.zhihudailynews.utils.ItemDecoration;
import com.yininghuang.zhihudailynews.widget.AutoLoadRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ZhihuDailyFragment extends BaseFragment implements ZhihuDailyContract.View, ZhihuLatestAdapter.OnItemClickListener {

    private ZhihuDailyContract.Presenter mPresenter;
    private ZhihuLatestAdapter mAdapter;

    private boolean isLoading = true;


    @BindView(R.id.contentRec)
    AutoLoadRecyclerView mContentRec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zhihudaily, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        mContentRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ZhihuLatestAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        mContentRec.setAdapter(mAdapter);
        mContentRec.addItemDecoration(new ItemDecoration(getResources().getDrawable(R.drawable.divider)));
        mPresenter.init();

        mContentRec.setOnLoadingListener(new AutoLoadRecyclerView.OnLoadingListener() {
            @Override
            public void onLoad() {
                if (isLoading)
                    return;
                List<ZhihuLatestNews> latestNewses = mAdapter.getLatestNewsList();
                mPresenter.queryHistoryStory(latestNewses.get(latestNewses.size() - 1).getDate());
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
        mAdapter.addNews(stories);
        mAdapter.notifyItemRangeInserted(mAdapter.getZhihuStoryList().size() - stories.getStories().size() + 1, stories.getStories().size());
    }

    @Override
    public void setLoadingStatus(Boolean status) {
        isLoading = status;
    }

    @Override
    public void setLoadingComplete() {
        mContentRec.setLoadComplete();
        mAdapter.setLoadComplete();
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
