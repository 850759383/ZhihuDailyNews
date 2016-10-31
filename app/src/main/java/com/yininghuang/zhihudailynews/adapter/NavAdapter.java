package com.yininghuang.zhihudailynews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuThemes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public class NavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOME = 0;
    private static final int TYPE_THEME = 1;

    private Context mContext;
    private List<ZhihuThemes.OthersBean> mThemes = new ArrayList<>();
    private OnNavItemClickListener mOnNavItemClickListener;

    private int mSelectThemeId = -1;

    public NavAdapter(Context context) {
        mContext = context;
    }

    public void setThemes(List<ZhihuThemes.OthersBean> themes) {
        mThemes.clear();
        mThemes.addAll(themes);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HOME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_home, parent, false);
            return new HomeHolder(view);
        } else if (viewType == TYPE_THEME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_theme, parent, false);
            return new ThemeHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNavItemClickListener != null)
                        mOnNavItemClickListener.onHomeClick();
                }
            });

            if (mSelectThemeId == -1)
                ((HomeHolder) holder).mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlackTranslucent95));
            else
                ((HomeHolder) holder).mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorTranslucent));
        } else if (holder instanceof ThemeHolder) {
            ThemeHolder themeHolder = (ThemeHolder) holder;
            themeHolder.mTitle.setText(mThemes.get(position - 1).getName());

            themeHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNavItemClickListener != null)
                        mOnNavItemClickListener.onThemeClick(mThemes.get(holder.getAdapterPosition() - 1).getId(),
                                mThemes.get(holder.getAdapterPosition() - 1).getName());
                }
            });

            if (mSelectThemeId == mThemes.get(position - 1).getId())
                themeHolder.mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlackTranslucent95));
            else
                themeHolder.mBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorTranslucent));
        }
    }

    public void setSelectThemeId(int id) {
        mSelectThemeId = id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mThemes == null)
            return 1;
        return mThemes.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HOME;
        return TYPE_THEME;
    }

    public void setOnNavItemClickListener(OnNavItemClickListener onNavItemClickListener) {
        mOnNavItemClickListener = onNavItemClickListener;
    }

    public interface OnNavItemClickListener {

        void onHomeClick();

        void onThemeClick(int id, String name);
    }

    public static class HomeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.background)
        RelativeLayout mBackground;

        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ThemeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView mTitle;

        @BindView(R.id.subscribe)
        ImageView mSubscribe;

        @BindView(R.id.background)
        RelativeLayout mBackground;

        public ThemeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
