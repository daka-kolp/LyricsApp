package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Lyric;

public class LyricActivity extends BaseActivity implements TaskListener<SongLyric> {

    private TextView mTextViewLyric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        Lyric lyric = getDataIntent();

        initViews();
        initToolbar(lyric.getTitle());

        new ParseSongLyricTask(this).execute(lyric.getLink());
    }

    private void initViews() {
        mTextViewLyric = findViewById(R.id.text_view_lyric);
    }

    private void initToolbar(String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private Lyric getDataIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(StartActivity.LYRIC_KEY);
    }

    //  implementation TaskListener

    @Override
    public void showProgress() {
        super.showProgress();
    }
    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void onFinalResult(TaskRequest<SongLyric> textSong) {
        if (textSong.getError() != null) {
            Toast.makeText(this, textSong.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mTextViewLyric.setText(textSong.getResult().getText());
    }

}
