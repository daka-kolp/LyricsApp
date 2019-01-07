package com.dakakolp.lyricsapp.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.ui.fragments.SongFragment;

public class StartActivity extends AppCompatActivity implements
        SongFragment.OnSongListFragmentInteractionListener {

    public static final String LINK_TO_LYRIC = "link to lyric";
    public static final String TITLE_SONG = "title song";

    private int mPage;
    private String mSearchString;

    private EditText mEditTextSearch;
    private ImageButton mImageButtonSearch;
    private ImageButton mImageButtonBefore;
    private ImageButton mImageButtonNext;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEditTextSearch = findViewById(R.id.edittext_search);
        mImageButtonSearch = findViewById(R.id.image_button_search);
        mImageButtonBefore = findViewById(R.id.image_button_before);
        mImageButtonNext = findViewById(R.id.image_button_next);
        mProgressBar = findViewById(R.id.progress_loading_songs);

        mImageButtonBefore.setVisibility(View.GONE);
        mImageButtonNext.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        mImageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mImageButtonNext.setVisibility(View.VISIBLE);
                mPage = 1;
                mSearchString = mEditTextSearch.getText().toString();
                uploadFragment(mPage, mSearchString);
                closeKeyboard();
            }
        });

        mImageButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if(mPage != 1) {
                    mPage--;
                    mImageButtonBefore.setVisibility(View.VISIBLE);
                } else {
                    mImageButtonBefore.setVisibility(View.GONE);
                }*/
                uploadFragment(mPage, mSearchString);
            }
        });

        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if(mPage >= 1 && mPage <= 50) {
                    mPage++;
                    mImageButtonNext.setVisibility(View.VISIBLE);
                } else {
                    mImageButtonNext.setVisibility(View.GONE);
                }
                if (mPage > 1){
                    mImageButtonBefore.setVisibility(View.VISIBLE);
                } else {
                    mImageButtonBefore.setVisibility(View.GONE);
                }*/
                uploadFragment(mPage, mSearchString);
            }
        });
    }

    private void uploadFragment(int page, String searchString) {
        SongFragment fragment = SongFragment.newInstance(page, searchString);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.song_list_fragment, fragment)
                .commit();
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

    //  implementation SongFragment.OnSongListFragmentInteractionListener
    @Override
    public void onOpenLyric(Song song) {
        Intent intent = new Intent(StartActivity.this, LyricActivity.class);
        intent.putExtra(TITLE_SONG, song.getSongTitle());
        intent.putExtra(LINK_TO_LYRIC, song.getLink());
        startActivity(intent);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
