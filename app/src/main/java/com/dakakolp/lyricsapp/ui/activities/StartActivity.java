package com.dakakolp.lyricsapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseSongTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.ListSong;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;
import com.dakakolp.lyricsapp.utils.ParserHelper;

import java.util.List;

public class StartActivity extends AppCompatActivity implements
        TaskListener<ListSong>,
        ListSongAdapter.OnClickSongListener{

    public static final String LINK_TO_LYRIC = "link to lyric";
    public static final String TITLE_SONG = "title song";
    private static int sNumberSongsOnPage = 20;

    private static int sPage = 0;
    private String mSearchString;

    private int mNumberSongs;
    private List<Song> mSongs;

    private EditText mEditTextSearch;
    private ImageButton mImageButtonSearch;
    private ImageButton mImageButtonBefore;
    private ImageButton mImageButtonNext;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ListSongAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        mProgressBar.setVisibility(View.GONE);

        mImageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ParserHelper.isOnline(StartActivity.this)){
                    showToastError();
                    return;
                }
                mImageButtonNext.setVisibility(View.VISIBLE);
                sPage = 1;
                mSearchString = mEditTextSearch.getText().toString();
                uploadSongList(sPage, mSearchString);
                closeKeyboard();
            }
        });

        mImageButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ParserHelper.isOnline(StartActivity.this)){
                    showToastError();
                    return;
                }
                if(sPage > 1) {
                    sPage--;
                    uploadSongList(sPage, mSearchString);
                } else {
                    showToast();
                }
            }
        });

        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ParserHelper.isOnline(StartActivity.this)){
                    showToastError();
                    return;
                }
                int pages = mNumberSongs/sNumberSongsOnPage;
                if(mNumberSongs >= sNumberSongsOnPage && mNumberSongs%sNumberSongsOnPage > 0)
                    pages++;
                if(sPage >= 1 && sPage < pages) {
                    sPage++;
                    uploadSongList(sPage, mSearchString);
                } else {
                    showToast();
                }
            }
        });
    }

    private void showToast() {
        Toast.makeText(StartActivity.this, "out",Toast.LENGTH_SHORT).show();
    }

    private void showToastError() {
        Toast.makeText(StartActivity.this, "Error, check connection",Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        mEditTextSearch = findViewById(R.id.edittext_search);
        mImageButtonSearch = findViewById(R.id.image_button_search);
        mImageButtonBefore = findViewById(R.id.image_button_before);
        mImageButtonNext = findViewById(R.id.image_button_next);
        mProgressBar = findViewById(R.id.progress_loading_songs);

        mRecyclerView = findViewById(R.id.recycler_view_songs);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new ListSongAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void uploadSongList(int page, String searchString) {
        new ParseSongTask(page, this).execute(searchString);
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/


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
    public void onFinalResult(TaskRequest<ListSong> songs) {
        if (songs.getError() != null) {
            Toast.makeText(this, songs.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mNumberSongs = songs.getResult().getNumberSongs();
        mSongs = songs.getResult().getSongs();
        mAdapter.setSongList(mSongs);
        mAdapter.notifyDataSetChanged();
    }

    //  implementation ListSongAdapter.OnClickSongListener
    @Override
    public void onClickSong(int position) {
        Intent intent = new Intent(StartActivity.this, LyricActivity.class);
        intent.putExtra(TITLE_SONG, mSongs.get(position).getSongTitle());
        intent.putExtra(LINK_TO_LYRIC, mSongs.get(position).getLink());
        startActivity(intent);
    }
}
