package com.example.bazinga.samplelist.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.callbacks.TaskStatusCallback;
import com.example.bazinga.samplelist.ui.fragment.WorkerFragment;

public class AsyncRetainActivity extends BaseAppCompatActivity implements View.OnClickListener, TaskStatusCallback {

    private Button mStartTaskBtn;
    private Button mStopTaskBtn;
    private TextView mProgressTextView;
    private ProgressBar mTaskProgressBar;

    private WorkerFragment mWorkerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_retain);
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        mWorkerFragment = (WorkerFragment) fragmentManager.findFragmentByTag(WorkerFragment.FRAGMENT_TAG);

        if (null == mWorkerFragment) {
            mWorkerFragment = new WorkerFragment();
            fragmentManager.beginTransaction().add(mWorkerFragment, WorkerFragment.FRAGMENT_TAG).commit();
        }
        mProgressTextView = (TextView) findViewById(R.id.task_progress_value);
        mTaskProgressBar = (ProgressBar) findViewById(R.id.task_progress_bar);
        mStartTaskBtn = (Button) findViewById(R.id.start_task_btn);
        mStopTaskBtn = (Button) findViewById(R.id.stop_task_btn);

        mStartTaskBtn.setOnClickListener(this);
        mStopTaskBtn.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.start_task_btn:
                if (null != mWorkerFragment) {
                    mWorkerFragment.startBackgroundTask();
                }
                break;
            case R.id.stop_task_btn:
                if (null != mWorkerFragment) {
                    mWorkerFragment.cancelBackgroundTask();
                }
                break;
        }
    }

    @Override
    public void onPreExecute() {
        Log.d("TESLA", "onPreExecute");
    }

    @Override
    public void onPostExecute() {
        Log.d("TESLA", "onPostExecute");
    }

    @Override
    public void onProgressUpdate(int progress) {
        mProgressTextView.setText(progress + "%");
        mTaskProgressBar.setProgress(progress);
    }

    @Override
    public void onCancelled() {

    }
}
