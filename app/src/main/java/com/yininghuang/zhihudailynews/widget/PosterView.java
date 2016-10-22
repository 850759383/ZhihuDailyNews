package com.yininghuang.zhihudailynews.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.utils.DisplayUtils;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class PosterView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private int mSelectIndex = 0;
    private List<ZhihuLatestNews.ZhihuTopStory> mData;
    private PosterAdapter mPosterAdapter;
    private Subscription mTimer;

    private static final long AUTO_SELECT_INTERVAL = 4000;

    @BindView(R.id.indicatorLayout)
    LinearLayout mIndicatorLayout;

    @BindView(R.id.posterPager)
    ViewPager viewPager;

    public PosterView(Context context) {
        this(context, null);
    }

    public PosterView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PosterView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.poster_layout, this, true);
        ButterKnife.bind(this);
    }

    private Subscription startTimer() {
        return Observable.interval(AUTO_SELECT_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mData == null)
                            return;
                        if (mSelectIndex < mData.size() - 1)
                            mSelectIndex++;
                        else
                            mSelectIndex = 0;
                        viewPager.setCurrentItem(mSelectIndex, true);
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (mTimer != null)
                    mTimer.unsubscribe();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mTimer = startTimer();
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTimer = startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null)
            mTimer.unsubscribe();
    }

    public PosterAdapter getAdapter() {
        return mPosterAdapter;
    }

    public void initPosters(List<ZhihuLatestNews.ZhihuTopStory> data) {
        mData = data;
        mPosterAdapter = new PosterAdapter(mData);
        viewPager.setAdapter(mPosterAdapter);
        viewPager.addOnPageChangeListener(this);
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            ImageView point = new ImageView(getContext());
            point.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LinearLayout.LayoutParams lp = new LinearLayout
                    .LayoutParams(DisplayUtils.dp2px(getContext(), 8), DisplayUtils.dp2px(getContext(), 8));
            lp.leftMargin = DisplayUtils.dp2px(getContext(), 4);
            point.setLayoutParams(lp);
            if (i == mSelectIndex)
                point.setImageResource(R.drawable.poster_selected_point);
            else
                point.setImageResource(R.drawable.poster_un_selected_point);
            mIndicatorLayout.addView(point);
        }
    }

    private void setSelectIndex(int index) {
        for (int i = 0; i < mIndicatorLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) mIndicatorLayout.getChildAt(i);
            if (i == index)
                imageView.setImageResource(R.drawable.poster_selected_point);
            else
                imageView.setImageResource(R.drawable.poster_un_selected_point);
        }
        mSelectIndex = index;
    }

    public void setOnPosterClickListener(OnPosterClickListener listener) {
        mPosterAdapter.setOnPosterClickListener(listener);
    }

    @Override
    public void onPageSelected(int position) {
        setSelectIndex(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnPosterClickListener {
        void onPosterClick(ZhihuLatestNews.ZhihuTopStory story);
    }

    public static class PosterAdapter extends PagerAdapter {

        private List<ZhihuLatestNews.ZhihuTopStory> mData;
        private OnPosterClickListener mListener;


        public PosterAdapter(List<ZhihuLatestNews.ZhihuTopStory> data) {
            mData = data;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ZhihuLatestNews.ZhihuTopStory story = mData.get(position);
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_view_pager_poster_content, null);
            ImageView poster = ButterKnife.findById(view, R.id.pagerImage);
            FrameLayout posterLayout = ButterKnife.findById(view, R.id.imageLayout);
            TextView title = ButterKnife.findById(view, R.id.title);
            title.setText(story.getTitle());
            container.addView(view);
            ImageLoader.load(container.getContext(), poster, story.getImage());
            posterLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onPosterClick(story);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setOnPosterClickListener(OnPosterClickListener listener) {
            mListener = listener;
        }
    }
}
