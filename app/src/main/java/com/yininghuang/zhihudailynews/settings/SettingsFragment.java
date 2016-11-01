package com.yininghuang.zhihudailynews.settings;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Yining Huang on 2016/10/21.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private SubscriptionList mSubscriptions = new SubscriptionList();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(UserSettingConstants.PREFERENCE__USER_SETTINGS);
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

        findPreference("dark_mode").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                UserSettingConstants.DARK_MODE = (boolean) newValue;
                ((BaseActivity) getActivity()).onThemeChange();
                return true;
            }
        });

        findPreference("skip_splash").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                UserSettingConstants.SKIP_SPLASH = (boolean) newValue;
                return true;
            }
        });

        findPreference("no_image").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                UserSettingConstants.NO_IMAGE_MODE = (boolean) newValue;
                return true;
            }
        });

        findPreference("use_webview").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                UserSettingConstants.USE_WEBVIEW = (boolean) newValue;
                return true;
            }
        });

        final Preference clearCache = findPreference("clear_cache");
        clearCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Subscription sb = Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(deleteDir(getActivity().getCacheDir()));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (getView() != null)
                                    Snackbar.make(getView(), R.string.clear_success, Snackbar.LENGTH_SHORT)
                                            .show();
                                clearCache.setSummary("0kb");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                mSubscriptions.add(sb);
                return true;
            }
        });

        Subscription sb = Observable.just(folderSize(getActivity().getCacheDir()))
                .map(new Func1<Long, Float>() {
                    @Override
                    public Float call(Long bt) {
                        return bt / 1024f;
                    }
                })
                .map(new Func1<Float, String>() {
                    @Override
                    public String call(Float kb) {
                        if (kb < 1024) {
                            return Math.round(kb * 100) / 100.0 + "KB";
                        }
                        return Math.round(kb / 1024 * 100) / 100.0 + "MB";
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        clearCache.setSummary(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mSubscriptions.add(sb);
    }

    private long folderSize(File directory) {
        long length = 0;
        if (!directory.exists())
            return length;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
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
        mSubscriptions.unsubscribe();
        super.onDestroyView();
    }
}
