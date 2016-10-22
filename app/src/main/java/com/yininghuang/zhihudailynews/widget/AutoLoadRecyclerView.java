package com.yininghuang.zhihudailynews.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class AutoLoadRecyclerView extends RecyclerView {

    private OnLoadingListener mOnLoadingListener;
    private boolean isLoadingComplete = false;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        mOnLoadingListener = listener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (!(getLayoutManager() instanceof LinearLayoutManager))
            throw new IllegalArgumentException("LayoutManager must be subclass of LinearLayoutManager");
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        if (lastVisiblePosition > layoutManager.getItemCount() - 2 && dy > 0 && !isLoadingComplete) {
            if (mOnLoadingListener != null)
                mOnLoadingListener.onLoad();
        }
    }

    public void setLoadComplete() {
        isLoadingComplete = true;
    }

    public interface OnLoadingListener {
        void onLoad();
    }
}
