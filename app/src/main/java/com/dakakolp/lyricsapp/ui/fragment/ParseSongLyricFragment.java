package com.dakakolp.lyricsapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dakakolp.lyricsapp.asynctasks.ParseSongLyricTask;
import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.ui.fragment.callbacks.AsyncTaskFragmentCallback;

public class ParseSongLyricFragment extends Fragment implements TaskListener<SongLyric> {
    private static final String LYRIC_KEY = "lyric key";

    protected AsyncTaskFragmentCallback mCallback;
    private String mTextSong;
    protected boolean mIsLoading;

    public static ParseSongLyricFragment newInstance(String textSong) {
        ParseSongLyricFragment fragment = new ParseSongLyricFragment();
        Bundle args = new Bundle();
        args.putString(LYRIC_KEY, textSong);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallback.updateFragmentProgress(mIsLoading);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mTextSong = args.getString(LYRIC_KEY);
        }
        setRetainInstance(true);
        execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AsyncTaskFragmentCallback) {
            mCallback = (AsyncTaskFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void showProgress() {
        mIsLoading = true;
        mCallback.showFragmentProgress();
    }

    @Override
    public void hideProgress() {
        mIsLoading = false;
        mCallback.hideFragmentProgress();
    }

    @Override
    public void onFinalResult(TaskRequest<SongLyric> request) {
        mCallback.onFragmentFinalResult(request);
    }

    protected void execute() {
        new ParseSongLyricTask(this).execute(mTextSong);
    }

}
