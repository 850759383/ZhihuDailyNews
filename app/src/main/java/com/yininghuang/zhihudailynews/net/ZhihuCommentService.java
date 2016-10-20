package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuComments;
import com.yininghuang.zhihudailynews.model.ZhihuNewsExtra;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Yining Huang on 2016/10/20.
 */

public interface ZhihuCommentService {

    @GET("story-extra/{id}")
    Observable<ZhihuNewsExtra> getNewsExtra(@Path("id") int id);

    @GET("story/{id}/comments")
    Observable<ZhihuComments> getComments(@Path("id") int id);

    @GET("story/{newsId}/comments/before/{userId}")
    Observable<ZhihuComments> getHistoryComments(@Path("newsId") int newsId, @Path("userId") int userId);
}
