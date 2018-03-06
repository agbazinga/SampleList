package com.example.bazinga.samplelist.application;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.example.bazinga.samplelist.ui.fragment.SettingsFragment;

/**
 * Created by Bazinga on 7/23/2017.
 */

public class SampleApplication extends Application {
    Thread.UncaughtExceptionHandler defaultHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        boolean isDarkMode = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_CHANGE_THEME, false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // Log.d("ABHI", "application onCreate");
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                handleUncaughtException(t, e);
            }
        });
    }


    private void handleUncaughtException(Thread t, Throwable e) {

        Log.d("ABHI", "uncaught exception");

        e.printStackTrace();
        defaultHandler.uncaughtException(t, e);
    }


}
