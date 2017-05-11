package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuComments;
import com.yininghuang.zhihudailynews.model.ZhihuNewsExtra;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Yining Huang on 2017/5/10.
 */
public class ZhihuCommentServiceTest {

    private ZhihuCommentService mService;

    @Before
    public void setUp() throws Exception {
        mService = RetrofitHelper.getInstance().createRetrofit(ZhihuCommentService.class, Api.ZHIHU_BASE_URL);
    }

    @Test
    public void getNewsExtra() throws Exception {
        ZhihuNewsExtra newsExtra = mService.getNewsExtra(3930445).toBlocking().first();
        assertNotNull(newsExtra);
    }

    @Test
    public void getComments() throws Exception {
        ZhihuComments comments = mService.getComments(3930445).toBlocking().first();
        assertNotNull(comments);
    }

    @Test
    public void getHistoryComments() throws Exception {
        ZhihuComments comments = mService.getHistoryComments(3930445, '1').toBlocking().first();
        assertNotNull(comments);

    }

}