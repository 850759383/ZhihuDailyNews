package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ImageLoader {

    public static void load(Context context, ImageView view, String url){
        Glide.with(context)
                .load(url)
                .crossFade()
                .centerCrop()
                .into(view);
    }

    public static void load(Context context, ImageView view, String url, RequestListener listener){
        Glide.with(context)
                .load(url)
                .crossFade()
                .centerCrop()
                .listener(listener)
                .into(view);
    }


}
