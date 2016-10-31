package com.yininghuang.zhihudailynews.detail;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.browser.WebViewActivity;
import com.yininghuang.zhihudailynews.comment.ZhihuCommentActivity;
import com.yininghuang.zhihudailynews.settings.UserSettingConstants;
import com.yininghuang.zhihudailynews.utils.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/18.
 */

public class ZhihuNewsDetailFragment extends BaseFragment implements ZhihuNewsDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbarImage)
    ImageView mAppbarImage;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.imageSource)
    TextView mImageSource;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    private int mDetailId = -1;
    private ZhihuNewsDetailContract.Presenter mPresenter;
    private View mRootView;

    public static ZhihuNewsDetailFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        ZhihuNewsDetailFragment fragment = new ZhihuNewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailId = getArguments().getInt("id", mDetailId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_zhihu_news_detail, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void initViews(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((ZhihuNewsDetailActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((ZhihuNewsDetailActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (UserSettingConstants.USE_WEBVIEW) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                    return true;
                }
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
        mPresenter.init(mDetailId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_zhihu_daily_content, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.share: {
                mPresenter.share();
                return true;
            }
            case R.id.comment: {
                Intent intent = new Intent(getActivity(), ZhihuCommentActivity.class);
                intent.putExtra("id", mDetailId);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBody(String body) {
        mWebView.loadDataWithBaseURL("file:///android_asset/", body, "text/html", "utf-8", null);
    }

    @Override
    public void showEmptyBody(String url) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void startShareChooser(Intent intent) {
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public void showAppBarImage(String url) {
        ImageLoader.load(getActivity(), mAppbarImage, url);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setImageSource(String source) {
        mImageSource.setText(source);
    }

    @Override
    public void setBlockImageDisplay(Boolean status) {
        mWebView.getSettings().setBlockNetworkImage(status);
    }

    @Override
    public void showLoadError() {
        Snackbar.make(mRootView, R.string.load_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.reload();
                    }
                }).show();
    }

    @Override
    public void setPresenter(ZhihuNewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
