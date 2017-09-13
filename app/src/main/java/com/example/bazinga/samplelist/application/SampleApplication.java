package com.example.bazinga.samplelist.application;

import android.app.Application;
import android.util.Log;

/**
 * Created by Bazinga on 7/23/2017.
 */

public class SampleApplication extends Application {
    Thread.UncaughtExceptionHandler defaultHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("ABHI", "application onCreate");
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
