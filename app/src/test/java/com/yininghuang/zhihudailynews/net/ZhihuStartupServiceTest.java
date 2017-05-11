package com.yininghuang.zhihudailynews.net;

import com.yininghuang.zhihudailynews.model.ZhihuStartup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Yining Huang on 2017/5/10.
 */
public class ZhihuStartupServiceTest {

    private ZhihuStartupService mService;

    @Before
    public void setUp() throws Exception {
        mService = RetrofitHelper.getInstance().createRetrofit(ZhihuStartupService.class,Api.ZHIHU_START_UP);
    }

    @Test
    public void getStartup() throws Exception {
        ZhihuStartup startup = mService.getStartup().toBlocking().first();
        assertNotNull(startup);
    }

}