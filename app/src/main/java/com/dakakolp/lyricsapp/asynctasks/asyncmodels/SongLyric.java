package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

public class SongLyric implements TaskResult {

    private String mText;

//    public SongLyric() {
//    }

    public SongLyric(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
