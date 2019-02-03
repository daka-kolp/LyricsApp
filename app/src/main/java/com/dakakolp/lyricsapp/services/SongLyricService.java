package com.dakakolp.lyricsapp.services;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public class SongLyricService extends BaseService {
    public static final String SONG_LYRIC_RECEIVER = "songLyricReceiver";

    public static final String PARAM_LINK_LYRIC = "linkLyricKey";
    public static final String PARAM_TEXT_LYRIC = "textLyricKey";
    public final static String PARAM_STATUS = "status";


    public static final int STATUS_SHOW_PROGRESS = 100;
    public static final int STATUS_HIDE_PROGRESS = 200;
    public static final int STATUS_RESULT = 300;

    public SongLyricService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String link = intent.getExtras().getString(PARAM_LINK_LYRIC);
            executeParseLyricTask(link);
        }
        return START_STICKY;
    }

    private void executeParseLyricTask(String link) {
        ParseSongLyricTask parseSongLyricTask = new ParseSongLyricTask(new TaskListener<SongLyric>() {
            @Override
            public void showProgress() {
                sendLoadingStatus(STATUS_SHOW_PROGRESS);
            }

            @Override
            public void hideProgress() {
                sendLoadingStatus(STATUS_HIDE_PROGRESS);
            }

            private void sendLoadingStatus(int status){
                Intent i = new Intent(SONG_LYRIC_RECEIVER);
                i.putExtra(PARAM_STATUS, status);
                sendBroadcast(i);
            }

            @Override
            public void onFinalResult(TaskResult<SongLyric> textSong) {
                if (textSong.getError() != null) {
                    Toast.makeText(SongLyricService.this, textSong.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                String lyricText = textSong.getResult().getText();
                Intent i = new Intent(SONG_LYRIC_RECEIVER);
                i.putExtra(PARAM_STATUS, STATUS_RESULT);
                i.putExtra(PARAM_TEXT_LYRIC, lyricText);
                sendBroadcast(i);
            }
        });
        parseSongLyricTask.execute(link);
    }


}
