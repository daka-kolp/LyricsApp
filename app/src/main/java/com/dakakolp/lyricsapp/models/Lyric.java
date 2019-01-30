package com.dakakolp.lyricsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Lyric implements Parcelable {
    private String mSinger;
    private String mTitle;
    private String mLink;

    public Lyric(String singer, String title, String link) {
        mSinger = singer;
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

    public String getSinger() {
        return mSinger;
    }

    public void setSinger(String singer) {
        mSinger = singer;
    }

    private Lyric(Parcel in) {
        mSinger = in.readString();
        mTitle = in.readString();
        mLink = in.readString();
    }

    public static final Creator<Lyric> CREATOR = new Creator<Lyric>() {
        @Override
        public Lyric createFromParcel(Parcel in) {
            return new Lyric(in);
        }

        @Override
        public Lyric[] newArray(int size) {
            return new Lyric[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSinger);
        dest.writeString(mTitle);
        dest.writeString(mLink);
    }
}
