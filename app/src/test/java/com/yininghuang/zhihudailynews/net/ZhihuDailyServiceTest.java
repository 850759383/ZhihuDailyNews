package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.model.ZhihuNewsContent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Yining Huang on 2017/5/10.
 */
public class ZhihuDailyServiceTest {

    private ZhihuDailyService mService;

    @Before
    public void setUp() throws Exception {
        mService = RetrofitHelper.getInstance().createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL);
    }

    @Test
    public void getLatestNews() throws Exception {
        ZhihuLatestNews news = mService.getLatestNews().toBlocking().first();
        assertNotNull(news);
    }

    @Test
    public void getNewsContent() throws Exception {
        ZhihuNewsContent content = mService.getNewsContent(3930445).toBlocking().first();
        assertNotNull(content);
    }

    @Test
    public void getHistoryNews() throws Exception {
        ZhihuLatestNews news = mService.getHistoryNews("20140523").toBlocking().first();
        assertNotNull(news);
    }

}