package com.dakakolp.lyricsapp.html.modelshttp;

import android.net.Uri;

public class Album {

    private String mImage;
    private String mSinger;
    private String mAlbumTitle;
    private String mLink;

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getSinger() {
        return mSinger;
    }

    public void setSinger(String singer) {
        mSinger = singer;
    }

    public String getSongTitle() {

        return mAlbumTitle;
    }

    public void setSongTitle(String songTitle) {
        mAlbumTitle = songTitle;
    }

}
