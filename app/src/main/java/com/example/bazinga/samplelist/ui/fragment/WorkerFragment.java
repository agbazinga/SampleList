package com.example.bazinga.samplelist.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bazinga.samplelist.callbacks.TaskStatusCallback;

/**
 * Created by Bazinga on 3/8/2018.
 */

public class WorkerFragment extends Fragment {

    private TaskStatusCallback mStatusCallback;
    private BackgroundTask mBackgroundTask;

    private boolean isTaskExecuting = false;

    public static final String FRAGMENT_TAG = "worker_fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStatusCallback = (TaskStatusCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStatusCallback = null;
    }

    public void startBackgroundTask() {
        if (!isTaskExecuting) {
            mBackgroundTask = new BackgroundTask();
            mBackgroundTask.execute();
            isTaskExecuting = true;
        }
    }

    public void cancelBackgroundTask() {
        if (isTaskExecuting) {
            mBackgroundTask.cancel(true);
            mStatusCallback.onProgressUpdate(0);
            isTaskExecuting = false;
        }
    }

    public void updateExecutingStatus(boolean isExecuting) {
        this.isTaskExecuting = isExecuting;
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (null != mStatusCallback)
                mStatusCallback.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int progress = 0;
            while (progress < 100 && !isCancelled()) {
                progress++;
                publishProgress(progress);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (null != mStatusCallback)
                mStatusCallback.onProgressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (null != mStatusCallback)
                mStatusCallback.onPostExecute();
            isTaskExecuting = false;
        }
    }

}
