package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.utils.ImageLoader;
import com.yininghuang.zhihudailynews.widget.PosterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuLatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ZhihuLatestNews> mLatestNewsList = new ArrayList<>();

    private int TYPE_POSTER = 0;
    private int TYPE_ITEM = 1;

    private OnItemClickListener mOnItemClickListener;

    public ZhihuLatestAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
            return new PosterHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu_lastest, parent, false);
            return new NewsHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterHolder) {
            PosterView posterView = ((PosterHolder) holder).getPosterView();
            if (mLatestNewsList.size() > 0){
                posterView.initPosters(mLatestNewsList.get(0).getTopStories());
                posterView.setOnPosterClickListener(new PosterView.OnPosterClickListener() {
                    @Override
                    public void onPosterClick(ZhihuLatestNews.ZhihuTopStory story) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onPosterClick(story);
                    }
                });
            }
        } else if (holder instanceof NewsHolder) {
            ZhihuLatestNews.ZhihuStory itemData = getCurrentItem(position);
            ((NewsHolder) holder).getTitle().setText(itemData.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onNewsClick(getCurrentItem(holder.getAdapterPosition()));
                }
            });

            if (itemData.getImages().size() == 0)
                return;
            ImageLoader.load(mContext, ((NewsHolder) holder).getImageView(), itemData.getImages().get(0));
        }
    }

    private ZhihuLatestNews.ZhihuStory getCurrentItem(int position) {
        int firstStorySize = mLatestNewsList.get(0).getStories().size();//11
        if (position <= firstStorySize + 1) {
            return mLatestNewsList.get(0).getStories().get(position -1);
        }
        int p2= position - firstStorySize + 1;
        int index1 = 1 + p2 / 20;
        int index2 = p2 % 20 - 1;
        return mLatestNewsList.get(index1).getStories().get(index2);
    }

    public void addNews(ZhihuLatestNews news) {
        mLatestNewsList.add(news);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        for (ZhihuLatestNews news : mLatestNewsList) {
            size += news.getStories().size();
        }
        return size + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_POSTER;
        return TYPE_ITEM;
    }

    public interface OnItemClickListener {

        void onPosterClick(ZhihuLatestNews.ZhihuTopStory topStory);

        void onNewsClick(ZhihuLatestNews.ZhihuStory story);
    }

    public static class PosterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.posterView)
        PosterView posterView;

        public PosterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public PosterView getPosterView() {
            return posterView;
        }
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.pic)
        ImageView imageView;

        public NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
