package com.yininghuang.zhihudailynews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class LicenseFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_license, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        mWebView.loadUrl();
    }
}
