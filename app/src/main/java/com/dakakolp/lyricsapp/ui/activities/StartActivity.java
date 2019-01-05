package com.dakakolp.lyricsapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.ui.fragments.SongFragment;

public class StartActivity extends AppCompatActivity implements SongFragment.OnSongListFragmentInteractionListener {

    public static final String LINK_TO_LYRIC = "link to lyric";
    public static final String TITLE_SONG = "title song";

    private int mPage = 1;

    private EditText mEditTextSearch;
    private ImageButton mImageButtonSearch;
    private ImageButton mImageButtonBefore;
    private ImageButton mImageButtonNext;

    private String searchString;


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

        mImageButtonBefore.setVisibility(View.GONE);
        mImageButtonNext.setVisibility(View.GONE);

        mImageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mImageButtonNext.setVisibility(View.VISIBLE);
                searchString = mEditTextSearch.getText().toString();
                SongFragment fragment = SongFragment.newInstance(1, searchString);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.song_list_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mImageButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mPage != 1) {
//                    mPage--;
//                    mImageButtonBefore.setVisibility(View.VISIBLE);
//                } else {
//                    mImageButtonBefore.setVisibility(View.GONE);
//                }
                SongFragment fragment = SongFragment.newInstance(mPage, searchString);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.song_list_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mPage >= 1 && mPage <= 50) {
//                    mPage++;
//                    mImageButtonNext.setVisibility(View.VISIBLE);
//                } else {
//                    mImageButtonNext.setVisibility(View.GONE);
//                }
//                if (mPage > 1){
//                    mImageButtonBefore.setVisibility(View.VISIBLE);
//                } else {
//                    mImageButtonBefore.setVisibility(View.GONE);
//                }
                SongFragment fragment = SongFragment.newInstance(mPage, searchString);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.song_list_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onOpenLyric(Song song) {
        Intent intent = new Intent(StartActivity.this, LyricActivity.class);
        intent.putExtra(TITLE_SONG, song.getSongTitle());
        intent.putExtra(LINK_TO_LYRIC, song.getLink());
        startActivity(intent);
    }
}
