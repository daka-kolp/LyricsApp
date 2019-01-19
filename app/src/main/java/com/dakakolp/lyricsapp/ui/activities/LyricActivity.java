package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Lyric;
import com.dakakolp.lyricsapp.ui.fragment.ParseSongLyricFragment;
import com.dakakolp.lyricsapp.ui.fragment.callbacks.AsyncTaskFragmentCallback;

public class LyricActivity extends BaseActivity implements AsyncTaskFragmentCallback {

    private static final String LYRIC_KEY = "lyric key";
    private static final String LYRIC_FRAG_TAG = "lyric_frag_tag";

    private TextView mTextViewLyric;
    private String mTextSong;
    private FrameLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);
        Lyric lyric = getDataIntent();

        initViews();
        initToolbar(lyric.getTitle());

        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        } else {
            loadLyricLink(lyric.getLink());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LYRIC_KEY, mTextSong);
    }

    private Lyric getDataIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(StartActivity.INTENT_LYRIC_KEY);
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_layout);
        mTextViewLyric = findViewById(R.id.text_view_lyric);
    }

    private void initToolbar(String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void restoreData(Bundle savedInstanceState) {
        mTextSong = savedInstanceState.getString(LYRIC_KEY);
        mTextViewLyric.setText(mTextSong);
    }

    private void loadLyricLink(String lyricLink) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ParseSongLyricFragment fragment = (ParseSongLyricFragment) fragmentManager.findFragmentByTag(LYRIC_FRAG_TAG);
        if (fragment == null) {
            fragment = ParseSongLyricFragment.newInstance(lyricLink);
            fragmentManager.beginTransaction().add(fragment, LYRIC_FRAG_TAG).commit();
        }
    }

//  implementation AsyncTaskLyricFragmentCallback

    @Override
    public void updateFragmentProgress(boolean isLoading) {
        if (isLoading) {
            setViewVisible();
        } else {
            setViewInvisible();
        }
    }

    @Override
    public void showFragmentProgress() {
        setViewVisible();
    }

    @Override
    public void hideFragmentProgress() {
        setViewInvisible();

    }

    private void setViewInvisible() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void setViewVisible() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentFinalResult(TaskRequest<SongLyric> textSong) {
        if (textSong.getError() != null) {
            Toast.makeText(this, textSong.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mTextSong = textSong.getResult().getText();
        mTextViewLyric.setText(mTextSong);
    }

}
