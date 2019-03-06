package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yininghuang.zhihudailynews.R;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private GradientDrawable mDivider;

    public ItemDecoration(Context context, int color) {
        mDivider = (GradientDrawable) context.getResources().getDrawable(R.drawable.divider);
        mDivider.setColor(color);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
