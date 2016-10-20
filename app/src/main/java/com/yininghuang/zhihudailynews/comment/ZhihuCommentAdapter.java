package com.yininghuang.zhihudailynews.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.adapter.ZhihuLatestAdapter;
import com.yininghuang.zhihudailynews.model.ZhihuComments;
import com.yininghuang.zhihudailynews.utils.CircleTransform;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public class ZhihuCommentAdapter extends RecyclerView.Adapter {

    List<ZhihuComments.ZhihuComment> mComments = new ArrayList<>();
    private Context mContext;

    private static final int TYPE_LOADING = -1;
    private static final int TYPE_ITEM = 0;

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
            ImageLoader.load(mContext, viewHolder.getUserImage(), comment.getAvatar(), new CircleTransform(mContext));
            viewHolder.getUserName().setText(comment.getAuthor());
            viewHolder.getContent().setText(comment.getContent());
            viewHolder.getPostTime().setText(comment.getTime() + "");
            if (comment.getReplyTo() != null) {
                viewHolder.getReplyTo().setVisibility(View.VISIBLE);
                viewHolder.getReplyTo().setText("回复 " + comment.getReplyTo().getAuthor() + "：" + comment.getReplyTo().getContent());
            } else {
                viewHolder.getReplyTo().setVisibility(View.GONE);
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
        if (mComments.size() == 0) {
            return 0;
        }
        return mComments.size() + 1;
    }

    public void addComments(List<ZhihuComments.ZhihuComment> comments) {
        mComments.addAll(comments);
    }

    public List<ZhihuComments.ZhihuComment> getComments() {
        return mComments;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userImage)
        ImageView userImage;

        @BindView(R.id.userName)
        TextView userName;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.replyTo)
        TextView replyTo;

        @BindView(R.id.postTime)
        TextView postTime;

        @BindView(R.id.agreeCount)
        TextView agreeCount;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getUserImage() {
            return userImage;
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getReplyTo() {
            return replyTo;
        }

        public TextView getPostTime() {
            return postTime;
        }

        public TextView getAgreeCount() {
            return agreeCount;
        }
    }
}
