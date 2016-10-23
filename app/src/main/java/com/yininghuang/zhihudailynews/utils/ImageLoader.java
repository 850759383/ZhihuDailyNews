package com.yininghuang.zhihudailynews.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class ImageLoader {

    private static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };

    public static void load(Context context, ImageView view, String url) {
        getDrawableTypeRequest(context, url).load(url)
                .crossFade()
                .centerCrop()
                .into(view);
    }

    public static void load(Context context, ImageView view, String url, RequestListener listener) {
        getDrawableTypeRequest(context, url).crossFade()
                .centerCrop()
                .listener(listener)
                .into(view);
    }

    public static void load(Context context, ImageView view, String url, BitmapTransformation transformation, int placeHolder) {
        getDrawableTypeRequest(context, url).placeholder(placeHolder)
                .transform(transformation)
                .crossFade()
                .into(view);
    }

    private static DrawableTypeRequest<String> getDrawableTypeRequest(Context context, String url) {
        if (UserSettingConstants.NO_IMAGE_MODE && !NetworkChecker.isWifiConnected(context))
            return Glide.with(context)
                    .using(cacheOnlyStreamLoader)
                    .load(url);
        else return Glide.with(context)
                .load(url);

    }

}
