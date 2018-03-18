package com.example.bazinga.samplelist.callbacks;

/**
 * Created by Bazinga on 3/8/2018.
 */

public interface TaskStatusCallback {
    void onPreExecute();
    void onPostExecute();
    void onProgressUpdate(int progress);
    void onCancelled();
}
