package com.yininghuang.zhihudailynews.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
 * Created by Yining Huang on 2016/10/22.
 */

public class AboutFragment extends PreferenceFragmentCompat {



    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_about);

        findPreference("license").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ActivityUtils.addFragment(
                        getActivity().getSupportFragmentManager(),
                        LicenseFragment.newInstance(),
                        R.id.mainFrameLayout,
                        "LicenseFragment");
                return true;
            }
        });

        findPreference("repo_url").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse(Constants.GITHUB_REPO_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });

        findPreference("zhihu_url").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse(Constants.ZHIHU_USER_HOME_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });

        findPreference("mail").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Constants.E_MAIL});
                startActivity(intent);
                return false;
            }
        });

        try {
            PackageManager pm = getActivity().getPackageManager();
            String name = pm.getPackageInfo(getActivity().getPackageName(), 0).versionName;
            findPreference("version").setSummary(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.about);
    }
}
