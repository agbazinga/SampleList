package com.example.bazinga.samplelist.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.utils.AppUtils;
import com.example.bazinga.samplelist.utils.Constants;

/**
 * Created by Bazinga on 3/11/2018.
 */

public class BaseLaunchModeActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private Button standardModeBtn;
    private Button singleTopModeBtn;
    private Button singleInstanceModeBtn;
    private Button singleTaskModeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        standardModeBtn = (Button) findViewById(R.id.launch_mode_standard_btn);
        singleTopModeBtn = (Button) findViewById(R.id.launch_mode_single_top_btn);
        singleInstanceModeBtn = (Button) findViewById(R.id.launch_mode_single_instance_btn);
        singleTaskModeBtn = (Button) findViewById(R.id.launch_mode_single_task_btn);


        standardModeBtn.setOnClickListener(this);
        singleTopModeBtn.setOnClickListener(this);
        singleInstanceModeBtn.setOnClickListener(this);
        singleTaskModeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.launch_mode_standard_btn:
                AppUtils.launchLaunchMode(this, Constants.LAUNCH_MODE_STANDARD);
                break;
            case R.id.launch_mode_single_top_btn:
                AppUtils.launchLaunchMode(this, Constants.LAUNCH_MODE_SINGLE_TOP);
                break;
            case R.id.launch_mode_single_instance_btn:
                AppUtils.launchLaunchMode(this, Constants.LAUNCH_MODE_SINGLE_INSTANCE);
                break;
            case R.id.launch_mode_single_task_btn:
                AppUtils.launchLaunchMode(this, Constants.LAUNCH_MODE_SINGLE_TASK);
                break;
        }
    }
}
