package com.dakakolp.lyricsapp.asynctasks;

import android.os.AsyncTask;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;

public abstract class BaseAsyncTask<T> extends AsyncTask<String, Void, TaskRequest<T>> {

    private TaskListener<T> mListener;

    BaseAsyncTask(TaskListener<T> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.initProgressBar();
    }

    @Override
    protected void onPostExecute(TaskRequest<T> t) {
        if (mListener != null)
            mListener.getFinalResult(t);
    }
}
