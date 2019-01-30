package com.dakakolp.lyricsapp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.widget.Toast;

import com.dakakolp.lyricsapp.asynctasks.ParseSongListTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongList;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;
import com.dakakolp.lyricsapp.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongListService extends Service implements TaskListener<SongList> {
    public static final String SEARCH_STRING = "search string";
    public static final String PAGE_NUMBER = "page number";

    public static final String SONG_LIST_BROADCAST = "song list broadcast";
    public static final String NUMBER_PAGES_KEY = "number_pages key";
    public static final String TEXT_NUMBER_PAGES_KEY = "text_number_pages key";
    public static final String SONG_LIST_KEY = "song_list key";
    public static final String IS_LOADING = "is loading" ;
    public static final String IS_CANCELED = "is canceled";

    private static final int NUMBER_SONGS_ON_PAGE = 20;

    private int mPage;
    private ParseSongListTask mParseSongListTask;
    public SongListService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean isCanceled = bundle.getBoolean(IS_CANCELED, false);
            if(isCanceled){
                if (mParseSongListTask != null) {
                    mParseSongListTask.cancel(true);
                    return START_STICKY;
                }
            }
            mPage = bundle.getInt(PAGE_NUMBER);
            String searchString = bundle.getString(SEARCH_STRING);
            executeTask(mPage, searchString);
        }
        return START_STICKY;
    }

    private void executeTask(int page, String searchString) {
        mParseSongListTask = new ParseSongListTask(page, this);
        mParseSongListTask.execute(searchString);
    }

    @Override
    public void showProgress() {
        Intent i = new Intent(SONG_LIST_BROADCAST);
        i.putExtra(IS_LOADING, true);
        sendBroadcast(i);
    }

    @Override
    public void hideProgress() {
        Intent i = new Intent(SONG_LIST_BROADCAST);
        i.putExtra(IS_LOADING, false);
        sendBroadcast(i);
    }

    @Override
    public void onFinalResult(TaskResult<SongList> songList) {
        int numberPages;
        List<Song> songs;
        String textNumberPages;
        if (songList.getError() != null) {
            Toast.makeText(this, songList.getError(), Toast.LENGTH_SHORT).show();
            numberPages = 0;
            textNumberPages = "";
            songs = null;
        } else {
            numberPages = getNumberOfPages(songList.getResult().getNumberSongs());
            textNumberPages = getTextNumberText(mPage, numberPages);
            songs = songList.getResult().getSongs();
        }
        Intent i = new Intent(SONG_LIST_BROADCAST);
        i.putExtra(NUMBER_PAGES_KEY, numberPages);
        i.putExtra(TEXT_NUMBER_PAGES_KEY, textNumberPages);
        i.putParcelableArrayListExtra(SONG_LIST_KEY, (ArrayList<Song>) songs);
        sendBroadcast(i);
    }


    private int getNumberOfPages(int numberSongs) {
        int pages = numberSongs / NUMBER_SONGS_ON_PAGE;
        if (numberSongs > NUMBER_SONGS_ON_PAGE && numberSongs % NUMBER_SONGS_ON_PAGE > 0) {
            pages++;
        } else if (numberSongs <= NUMBER_SONGS_ON_PAGE) {
            pages = 1;
        }
        return pages;
    }

    private String getTextNumberText(int page, int numberPages) {
        return "[" + page + " page of " + numberPages + "]";
    }

}
