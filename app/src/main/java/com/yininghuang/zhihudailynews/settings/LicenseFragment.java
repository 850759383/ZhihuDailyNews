package com.yininghuang.zhihudailynews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yininghuang.zhihudailynews.BaseFragment;
import com.yininghuang.zhihudailynews.R;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class LicenseFragment extends BaseFragment {

    private static final String LICENSE_URL = "file:///android_asset/license.html";

    private WebView mWebView;

    public static LicenseFragment newInstance() {
        return new LicenseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_license, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.webView);
        mWebView.loadUrl(LICENSE_URL);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.license);
    }
}
