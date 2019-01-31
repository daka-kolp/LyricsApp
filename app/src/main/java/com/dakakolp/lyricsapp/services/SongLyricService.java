package com.dakakolp.lyricsapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public class SongLyricService extends BaseService implements TaskListener<SongLyric> {
    public static final String SONG_LYRIC_BROADCAST = "song_lyric broadcast";
    public static final String LOAD_STATUS_BROADCAST = "loading_status broadcast";

    public static final String LINK_LYRIC_KEY = "link_lyric key";
    public static final String TEXT_LYRIC_KEY = "text_lyric key";

    public static final String IS_LYRIC_LOADING = "is lyric loading";

    private ParseSongLyricTask mParseSongLyricTask;

    public SongLyricService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String link = intent.getExtras().getString(LINK_LYRIC_KEY);
            executeTask(link);
        }
        return START_STICKY;
    }

    private void executeTask(String link) {
        mParseSongLyricTask = new ParseSongLyricTask(this);
        mParseSongLyricTask.execute(link);
    }

    @Override
    public void showProgress() {
        sendLoadingStatus(true);
    }

    @Override
    public void hideProgress() {
        sendLoadingStatus(false);
    }

    private void sendLoadingStatus(boolean isLoading){
        Intent i = new Intent(LOAD_STATUS_BROADCAST);
        i.putExtra(IS_LYRIC_LOADING, isLoading);
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
        i.putExtra(TEXT_LYRIC_KEY, lyricText);
        sendBroadcast(i);
    }
}
