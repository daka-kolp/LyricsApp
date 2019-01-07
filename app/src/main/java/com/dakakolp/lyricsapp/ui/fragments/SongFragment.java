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
import android.widget.Toast;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.asynctasks.ParseSongTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;

import java.util.List;

public class SongFragment extends Fragment implements
        TaskListener<List<Song>>,
        ListSongAdapter.OnClickSongListener {
    private static final String PAGE_NUMBER = "page number";
    private static final String SEARCH_SONG = "search song";

    private int mPageNumber;
    private String mSearchSong;

    private List<Song> mSongs;

    private Context mContext;
    private RecyclerView mRecyclerView;

    private OnSongListFragmentInteractionListener mListener;

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
        new ParseSongTask(mPageNumber, this).execute(mSearchSong);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewContainer = inflater.inflate(R.layout.fragment_song, container, false);

        mRecyclerView = viewContainer.findViewById(R.id.recycler_view_songs);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(gridLayoutManager);

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

    //  implementation TaskListener
    @Override
    public void initProgressBar() {
        mListener.showProgressBar();
    }

    @Override
    public void getFinalResult(TaskRequest<List<Song>> songs) {
        mListener.hideProgressBar();
        if (songs.getError() != null) {
            Toast.makeText(mContext, songs.getError(), Toast.LENGTH_SHORT).show();
            return;
        }
        mSongs = songs.getResult();
        ListSongAdapter adapter = new ListSongAdapter(mSongs, this);
        mRecyclerView.setAdapter(adapter);
    }


    //  implementation ListSongAdapter.OnClickSongListener
    @Override
    public void onClickSong(int position) {
        if (mListener != null)
            mListener.onOpenLyric(mSongs.get(position));
    }

    public interface OnSongListFragmentInteractionListener {
        void onOpenLyric(Song song);

        void showProgressBar();

        void hideProgressBar();
    }
}