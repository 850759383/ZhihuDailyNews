package com.yininghuang.zhihudailynews.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.model.ZhihuLatestNews;
import com.yininghuang.zhihudailynews.utils.DisplayUtils;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public class PosterView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private int mSelectIndex = 0;
    private List<ZhihuLatestNews.ZhihuTopStory> mData;
    private PosterAdapter posterAdapter;

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

    public void initViews(List<ZhihuLatestNews.ZhihuTopStory> data) {
        mData = data;
        posterAdapter = new PosterAdapter(mData);
        viewPager.setAdapter(posterAdapter);
        viewPager.addOnPageChangeListener(this);
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            ImageView point = new ImageView(getContext());
            point.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtils.dp2px(getContext(), 8), DisplayUtils.dp2px(getContext(), 8));
            lp.leftMargin = DisplayUtils.dp2px(getContext(), 4);
            point.setLayoutParams(lp);
            if (i == mSelectIndex)
                point.setImageResource(R.drawable.poster_selected_point);
            else
                point.setImageResource(R.drawable.poster_un_selected_point);
            mIndicatorLayout.addView(point);
        }
    }

    public void setSelectIndex(int index) {
        if (mData == null || index > mData.size() - 1)
            return;
        ImageView imageView = (ImageView) mIndicatorLayout.getChildAt(mSelectIndex);
        imageView.setImageResource(R.drawable.poster_un_selected_point);
        imageView = (ImageView) mIndicatorLayout.getChildAt(index);
        imageView.setImageResource(R.drawable.poster_selected_point);
        mSelectIndex = index;
    }

    public void setOnPosterClickListener(OnPosterClickListener listener) {
        posterAdapter.setOnPosterClickListener(listener);
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

    interface OnPosterClickListener {
        void onPosterClick(ZhihuLatestNews.ZhihuTopStory story);
    }

    public class PosterAdapter extends PagerAdapter {

        private List<ZhihuLatestNews.ZhihuTopStory> mData;
        private OnPosterClickListener mListener;

        public PosterAdapter(List<ZhihuLatestNews.ZhihuTopStory> data) {
            mData = data;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ZhihuLatestNews.ZhihuTopStory story = mData.get(position);
            ImageView imageView = new ImageView(container.getContext());
            container.addView(imageView);
            ImageLoader.load(container.getContext(), imageView, story.getImage());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onPosterClick(story);
                }
            });
            return imageView;
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
