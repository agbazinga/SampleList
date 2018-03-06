package com.example.bazinga.samplelist.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.ContextMenu;
import android.view.View;

import com.example.bazinga.samplelist.R;

/**
 * Created by Bazinga on 5/27/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_CHANGE_THEME = "key_change_theme";

    private SwitchPreference mChangeThemeSwitch;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main_settings);

        mChangeThemeSwitch = (SwitchPreference) findPreference(KEY_CHANGE_THEME);

        mChangeThemeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isDarkTheme = (boolean) newValue;
                if (isDarkTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                getActivity().recreate();
                return true;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
