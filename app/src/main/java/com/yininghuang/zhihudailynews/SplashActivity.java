package com.yininghuang.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yininghuang.zhihudailynews.home.MainActivity;
import com.yininghuang.zhihudailynews.model.ZhihuImage;
import com.yininghuang.zhihudailynews.net.Api;
import com.yininghuang.zhihudailynews.net.RetrofitHelper;
import com.yininghuang.zhihudailynews.net.ZhihuDailyService;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.startupImage)
    ImageView mStartupImage;

    @BindView(R.id.describe)
    TextView mImageDescribe;

    private SubscriptionList subscriptions = new SubscriptionList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserSettingConstants.SKIP_SPLASH)
            toMainActivity(0);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Subscription sb = RetrofitHelper.getInstance()
                .createRetrofit(ZhihuDailyService.class, Api.ZHIHU_BASE_URL)
                .getStartupImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZhihuImage>() {
                    @Override
                    public void call(ZhihuImage zhihuImage) {
                        loadSplashImage(zhihuImage.getImg());
                        mImageDescribe.setText(zhihuImage.getText());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        toMainActivity(0);
                    }
                });
        subscriptions.add(sb);
    }

    private void toMainActivity(int time) {
        Subscription sb = Observable.timer(time, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        subscriptions.add(sb);
    }

    private void loadSplashImage(String url) {
        ImageLoader.load(SplashActivity.this, mStartupImage, url, new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                toMainActivity(0);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                toMainActivity(2);
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
