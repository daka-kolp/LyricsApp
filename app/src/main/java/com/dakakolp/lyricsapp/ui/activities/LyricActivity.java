package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;
import com.dakakolp.lyricsapp.models.Lyric;

public class LyricActivity extends BaseActivity implements TaskListener<SongLyric> {

    private static final String LYRIC_TEXT_KEY = "lyric text key";
    private static final String LYRIC_KEY = "lyric key";
    private TextView mTextViewTitle;
    private TextView mTextViewLyric;
    private FrameLayout mProgressBar;

    private Lyric mLyric;
    private String mLyricText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        initViews();
        loadData(savedInstanceState);
        initToolbar(mLyric.getTitle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LYRIC_KEY, mLyric);
        outState.putString(LYRIC_TEXT_KEY, mLyricText);
    }

    private void loadData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLyric = savedInstanceState.getParcelable(LYRIC_KEY);
            mLyricText = savedInstanceState.getString(LYRIC_TEXT_KEY);
            refreshViews(mLyric, mLyricText);
        } else {
            mLyric = getDataIntent();
            new ParseSongLyricTask(this).execute(mLyric.getLink());
        }
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_layout);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewLyric = findViewById(R.id.text_view_lyric);
    }

    private void initToolbar(String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private Lyric getDataIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(StartActivity.INTENT_LYRIC_KEY);
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
    public void onFinalResult(TaskResult<SongLyric> textSong) {
        if (textSong.getError() != null) {
            Toast.makeText(this, textSong.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mLyricText = textSong.getResult().getText();
        refreshViews(mLyric, mLyricText);
    }

    private void refreshViews(Lyric lyric, String textSong) {
        mTextViewTitle.setText(lyric.getTitle() + "\nby\n" + lyric.getSinger());
        mTextViewLyric.setText(textSong);
    }

}
