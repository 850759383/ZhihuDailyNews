package com.yininghuang.zhihudailynews.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Yining Huang on 2016/11/1.
 */

public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private OnItemSwipeListener mOnItemSwipeListener;

    public MyItemTouchHelper(OnItemSwipeListener listener) {
        mOnItemSwipeListener = listener;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(0, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mOnItemSwipeListener.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public interface OnItemSwipeListener {

        void onItemDismiss(int position);
    }
}
