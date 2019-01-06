package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseLyricTask;

public class LyricActivity extends AppCompatActivity implements ParseLyricTask.ParseLyricListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

//  implementation ParseLyricTask.ParseLyricListener
    @Override
    public void showProgressBar() {

    }

    @Override
    public void updateProgressBar() {

    }

    @Override
    public void getFinalResult(String textSong) {
        if(textSong.isEmpty()) {
            Toast.makeText(this, "No access to lyric", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView textViewLyric = findViewById(R.id.textview_lyric);
        textViewLyric.setText(textSong);
    }
}
