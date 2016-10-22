package com.yininghuang.zhihudailynews.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Yining Huang on 2016/10/22.
 */

public class ActivityUtils {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name) {
        fragmentManager.beginTransaction()
                .replace(layout, fragment, name)
                .commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name) {
        fragmentManager.beginTransaction()
                .replace(layout, fragment, name)
                .addToBackStack(name)
                .commit();
    }
}
