package com.dakakolp.lyricsapp.asynctasks.asyncmodels;

import com.dakakolp.lyricsapp.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongList {
    private List<Song> mSongs;
    private int mNumberSongs;

    public SongList() {
        mSongs = new ArrayList<>();
    }

    public SongList(List<Song> songs, int numberSongs) {
        mSongs = songs;
        mNumberSongs = numberSongs;
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
