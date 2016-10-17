package com.yininghuang.zhihudailynews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Yining Huang on 2016/10/17.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(savedInstanceState);
    }

    public abstract void initViews(@Nullable Bundle savedInstanceState);
}
