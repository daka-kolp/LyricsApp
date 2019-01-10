package com.dakakolp.lyricsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Lyric implements Parcelable {
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

    private Lyric(Parcel in) {
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
        dest.writeString(mTitle);
        dest.writeString(mLink);
    }
}
