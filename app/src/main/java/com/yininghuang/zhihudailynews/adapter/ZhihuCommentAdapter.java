package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuComments;
import com.yininghuang.zhihudailynews.utils.CircleTransform;
import com.yininghuang.zhihudailynews.utils.DateUtils;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING = -1;
    private static final int TYPE_ITEM = 0;
    private List<ZhihuComments.ZhihuComment> mComments = new ArrayList<>();
    private Context mContext;
    private Boolean isLoadComplete = false;

    public ZhihuCommentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_status, parent, false);
            return new ZhihuLatestAdapter.LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder viewHolder = (CommentViewHolder) holder;
            ZhihuComments.ZhihuComment comment = mComments.get(position);
            ImageLoader.load(mContext, viewHolder.userImage, comment.getAvatar(), new CircleTransform(mContext), R.drawable.user_avatar);
            viewHolder.userName.setText(comment.getAuthor());
            viewHolder.content.setText(comment.getContent());
            viewHolder.postTime.setText(DateUtils.format(new Date(comment.getTime() * 1000L)));
            viewHolder.agreeCount.setText(String.valueOf(comment.getLikes()));
            if (comment.getReplyTo() != null) {
                viewHolder.replyTo.setVisibility(View.VISIBLE);
                viewHolder.replyTo.setText(
                        Html.fromHtml(mContext.getString(R.string.replyTo, comment.getReplyTo().getAuthor(), comment.getReplyTo().getContent()))
                );
            } else {
                viewHolder.replyTo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mComments.size())
            return TYPE_LOADING;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (mComments.size() == 0 || isLoadComplete) {
            return mComments.size();
        }
        return mComments.size() + 1;
    }

    public void addComments(List<ZhihuComments.ZhihuComment> comments) {
        mComments.addAll(comments);
    }

    public List<ZhihuComments.ZhihuComment> getComments() {
        return mComments;
    }

    public void setLoadComplete() {
        isLoadComplete = true;
        notifyItemRemoved(getItemCount() - 1);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView content;
        TextView replyTo;
        TextView postTime;
        TextView agreeCount;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.userImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            content = (TextView) itemView.findViewById(R.id.content);
            replyTo = (TextView) itemView.findViewById(R.id.replyTo);
            postTime = (TextView) itemView.findViewById(R.id.postTime);
            agreeCount = (TextView) itemView.findViewById(R.id.agreeCount);
        }
    }
}
