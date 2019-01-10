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

import java.util.List;

public class StartActivity extends BaseActivity implements
        TaskListener<SongList>,
        ListSongAdapter.OnClickSongListener {

    public static final String LYRIC_KEY = "linkToLyric key";
    private static final String SEARCH_STRING_KEY = "EditText key";
    private static final int NUMBER_SONGS_ON_PAGE = 20;
    private static final String PAGE_KEY = "page key";

    private int mPage;
    private String mSearchString;

    private int mNumberPages;
    private List<Song> mSongs;

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

        if(savedInstanceState != null) {
            mPage = savedInstanceState.getInt(PAGE_KEY);
            mSearchString = savedInstanceState.getString(SEARCH_STRING_KEY);
            uploadSongList(mPage, mSearchString);
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
        outState.putString(SEARCH_STRING_KEY, mSearchString);
        outState.putInt(PAGE_KEY, mPage);
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
    public void cancelProgress() {
        
    }

    @Override
    public void onFinalResult(TaskRequest<SongList> songs) {
        String textNumberPages;
        if (songs.getError() != null) {
            Toast.makeText(this, songs.getError(), Toast.LENGTH_SHORT).show();
            mNumberPages = 0;
            textNumberPages = "";
            mSongs = null;
            hideNavigationButtons();
        } else {
            mNumberPages = getNumberOfPages(songs.getResult().getNumberSongs());
            textNumberPages = "[" + mPage + " page of " + mNumberPages + "]";
            mSongs = songs.getResult().getSongs();
            showNavigationButtons();
        }
        refreshViews(textNumberPages, mSongs);
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

    private void showNavigationButtons() {
        mImageButtonBefore.setVisibility(View.VISIBLE);
        mImageButtonNext.setVisibility(View.VISIBLE);
    }

    private void hideNavigationButtons() {
        mImageButtonBefore.setVisibility(View.GONE);
        mImageButtonNext.setVisibility(View.GONE);
    }

    private void refreshViews(String textNumberPages, List<Song> songs) {
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
