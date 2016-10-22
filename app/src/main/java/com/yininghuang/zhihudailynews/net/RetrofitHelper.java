package com.yininghuang.zhihudailynews.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class RetrofitHelper {

    private static RetrofitHelper INSTANCE;

    private RetrofitHelper(){

    }

    public static RetrofitHelper getInstance(){
        if (INSTANCE == null)
            INSTANCE = new RetrofitHelper();
        return INSTANCE;
    }

    public <T> T createRetrofit(Class<T> retrofitInterface, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createClient())
                .build().create(retrofitInterface);
    }

    public OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .build();
    }
}