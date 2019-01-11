package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseSongListTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongList;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Lyric;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends BaseActivity implements
        TaskListener<SongList>,
        ListSongAdapter.OnClickSongListener {

    private static final String PAGE_KEY = "page key";
    private static final String NUMBER_PAGES_KEY = "number pages key";
    private static final String SEARCH_STRING_KEY = "search string key";
    private static final String SONG_LIST_KEY = "song list key";
    public static final String LYRIC_KEY = "linkToLyric key";
    private static final int NUMBER_SONGS_ON_PAGE = 20;

    private int mPage;
    private String mSearchString;

    private int mNumberPages;
    private List<Song> mSongs;
    private String mTextNumberPages;

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

        if (savedInstanceState != null) {
            mPage = savedInstanceState.getInt(PAGE_KEY);
            mNumberPages = savedInstanceState.getInt(NUMBER_PAGES_KEY);
            mSearchString = savedInstanceState.getString(SEARCH_STRING_KEY);
            mSongs = savedInstanceState.getParcelableArrayList(SONG_LIST_KEY);
            if(mSongs != null) {
                mTextNumberPages = getTextNumberText(mPage, mNumberPages);
                refreshViews(mTextNumberPages, mSongs);
            }

        }

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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_KEY, mPage);
        outState.putInt(NUMBER_PAGES_KEY, mNumberPages);
        outState.putString(SEARCH_STRING_KEY, mSearchString);
        outState.putParcelableArrayList(SONG_LIST_KEY, (ArrayList<Song>) mSongs);
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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

    private void clickOnSearch() {
        mPage = 1;
        mSearchString = mEditTextSearch.getText().toString();
        uploadSongList(mPage, mSearchString);
        closeKeyboard();
    }

    private void clickOnBefore() {
        if (mPage > 1) {
            mPage--;
            uploadSongList(mPage, mSearchString);
        }
    }

    private void clickOnNext() {
        if (mPage >= 1 && mPage < mNumberPages) {
            mPage++;
            uploadSongList(mPage, mSearchString);
        }
    }

    private void uploadSongList(int page, String searchString) {
        new ParseSongListTask(page, this).execute(searchString);
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
    public void onFinalResult(TaskRequest<SongList> songs) {
        if (songs.getError() != null) {
            Toast.makeText(this, songs.getError(), Toast.LENGTH_SHORT).show();
            mNumberPages = 0;
            mTextNumberPages = "";
            mSongs = null;
        } else {
            mNumberPages = getNumberOfPages(songs.getResult().getNumberSongs());
            mTextNumberPages = getTextNumberText(mPage, mNumberPages);
            mSongs = songs.getResult().getSongs();
        }
        refreshViews(mTextNumberPages, mSongs);
    }

    private int getNumberOfPages(int numberSongs) {
        int pages = numberSongs / NUMBER_SONGS_ON_PAGE;
        if (numberSongs > NUMBER_SONGS_ON_PAGE && numberSongs % NUMBER_SONGS_ON_PAGE > 0) {
            pages++;
        } else if (numberSongs <= NUMBER_SONGS_ON_PAGE) {
            pages = 1;
        }
        return pages;
    }

    private String getTextNumberText(int page, int numberPages) {
        return "[" + page + " page of " + numberPages + "]";
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
        intent.putExtra(LYRIC_KEY, new Lyric(
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
}
