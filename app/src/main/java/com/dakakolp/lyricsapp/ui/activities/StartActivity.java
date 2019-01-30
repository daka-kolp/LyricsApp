package com.dakakolp.lyricsapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.models.Lyric;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.services.SongListService;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends BaseActivity implements
        ListSongAdapter.OnClickSongListener {
//    public static final String TAG = "qwerty";

    private static final String PAGE_KEY = "page key";
    public static final String TEXT_NUMBER_PAGES_KEY = "text_number_pages key";
    private static final String SEARCH_STRING_KEY = "search_string key";
    private static final String NUMBER_PAGES_KEY = "number_pages key";
    private static final String SONG_LIST_KEY = "song_list key";
    public static final String INTENT_LYRIC_KEY = "link_to_lyric key";

    private int mPage;
    private String mSearchString;

    private int mNumberPages;
    private List<Song> mSongs;
    private String mTextNumberPages;

    private FrameLayout mProgressBarLayout;
    private LinearLayout mMainLayout;

    private EditText mEditTextSearch;
    private ImageButton mImageButtonSearch;
    private TextView mTextViewNumberPages;
    private ImageButton mImageButtonBefore;
    private ImageButton mImageButtonNext;
    private RecyclerView mRecyclerView;
    private ListSongAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initViews();
        hideNavigationButtons();
        restoreData(savedInstanceState);

        mImageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnSearch();
            }
        });
        mImageButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnBefore();
            }
        });
        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnNext();
            }
        });

        mProgressBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgress();
                cancel();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_KEY, mPage);
        outState.putInt(NUMBER_PAGES_KEY, mNumberPages);
        outState.putString(SEARCH_STRING_KEY, mSearchString);
        outState.putString(TEXT_NUMBER_PAGES_KEY, mTextNumberPages);
        outState.putParcelableArrayList(SONG_LIST_KEY, (ArrayList<Song>) mSongs);
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mProgressBarLayout = findViewById(R.id.progress_layout);
        mMainLayout = findViewById(R.id.main_linear_layout);

        mEditTextSearch = findViewById(R.id.edittext_search);
        mImageButtonSearch = findViewById(R.id.image_button_search);
        mImageButtonBefore = findViewById(R.id.image_button_before);
        mImageButtonNext = findViewById(R.id.image_button_next);
        mTextViewNumberPages = findViewById(R.id.text_view_pages);

        mRecyclerView = findViewById(R.id.recycler_view_songs);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new ListSongAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPage = savedInstanceState.getInt(PAGE_KEY);
            mNumberPages = savedInstanceState.getInt(NUMBER_PAGES_KEY);
            mSearchString = savedInstanceState.getString(SEARCH_STRING_KEY);
            mSongs = savedInstanceState.getParcelableArrayList(SONG_LIST_KEY);
            if (mSongs != null) {
                mTextNumberPages = savedInstanceState.getString(TEXT_NUMBER_PAGES_KEY);
                refreshViews(mTextNumberPages, mSongs);
            }
        }
    }

    private void clickOnSearch() {
        mPage = 1;
        mSearchString = mEditTextSearch.getText().toString();
        downloadSongList(mPage, mSearchString);
        closeKeyboard();
    }

    private void clickOnBefore() {
        if (mPage > 1) {
            mPage--;
            downloadSongList(mPage, mSearchString);
        }
    }

    private void clickOnNext() {
        if (mPage >= 1 && mPage < mNumberPages) {
            mPage++;
            downloadSongList(mPage, mSearchString);
        }
    }

    private void downloadSongList(int page, String searchString) {
        Intent intent = new Intent(this, SongListService.class);
        intent.putExtra(SongListService.PAGE_NUMBER, page);
        intent.putExtra(SongListService.SEARCH_STRING, searchString);
        startService(intent);
    }

    private void cancel() {
        Intent intent = new Intent(this, SongListService.class);
        intent.putExtra(SongListService.IS_CANCELED, true);
        startService(intent);
    }

    public void showProgress() {
        mMainLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mMainLayout.setVisibility(View.VISIBLE);
        mProgressBarLayout.setVisibility(View.GONE);
    }

    private void showNavigationButtons() {
        mImageButtonBefore.setVisibility(View.VISIBLE);
        mImageButtonNext.setVisibility(View.VISIBLE);
    }

    private void hideNavigationButtons() {
        mImageButtonBefore.setVisibility(View.GONE);
        mImageButtonNext.setVisibility(View.GONE);
    }

    private void refreshViews(String textNumberPages, List<Song> songs) {
        if (mSongs == null) {
            hideNavigationButtons();
        } else {
            showNavigationButtons();
        }
        mTextViewNumberPages.setText(textNumberPages);
        mAdapter.setSongList(songs);
        mRecyclerView.scrollToPosition(0);
        mAdapter.notifyDataSetChanged();

    }

    //  implementation ListSongAdapter.OnClickSongListener
    @Override
    public void onClickSong(int position) {
        Intent intent = new Intent(StartActivity.this, LyricActivity.class);
        intent.putExtra(INTENT_LYRIC_KEY, new Lyric(
                mSongs.get(position).getSinger(),
                mSongs.get(position).getSongTitle(),
                mSongs.get(position).getLink())
        );
        startActivity(intent);
    }

    /*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    private BroadcastReceiver uiUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                boolean isLoading = intent.getExtras().getBoolean(SongListService.IS_LOADING);
                if (isLoading) {
                    showProgress();
                    return;
                } else {
                    hideProgress();
                }
                mNumberPages = intent.getExtras().getInt(SongListService.NUMBER_PAGES_KEY);
                mTextNumberPages = intent.getExtras().getString(SongListService.TEXT_NUMBER_PAGES_KEY);
                mSongs = intent.getExtras().getParcelableArrayList(SongListService.SONG_LIST_KEY);
                refreshViews(mTextNumberPages, mSongs);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(uiUpdate, new IntentFilter(SongListService.SONG_LIST_BROADCAST));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(uiUpdate);
    }
}
