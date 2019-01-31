package com.dakakolp.lyricsapp.services.receivermodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.dakakolp.lyricsapp.models.Song;

import java.util.List;

public class DataSearchResponse implements Parcelable {

    private int mNumberPages;
    private String mTextNumberPages;
    private List<Song> mSongs;

    public DataSearchResponse(int numberPages, String textNumberPages, List<Song> songs) {
        mNumberPages = numberPages;
        mTextNumberPages = textNumberPages;
        mSongs = songs;
    }

    protected DataSearchResponse(Parcel in) {
        mNumberPages = in.readInt();
        mTextNumberPages = in.readString();
        mSongs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<DataSearchResponse> CREATOR = new Creator<DataSearchResponse>() {
        @Override
        public DataSearchResponse createFromParcel(Parcel in) {
            return new DataSearchResponse(in);
        }

        @Override
        public DataSearchResponse[] newArray(int size) {
            return new DataSearchResponse[size];
        }
    };

    public int getNumberPages() {
        return mNumberPages;
    }

    public void setNumberPages(int numberPages) {
        mNumberPages = numberPages;
    }

    public String getTextNumberPages() {
        return mTextNumberPages;
    }

    public void setTextNumberPages(String textNumberPages) {
        mTextNumberPages = textNumberPages;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mNumberPages);
        dest.writeString(mTextNumberPages);
        dest.writeTypedList(mSongs);
    }
}
