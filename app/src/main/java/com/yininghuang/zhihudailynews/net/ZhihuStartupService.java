package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuStartup;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Yining Huang on 2017/5/2.
 */

public interface ZhihuStartupService {

    @GET("prefetch-launch-images/1080*1920")
    Observable<ZhihuStartup> getStartup();
}
