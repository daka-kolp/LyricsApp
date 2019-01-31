package com.dakakolp.lyricsapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.models.Lyric;
import com.dakakolp.lyricsapp.services.SongListService;
import com.dakakolp.lyricsapp.services.SongLyricService;

public class LyricActivity extends BaseActivity {

    private static final String TEXT_SAVE_INST_KEY = "lyric_text key";
    private static final String LYRIC_SAVE_INST_KEY = "lyric_obj key";

    private TextView mTextViewTitle;
    private TextView mTextViewLyric;

    private FrameLayout mProgressBar;

    private Lyric mLyric;
    private String mLyricText;

    private BroadcastReceiver mLyricReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                int status = intent.getExtras().getInt(SongListService.PARAM_STATUS, 0);
                switch (status) {
                    case SongListService.STATUS_START:
                        updateProgress(true);
                        break;
                    case SongListService.STATUS_FINISH:
                        updateProgress(false);
                        break;
                    case SongListService.STATUS_RESULT:
                        mLyricText = intent.getExtras().getString(SongLyricService.PARAM_TEXT_LYRIC);
                        updateViews(mLyric, mLyricText);
                        break;
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mLyricReceiver, new IntentFilter(SongLyricService.SONG_LYRIC_BROADCAST));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mLyricReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LYRIC_SAVE_INST_KEY, mLyric);
        outState.putString(TEXT_SAVE_INST_KEY, mLyricText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        initViews();
        loadData(savedInstanceState);
        initToolbar(mLyric.getTitle());
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progress_layout);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewLyric = findViewById(R.id.text_view_lyric);
    }

    private void loadData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLyric = savedInstanceState.getParcelable(LYRIC_SAVE_INST_KEY);
            mLyricText = savedInstanceState.getString(TEXT_SAVE_INST_KEY);
            updateViews(mLyric, mLyricText);
        } else {
            mLyric = getDataIntent();
            downloadLyric(mLyric.getLink());
        }
    }

    private Lyric getDataIntent() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(StartActivity.LYRIC_KEY);
    }

    private void downloadLyric(String link) {
        Intent intent = new Intent(this, SongLyricService.class);
        intent.putExtra(SongLyricService.PARAM_LINK_LYRIC, link);
        startService(intent);
    }

    private void initToolbar(String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void updateProgress(boolean loading) {
        if (loading) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    private void updateViews(Lyric lyric, String textSong) {
        mTextViewTitle.setText(lyric.getTitle() + "\nby\n" + lyric.getSinger());
        mTextViewLyric.setText(textSong);
    }
}
