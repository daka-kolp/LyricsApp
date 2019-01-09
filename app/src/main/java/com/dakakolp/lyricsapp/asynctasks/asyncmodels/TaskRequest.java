package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

public class TaskRequest<R extends TaskResult> {

    private String mError;
    private R mResult;

    public TaskRequest(String error) {
        mError = error;
    }

    public TaskRequest(R result) {
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
