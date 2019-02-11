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
import com.dakakolp.lyricsapp.services.SongService;

public class LyricActivity extends BaseActivity {

    private static final String TEXT_SAVE_INST_KEY = "lyricTextKey";
    private static final String LYRIC_SAVE_INST_KEY = "lyricObjKey";

    private TextView mTextViewTitle;
    private TextView mTextViewLyric;

    private FrameLayout mProgressBar;

    private Lyric mLyric;
    private String mLyricText;

    private BroadcastReceiver mLyricReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                int status = intent.getExtras().getInt(SongService.PARAM_STATUS_LYR, 0);
                switch (status) {
                    case SongService.STATUS_SHOW_PROGRESS_LYR:
                        updateStatusProgress(true);
                        break;
                    case SongService.STATUS_HIDE_PROGRESS_LYR:
                        updateStatusProgress(false);
                        break;
                    case SongService.STATUS_RESULT_LYR:
                        mLyricText = intent.getExtras().getString(SongService.PARAM_TEXT_LYRIC);
                        if (mLyricText != null) {
                            updateViews();
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        initViews();
        loadData(savedInstanceState);
        initToolbar(mLyric.getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mLyricReceiver, new IntentFilter(SongService.SONG_LYRIC_RECEIVER));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LYRIC_SAVE_INST_KEY, mLyric);
        outState.putString(TEXT_SAVE_INST_KEY, mLyricText);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mLyricReceiver);
    }

    private void updateStatusProgress(boolean loading) {
        if (loading) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void updateViews() {
        mTextViewTitle.setText(mLyric.getTitle() + "\nby\n" + mLyric.getSinger());
        mTextViewLyric.setText(mLyricText);
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
            updateViews();
        } else {
            mLyric = getDataIntent();
            downloadLyric(mLyric.getLink());
        }
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

    private void downloadLyric(String link) {
        Intent intent = new Intent(this, SongService.class);
        intent.putExtra(SongService.PARAM_LINK_LYRIC, link);
        startService(intent);
    }
}
