package com.yininghuang.zhihudailynews.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.yininghuang.zhihudailynews.R;
import com.yininghuang.zhihudailynews.utils.ActivityUtils;

/**
 * Created by Yining Huang on 2016/10/22.
 */

public class AboutFragment extends PreferenceFragmentCompat {

    public static final String ZHIHU_USER_HOME_URL = "https://www.zhihu.com/people/undefeated";

    public static final String GIT_HUB_REPO_URL = "https://github.com/850759383/ZhihuDailyNews";

    public static final String E_MAIL = "iamyining@yahoo.com";


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_about);

        findPreference("license").setOnPreferenceClickListener(preference -> {
            ActivityUtils.addFragment(
                    getActivity().getSupportFragmentManager(),
                    LicenseFragment.newInstance(),
                    R.id.mainFrameLayout,
                    "LicenseFragment");
            return true;
        });

        findPreference("repo_url").setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse(GIT_HUB_REPO_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        });

        findPreference("zhihu_url").setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse(ZHIHU_USER_HOME_URL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        });

        findPreference("mail").setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{E_MAIL});
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        });

        try {
            PackageManager pm = getActivity().getPackageManager();
            String name = pm.getPackageInfo(getActivity().getPackageName(), 0).versionName;
            findPreference("version").setSummary(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        findPreference("disclaimer").setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.disclaimer)
                    .setMessage(R.string.disclaimer_content)
                    .setPositiveButton(R.string.confirm, null)
                    .create()
                    .show();
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.about);
    }
}
