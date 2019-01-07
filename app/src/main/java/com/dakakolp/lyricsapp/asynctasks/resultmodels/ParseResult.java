package com.dakakolp.lyricsapp.asynctasks.resultmodels;

public class ParseResult<T> {

    private String mError;
    private T mResult;

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public T getResult() {
        return mResult;
    }

    public void setResult(T result) {
        mResult = result;
    }
}
