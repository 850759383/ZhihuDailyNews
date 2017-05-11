package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuTheme;
import com.yininghuang.zhihudailynews.model.ZhihuThemes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Yining Huang on 2017/5/10.
 */
public class ZhihuThemeServiceTest {

    private ZhihuThemeService mService;

    @Before
    public void setUp() throws Exception {
        mService = RetrofitHelper.getInstance().createRetrofit(ZhihuThemeService.class, Api.ZHIHU_BASE_URL);
    }

    @Test
    public void getThemes() throws Exception {
        ZhihuThemes themes = mService.getThemes().toBlocking().first();
        assertNotNull(themes);
    }

    @Test
    public void getTheme() throws Exception {
        ZhihuTheme theme = mService.getTheme(11).toBlocking().first();
        assertNotNull(theme);
    }

    @Test
    public void getHistoryThemeStory() throws Exception {
        ZhihuTheme theme = mService.getHistoryThemeStory(11, 1).toBlocking().first();
        assertNotNull(theme);
    }

}