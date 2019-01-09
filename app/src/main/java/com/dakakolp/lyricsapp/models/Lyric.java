package com.dakakolp.lyricsapp.models;

public class Lyric {
    private String mTitle;
    private String mLink;

    public Lyric(String title, String link) {
        mTitle = title;
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }
}
