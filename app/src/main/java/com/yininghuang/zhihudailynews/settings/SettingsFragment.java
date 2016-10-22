package com.yininghuang.zhihudailynews.settings;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.yininghuang.zhihudailynews.Constants;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private Subscription mSubscription;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(SettingsActivity.PREFERENCE__USER_SETTINGS);
        addPreferencesFromResource(R.xml.fragment_settings);

        findPreference("about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ActivityUtils.addFragment(
                        getActivity().getSupportFragmentManager(),
                        AboutFragment.newInstance(),
                        R.id.mainFrameLayout,
                        "AboutFragment");
                return true;
            }
        });

        findPreference("no_image").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Constants.NO_IMAGE_MODE = (boolean) newValue;
                return true;
            }
        });

        findPreference("use_webview").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Constants.USE_WEBVIEW = (boolean) newValue;
                return true;
            }
        });

        findPreference("clear_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mSubscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(deleteDir(getContext().getCacheDir()));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (getView() != null)
                                    Snackbar.make(getView(), R.string.clear_success, Snackbar.LENGTH_SHORT)
                                            .show();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                return true;
            }
        });
    }

    private boolean deleteDir(File dir) {
        if (!dir.exists())
            return true;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.settings);
    }

    @Override
    public void onDestroyView() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
        super.onDestroyView();
    }
}
