package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

public class TaskResult<R> {

    private String mError;
    private R mResult;

    public TaskResult(String error) {
        mError = error;
    }

    public TaskResult(R result) {
        mResult = result;
    }

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
