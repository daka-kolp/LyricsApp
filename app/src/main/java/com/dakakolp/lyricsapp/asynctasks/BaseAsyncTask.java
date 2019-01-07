package com.dakakolp.lyricsapp.asynctasks;

import android.os.AsyncTask;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.ParseListener;

public abstract class BaseAsyncTask<T> extends AsyncTask<String, Void, T> {

    private ParseListener<T> mListener;

    protected BaseAsyncTask(ParseListener<T> listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.initProgressBar();
    }

    @Override
    protected void onPostExecute(T t) {
        if (mListener != null)
            mListener.getFinalResult(t);
    }
}
