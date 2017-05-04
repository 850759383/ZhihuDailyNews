package com.yininghuang.zhihudailynews.settings;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.yininghuang.zhihudailynews.BaseActivity;
import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;
import com.yininghuang.zhihudailynews.utils.CacheManager;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

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

        findPreference("about").setOnPreferenceClickListener(preference -> {
            ActivityUtils.addFragment(
                    getActivity().getSupportFragmentManager(),
                    AboutFragment.newInstance(),
                    R.id.mainFrameLayout,
                    "AboutFragment");
            return true;
        });

        findPreference("dark_mode").setOnPreferenceChangeListener((preference, newValue) -> {
            UserSettingConstants.DARK_THEME = (boolean) newValue;
            ((BaseActivity) getActivity()).onThemeChange();
            return true;
        });

        findPreference("skip_splash").setOnPreferenceChangeListener((preference, newValue) -> {
            UserSettingConstants.SKIP_SPLASH = (boolean) newValue;
            return true;
        });

        findPreference("no_image").setOnPreferenceChangeListener((preference, newValue) -> {
            UserSettingConstants.NO_IMAGE_MODE = (boolean) newValue;
            return true;
        });

        findPreference("use_webview").setOnPreferenceChangeListener((preference, newValue) -> {
            UserSettingConstants.USE_WEBVIEW = (boolean) newValue;
            return true;
        });

        final Preference clearCache = findPreference("clear_cache");
        clearCache.setOnPreferenceClickListener(preference -> {
            Subscription sb = CacheManager.getInstance(getActivity())
                    .clearCache()
                    .subscribe(aBoolean -> {
                        if (getView() != null)
                            Snackbar.make(getView(), R.string.clear_success, Snackbar.LENGTH_SHORT).show();
                        clearCache.setSummary("0B");
                    }, Throwable::printStackTrace);
            mSubscriptions.add(sb);
            return true;
        });

        Subscription sb = CacheManager.getInstance(getActivity())
                .calcCacheSize()
                .subscribe(clearCache::setSummary, Throwable::printStackTrace);
        mSubscriptions.add(sb);
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
