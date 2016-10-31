package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuTheme;
import com.yininghuang.zhihudailynews.model.ZhihuThemes;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Yining Huang on 2016/10/31.
 */

public interface ZhihuThemeService {

    @GET("themes")
    Observable<ZhihuThemes> getThemes();

    @GET("theme/{id}")
    Observable<ZhihuTheme> getTheme(@Path("id") int id);

    @GET("theme/{themeId}/before/{userId}")
    Observable<ZhihuTheme> getHistoryThemeStory(@Path("themeId") int themeId, @Path("userId") int userId);
}
