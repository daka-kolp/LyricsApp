package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

public class TaskRequest<R> {

    private String mError;
    private R mResult;

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public R getResult() {
        return mResult;
    }

    public void setResult(R result) {
        mResult = result;
    }
}
