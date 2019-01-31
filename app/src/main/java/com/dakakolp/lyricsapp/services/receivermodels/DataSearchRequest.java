package com.dakakolp.lyricsapp.services.receivermodels;

import android.os.Parcel;
import android.os.Parcelable;

public class DataSearchRequest implements Parcelable {

    private int mPage;
    private String mTextSearch;

    public DataSearchRequest(int page, String textSearch) {
        mPage = page;
        mTextSearch = textSearch;
    }

    protected DataSearchRequest(Parcel in) {
        mPage = in.readInt();
        mTextSearch = in.readString();
    }

    public static final Creator<DataSearchRequest> CREATOR = new Creator<DataSearchRequest>() {
        @Override
        public DataSearchRequest createFromParcel(Parcel in) {
            return new DataSearchRequest(in);
        }

        @Override
        public DataSearchRequest[] newArray(int size) {
            return new DataSearchRequest[size];
        }
    };

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public String getTextSearch() {
        return mTextSearch;
    }

    public void setTextSearch(String textSearch) {
        mTextSearch = textSearch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPage);
        dest.writeString(mTextSearch);
    }
}
