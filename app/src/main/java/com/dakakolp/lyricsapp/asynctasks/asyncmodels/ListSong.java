package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

import com.dakakolp.lyricsapp.models.Song;

import java.util.ArrayList;
import java.util.List;

public class ListSong {
    private List<Song> mSongs;
    private int mNumberSongs;

    public ListSong() {
        mSongs = new ArrayList<>();
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        this.mSongs = songs;
    }

    public int getNumberSongs() {
        return mNumberSongs;
    }

    public void setNumberSongs(int numberSongs) {
        mNumberSongs = numberSongs;
    }
}
