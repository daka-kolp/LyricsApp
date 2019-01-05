package com.dakakolp.lyricsapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.asynctasks.ParseSongTask;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SongFragment extends Fragment {
    private static final String PAGE_NUMBER = "page number";
    private static final String SEARCH_SONG = "search song";

    private int mPageNumber;
    private String mSearchSong;

    private List<Song> mSongs;

    private Context mContext;

    private OnSongListFragmentInteractionListener mListener;
    private ListSongAdapter.OnClickSongListener mClickSongListener = new ListSongAdapter.OnClickSongListener() {
        @Override
        public void onClickSong(int position) {
            if (mListener != null)
                mListener.onOpenLyric(mSongs.get(position));
        }
    };
    public SongFragment() {
        // Required empty public constructor
    }
    
    public static SongFragment newInstance(int page, String song) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, page);
        args.putString(SEARCH_SONG, song);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageNumber = getArguments().getInt(PAGE_NUMBER);
            mSearchSong = getArguments().getString(SEARCH_SONG);
        }
        ParseSongTask asyncTask = new ParseSongTask(mPageNumber);
        // TODO: handle exceptions
        try {
            mSongs = asyncTask.execute(mSearchSong).get(3, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewContainer = inflater.inflate(R.layout.fragment_song, container, false);

        RecyclerView recyclerViewSongs = viewContainer.findViewById(R.id.recycler_view_songs);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewSongs.setLayoutManager(gridLayoutManager);

        ListSongAdapter songAdapter = new ListSongAdapter(mSongs, mClickSongListener);
        recyclerViewSongs.setAdapter(songAdapter);

        return viewContainer;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnSongListFragmentInteractionListener) {
            mListener = (OnSongListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSongListFragmentInteractionListener {
        void onOpenLyric(Song song);
    }
}
