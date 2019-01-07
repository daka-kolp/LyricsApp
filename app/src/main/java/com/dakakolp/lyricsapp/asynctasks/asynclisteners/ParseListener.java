package com.dakakolp.lyricsapp.asynctasks.asynclisteners;

public interface ParseListener<T> {
    void initProgressBar();
    void getFinalResult(T t);
}
