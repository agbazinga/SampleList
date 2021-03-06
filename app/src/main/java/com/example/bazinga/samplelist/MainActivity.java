package com.example.bazinga.samplelist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bazinga.samplelist.ui.activity.AppListActivity;
import com.example.bazinga.samplelist.ui.activity.AsyncRetainActivity;
import com.example.bazinga.samplelist.ui.activity.BaseAppCompatActivity;
import com.example.bazinga.samplelist.ui.activity.OtpDetectorActivity;
import com.example.bazinga.samplelist.ui.activity.SettingsActivity;
import com.example.bazinga.samplelist.utils.AppUtils;

import java.util.List;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TESLA";

    private static final String KEY_SCREEN_ORIENTATION = "key_screen_orientation";
    private static final String ACTION_CUSTOM_DOWNLOAD = "com.example.bazinga.intent.ACTION_CUSTOM_DOWNLOAD";

    private Button clickButton;
    private Spinner mSpinner;
    private CustomBroadcastReceiver mCustomBroadcastReceiver;
    private MediaSession mediaSession;
    private PackageManager packageManager;
    private ViewType currentViewType = ViewType.UNKNOWN;

    private int mRotateState;

    private enum ViewType {
        UNKNOWN,
        DOWNLOAD_SHOW_CARD,
        DOWNLOAD_PROGRESS,
        DOWNLOAD_COMPLETE
    }

    private String[] launchMode = {"Standard", "Single Top", "Single Instance", "Single Task"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ABHI", "MainActivity OnCreate ");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCustomBroadcastReceiver = new CustomBroadcastReceiver();
        registerCustomBroadcastReceiver();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_email);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                //  Settings.System.putInt(getContentResolver(), Settings.System.VIBRATE_WHEN_RINGING, 0);
                //  Intent intent = new Intent(MainActivity.this, AnimationTestActivity.class);
                // startActivity(intent);

                setViewType(ViewType.DOWNLOAD_SHOW_CARD);
            }
        });

        packageManager = getPackageManager();

        clickButton = (Button) findViewById(R.id.sample_button);
        clickButton.setOnClickListener(this);

        setMediaSession();
        playSilentAudio();
        Log.d(TAG, "Default Rotate State : " + mRotateState);

        if (savedInstanceState != null) {
            mRotateState = savedInstanceState.getInt(KEY_SCREEN_ORIENTATION);
            Log.d(TAG, "OnCreate savedInstanceState Rotate State : " + mRotateState);

            int currentOrientation = getScreenOrientation();

            Log.d(TAG, "current screen orientation : " + currentOrientation);

            if (mRotateState != currentOrientation) {
                mRotateState = currentOrientation;
                Toast.makeText(this, "Orientation Change Detected ", Toast.LENGTH_SHORT).show();
            }
        }

        mSpinner = (Spinner) findViewById(R.id.launch_mode_spinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, launchMode);
        mSpinner.setAdapter(spinnerAdapter);
    }

    private void registerCustomBroadcastReceiver() {
        if (null != mCustomBroadcastReceiver) {
            mCustomBroadcastReceiver.register();
        }
    }

    private void unRegisterCustomBroadcastReceiver() {
        if (null != mCustomBroadcastReceiver) {
            mCustomBroadcastReceiver.unRegister();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sample_button:
                //Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
                launchAppListActivity();
                //setViewType(ViewType.DOWNLOAD_COMPLETE);
                //AppUtils.launchLaunchMode(this, mSpinner.getSelectedItemPosition() + 1);
                break;
        }
    }

    private void launchAppListActivity() {
        Intent launchIntent = new Intent(this, AppListActivity.class);
        startActivityForResult(launchIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
    }

    @Override
    protected void  onResume() {
        super.onResume();
        mediaSession.setActive(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaSession.setActive(false);
        Log.d(TAG, "onPause");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unRegisterCustomBroadcastReceiver();
        release();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState : mRotateState : " + mRotateState);
        outState.putInt(KEY_SCREEN_ORIENTATION, mRotateState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onSaveInstanceState");
        super.onConfigurationChanged(newConfig);
    }

    private void setMediaSession() {
        mediaSession = new MediaSession(this, "SampleList");
        mediaSession.setActive(true);
        mediaSession.setCallback(new MediaSessionCallback());
        PlaybackState.Builder mMediaSessionBuilder = new PlaybackState.Builder();
        mMediaSessionBuilder.setState(PlaybackState.STATE_PLAYING, 0, 0);
        mediaSession.setPlaybackState(mMediaSessionBuilder.build());

    }

    private void release() {
        if (mediaSession != null) {
            mediaSession.release();
        }
    }

    private void playSilentAudio() {
        final MediaPlayer mMediaPlayer;
        mMediaPlayer = MediaPlayer.create(this, R.raw.blan_audio_250ms);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mMediaPlayer.release();
            }
        });
        mMediaPlayer.start();
    }

    private class MediaSessionCallback extends MediaSession.Callback {
        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            Log.d(TAG, "MediaSessionCallback");
            //Toast.makeText(mContext,"MediaSessionCallback",Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private class CustomBroadcastReceiver extends BroadcastReceiver {

        IntentFilter intentFilter;
        boolean isRegistered;

        private void register() {
            if (!isRegistered) {
                intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION_CUSTOM_DOWNLOAD);
                registerReceiver(this, intentFilter);
                isRegistered = true;
            }
        }

        private void unRegister() {
            if (isRegistered) {
                unregisterReceiver(this);
                isRegistered = false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Intent i = new Intent();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setPackage("com.samsung.android.contacts");

            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(i, 0);

            for (ResolveInfo ri : resolveInfos) {
                Log.d("BUXX", "Activity Name : " + ri.activityInfo.name);
            }


            if (action.equals(ACTION_CUSTOM_DOWNLOAD)) {
                currentViewType.name();
                Log.d("BUXX", "currentViewType : " + currentViewType
                        + " : currentType.name() : " + currentViewType.name()
                        + " : currentType.ordinal() : " + currentViewType.ordinal());
            }
        }
    }

    private ViewType getViewType() {
        return currentViewType;
    }

    private void setViewType(ViewType newViewType) {
        currentViewType = newViewType;
    }
}

