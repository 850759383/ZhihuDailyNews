package com.yininghuang.zhihudailynews.utils;

import android.content.Context;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class DisplayUtils {

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
