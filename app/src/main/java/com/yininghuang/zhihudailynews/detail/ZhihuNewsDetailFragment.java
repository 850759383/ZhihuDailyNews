package com.yininghuang.zhihudailynews.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuNewsDetailFragment extends BaseFragment implements ZhihuNewsDetailContract.View {

    private int mDetailId = -1;

    private ZhihuNewsDetailContract.Presenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapseLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.appbarImage)
    ImageView mAppbarImage;

    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailId = getArguments().getInt("id", mDetailId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zhihu_news_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        ((ZhihuNewsDetailActivity) getActivity()).setSupportActionBar(mToolbar);
        ((ZhihuNewsDetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mPresenter.init(mDetailId);
    }

    public static ZhihuNewsDetailFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        ZhihuNewsDetailFragment fragment = new ZhihuNewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showBody(String body) {
        mWebView.loadDataWithBaseURL("file:///android_asset/", body, "text/html", "utf-8", null);
    }

    @Override
    public void showEmptyBody(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void showAppBarImage(String url) {
        ImageLoader.load(getActivity(), mAppbarImage, url);
    }

    @Override
    public void setTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void showLoadError() {
        Snackbar.make(getView(), R.string.load_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.reload(mDetailId);
                    }
                }).show();
    }

    @Override
    public void setPresenter(ZhihuNewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
