package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuImage;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public interface ZhihuDailyService {

    @GET("news/latest")
    Observable<ZhihuLatestNews> getLatestNews();

    @GET("news/{id}")
    Observable<ZhihuNewsContent> getNewsContent(@Path("id") int id);

    @GET("start-image/1080*1920")
    Observable<ZhihuImage> getStartupImage();

    @GET("news/before/{date}")
    Observable<ZhihuLatestNews> getHistoryNews(@Path("date") String before);
}
