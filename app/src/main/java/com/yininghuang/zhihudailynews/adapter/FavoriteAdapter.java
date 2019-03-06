package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.detail.ZhihuNewsDetailActivity;
import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;
import com.yininghuang.zhihudailynews.model.db.Favorite;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Favorite> mFavorites = new ArrayList<>();
    private Context mContext;

    public FavoriteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ZhihuNewsContent content = new Gson().fromJson(mFavorites.get(position).getContent(), ZhihuNewsContent.class);
        holder.mTitle.setText(content.getTitle());
        if (content.getImage() == null && content.getImages() == null) {
            holder.mPic.setVisibility(View.GONE);
        } else {
            holder.mPic.setVisibility(View.VISIBLE);
            String image = content.getImage();
            List<String> images = content.getImages();
            if (image != null && !image.isEmpty()) {
                ImageLoader.load(mContext, holder.mPic, image);
            } else if (images != null && images.size() > 0) {
                ImageLoader.load(mContext, holder.mPic, images.get(0));
            }
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ZhihuNewsDetailActivity.class);
            intent.putExtra("id", mFavorites.get(holder.getAdapterPosition()).getId());
            mContext.startActivity(intent);
        });
    }

    public void addFavorite(List<Favorite> favorites) {
        mFavorites.addAll(favorites);
    }

    public void addFavorite(Favorite favorite, int index) {
        mFavorites.add(index, favorite);
    }

    public List<Favorite> getFavorites() {
        return mFavorites;
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        ImageView mPic;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mPic = itemView.findViewById(R.id.pic);
        }
    }
}
