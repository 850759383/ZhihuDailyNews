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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    List<Favorite> mFavorites = new ArrayList<>();
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
        if (content.getImage() == null) {
            holder.mPic.setVisibility(View.GONE);
        } else {
            holder.mPic.setVisibility(View.VISIBLE);
            ImageLoader.load(mContext, holder.mPic, content.getImage());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZhihuNewsDetailActivity.class);
                intent.putExtra("id", mFavorites.get(holder.getAdapterPosition()).getId());
                mContext.startActivity(intent);
            }
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

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.pic)
        ImageView mPic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
