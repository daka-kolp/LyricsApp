package com.dakakolp.lyricsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mSinger;
    private String mSongTitle;
    private String mLink;

    public Song() {
    }

    protected Song(Parcel in) {
        mSinger = in.readString();
        mSongTitle = in.readString();
        mLink = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSinger);
        dest.writeString(mSongTitle);
        dest.writeString(mLink);
    }
}
