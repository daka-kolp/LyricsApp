package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;

public class LyricActivity extends AppCompatActivity implements
        TaskListener<String> {

    private  TextView mTextViewLyric;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewLyric = findViewById(R.id.text_view_lyric);
        mProgressBar = findViewById(R.id.progress_loading_lyric);
        mProgressBar.setVisibility(View.GONE);

        getDataIntent();
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(StartActivity.TITLE_SONG);
        String link = intent.getStringExtra(StartActivity.LINK_TO_LYRIC);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);

        new ParseLyricTask(this).execute(link);
    }

//  implementation TaskListener
    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFinalResult(TaskRequest<String> textSong) {
        if(textSong.getError() != null) {
            Toast.makeText(this, textSong.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mTextViewLyric.setText(textSong.getResult());
    }

}
