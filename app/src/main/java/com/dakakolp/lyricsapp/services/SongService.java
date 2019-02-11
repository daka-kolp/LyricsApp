package com.dakakolp.lyricsapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.dakakolp.lyricsapp.asynctasks.ParseSongListTask;
import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongList;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.services.receivermodels.DataSearchRequest;
import com.dakakolp.lyricsapp.services.receivermodels.DataSearchResponse;

import java.util.List;

public class SongService extends Service {
    public static final String SONG_LIST_RECEIVER = "songListReceiver";
    public static final String IS_CANCELED = "isCanceled";

    public static final String PARAM_DATA_SEARCH_REQUEST = "dataSearchRequest";
    public static final String PARAM_DATA_SEARCH_RESPONSE = "dataSearchResponse";
    public final static String PARAM_STATUS_LIST = "status_list";

    public static final int STATUS_SHOW_PROGRESS = 100;
    public static final int STATUS_HIDE_PROGRESS = 200;
    public static final int STATUS_RESULT = 300;

    public static final String SONG_LYRIC_RECEIVER = "songLyricReceiver";

    public static final String PARAM_LINK_LYRIC = "linkLyricKey";
    public static final String PARAM_TEXT_LYRIC = "textLyricKey";
    public final static String PARAM_STATUS_LYR = "status_lyr";

    public static final int STATUS_SHOW_PROGRESS_LYR = 400;
    public static final int STATUS_HIDE_PROGRESS_LYR = 500;
    public static final int STATUS_RESULT_LYR = 600;

    private static final int NUMBER_SONGS_ON_PAGE = 20;
    private ParseSongListTask mParseSongListTask;

    public SongService() {
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
            if (bundle.getString(PARAM_LINK_LYRIC) != null) {
                String link = bundle.getString(PARAM_LINK_LYRIC);
                executeParseLyricTask(link);
            }

            if (bundle.getBoolean(IS_CANCELED)) {
                if (mParseSongListTask != null) {
                    mParseSongListTask.cancel(true);
                    return START_STICKY;
                }
            }
            if (bundle.getParcelable(PARAM_DATA_SEARCH_REQUEST) != null) {
                DataSearchRequest request = bundle.getParcelable(PARAM_DATA_SEARCH_REQUEST);
                if (request != null) {
                    executeParseSongListTask(request.getPage(), request.getTextSearch());
                }
            }
        }
        return START_STICKY;
    }

    private void executeParseSongListTask(final int page, String searchString) {
        if (!searchString.isEmpty()) {
            mParseSongListTask = new ParseSongListTask(page, new TaskListener<SongList>() {
                @Override
                public void showProgress() {
                    sendLoadingStatus(STATUS_SHOW_PROGRESS);
                }

                @Override
                public void hideProgress() {
                    sendLoadingStatus(STATUS_HIDE_PROGRESS);
                }

                private void sendLoadingStatus(int status) {
                    Intent i = new Intent(SONG_LIST_RECEIVER);
                    i.putExtra(PARAM_STATUS_LIST, status);
                    sendBroadcast(i);
                }

                @Override
                public void onFinalResult(TaskResult<SongList> songList) {
                    int numberPages;
                    List<Song> songs;
                    String textNumberPages;
                    if (songList.getError() != null) {
                        Toast.makeText(SongService.this, songList.getError(), Toast.LENGTH_SHORT).show();
                        numberPages = 0;
                        textNumberPages = "";
                        songs = null;
                    } else {
                        numberPages = getNumberOfPages(songList.getResult().getNumberSongs());
                        textNumberPages = getTextNumberText(page, numberPages);
                        songs = songList.getResult().getSongs();
                    }
                    sendResponse(numberPages, textNumberPages, songs);
                }

                private void sendResponse(int numberPages, String textNumberPages, List<Song> songs) {
                    DataSearchResponse response = new DataSearchResponse(numberPages, textNumberPages, songs);
                    Intent i = new Intent(SONG_LIST_RECEIVER);
                    i.putExtra(PARAM_STATUS_LIST, STATUS_RESULT);
                    i.putExtra(PARAM_DATA_SEARCH_RESPONSE, response);
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

            });
            mParseSongListTask.execute(searchString);
        }


    }

    private void executeParseLyricTask(String link) {
        ParseSongLyricTask parseSongLyricTask = new ParseSongLyricTask(new TaskListener<SongLyric>() {
            @Override
            public void showProgress() {
                sendLoadingStatus(STATUS_SHOW_PROGRESS_LYR);
            }

            @Override
            public void hideProgress() {
                sendLoadingStatus(STATUS_HIDE_PROGRESS_LYR);
            }

            private void sendLoadingStatus(int status) {
                Intent i = new Intent(SONG_LYRIC_RECEIVER);
                i.putExtra(PARAM_STATUS_LYR, status);
                sendBroadcast(i);
            }

            @Override
            public void onFinalResult(TaskResult<SongLyric> textSong) {
                if (textSong.getError() != null) {
                    Toast.makeText(SongService.this, textSong.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                String lyricText = textSong.getResult().getText();
                Intent i = new Intent(SONG_LYRIC_RECEIVER);
                i.putExtra(PARAM_STATUS_LYR, STATUS_RESULT_LYR);
                i.putExtra(PARAM_TEXT_LYRIC, lyricText);
                sendBroadcast(i);
            }
        });
        parseSongLyricTask.execute(link);
    }


}
