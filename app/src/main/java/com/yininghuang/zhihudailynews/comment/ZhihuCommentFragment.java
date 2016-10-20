package com.yininghuang.zhihudailynews.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuComments;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentFragment extends BaseFragment implements ZhihuCommentContract.View {

    private View mRootView;
    private ZhihuCommentContract.Presenter mPresenter;
    private ZhihuCommentAdapter mAdapter;

    @BindView(R.id.contentRec)
    RecyclerView mContentRec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_zhihu_comment, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        mContentRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ZhihuCommentAdapter(getActivity());
        mContentRec.setAdapter(mAdapter);
        mPresenter.init(getArguments().getInt("id"));
    }

    public static ZhihuCommentFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        ZhihuCommentFragment fragment = new ZhihuCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showComments(List<ZhihuComments.ZhihuComment> comments) {
        mAdapter.addComments(comments);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCommentsCount(int count) {
        getActivity().setTitle("评论(" + count + ")");
    }

    @Override
    public void addHistoryComment(List<ZhihuComments.ZhihuComment> comments) {
        mAdapter.addComments(comments);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - comments.size(), comments.size());
    }

    @Override
    public void showLoadError() {

    }

    @Override
    public void showLoadComplete() {

    }

    @Override
    public void setPresenter(ZhihuCommentContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
