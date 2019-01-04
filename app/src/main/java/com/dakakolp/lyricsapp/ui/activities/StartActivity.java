package com.dakakolp.lyricsapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;
import com.dakakolp.lyricsapp.ui.adapters.listeners.OnClickSongListener;

public class StartActivity extends AppCompatActivity implements OnClickSongListener {

    private RecyclerView mRecyclerViewSongs;
    private ListSongAdapter mListSongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerViewSongs = findViewById(R.id.recycler_view_songs);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewSongs.setLayoutManager(gridLayoutManager);

        mListSongAdapter = new ListSongAdapter(null, this);
        mRecyclerViewSongs.setAdapter(mListSongAdapter);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClickSong(int position) {

    }
}
