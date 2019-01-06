package com.dakakolp.lyricsapp.models;

public class Song {

    private String mSinger;
    private String mSongTitle;
    private String mLink;

    public String getSinger() {
        return mSinger;
    }

    public void setSinger(String singer) {
        mSinger = singer;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @Override
    public String toString() {
        return "Song{" +
                "mSinger='" + mSinger + '\'' +
                ", mSongTitle='" + mSongTitle + '\'' +
                ", mLink='" + mLink + '\'' +
                '}';
    }
}
