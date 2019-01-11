package com.dakakolp.lyricsapp.asynctasks;

import android.os.AsyncTask;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public abstract class BaseAsyncTask<R extends TaskResult> extends AsyncTask<String, Void, TaskRequest<R>> {

    private TaskListener<R> mListener;

    BaseAsyncTask(TaskListener<R> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.showProgress();
    }

    @Override
    protected void onPostExecute(TaskRequest<R> t) {
        if (mListener != null) {
            mListener.hideProgress();
            mListener.onFinalResult(t);
        }
    }
}
