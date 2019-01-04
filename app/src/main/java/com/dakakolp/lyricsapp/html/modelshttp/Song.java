package com.dakakolp.lyricsapp.html.modelshttp;

import android.net.Uri;

public class Song {

    private Uri mImage;
    private String mSinger;
    private String mSongTitle;
    private String mLyric;

    public Uri getImage() {
        return mImage;
    }

    public void setImage(Uri image) {
        mImage = image;
    }

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

    public String getLyric() {
        return mLyric;
    }

    public void setLyric(String lyric) {
        mLyric = lyric;
    }

}
