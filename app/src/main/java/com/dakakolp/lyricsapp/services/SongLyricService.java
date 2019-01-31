package com.dakakolp.lyricsapp.services;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public class SongLyricService extends BaseService implements TaskListener<SongLyric> {
    public static final String SONG_LYRIC_BROADCAST = "song_lyric broadcast";

    public static final String PARAM_LINK_LYRIC = "link_lyric key";
    public static final String PARAM_TEXT_LYRIC = "text_lyric key";
    public final static String PARAM_STATUS = "status";


    public static final int STATUS_START = 100;
    public static final int STATUS_FINISH = 200;
    public static final int STATUS_RESULT = 300;

    public SongLyricService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String link = intent.getExtras().getString(PARAM_LINK_LYRIC);
            executeTask(link);
        }
        return START_STICKY;
    }

    private void executeTask(String link) {
        ParseSongLyricTask parseSongLyricTask = new ParseSongLyricTask(this);
        parseSongLyricTask.execute(link);
    }

    @Override
    public void showProgress() {
        sendLoadingStatus(STATUS_START);
    }

    @Override
    public void hideProgress() {
        sendLoadingStatus(STATUS_FINISH);
    }

    private void sendLoadingStatus(int status){
        Intent i = new Intent(SONG_LYRIC_BROADCAST);
        i.putExtra(PARAM_STATUS, status);
        sendBroadcast(i);
    }

    @Override
    public void onFinalResult(TaskResult<SongLyric> textSong) {
        if (textSong.getError() != null) {
            Toast.makeText(this, textSong.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        String lyricText = textSong.getResult().getText();
        Intent i = new Intent(SONG_LYRIC_BROADCAST);
        i.putExtra(PARAM_STATUS, STATUS_RESULT);
        i.putExtra(PARAM_TEXT_LYRIC, lyricText);
        sendBroadcast(i);
    }
}
