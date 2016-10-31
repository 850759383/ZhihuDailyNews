package com.yininghuang.zhihudailynews.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yininghuang.zhihudailynews.R;

/**
 * Created by Yining Huang on 2016/10/22.
 */

public class ActivityUtils {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name) {
        replaceFragment(fragmentManager, fragment, layout, name, true);
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name) {
        addFragment(fragmentManager, fragment, layout, name, true);
    }

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name, boolean animate) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out);
        }
        transaction.replace(layout, fragment, name)
                .commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int layout, String name, boolean animate) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out);
        }
        transaction.replace(layout, fragment, name)
                .addToBackStack(name)
                .commit();
    }
}
