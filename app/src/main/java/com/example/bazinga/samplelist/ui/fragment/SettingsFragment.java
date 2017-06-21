package com.example.bazinga.samplelist.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.View;

import com.example.bazinga.samplelist.R;

/**
 * Created by Bazinga on 5/27/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main_settings);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
