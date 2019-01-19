package com.dakakolp.lyricsapp.ui.fragment.callbacks;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;

public interface AsyncTaskFragmentCallback {

    void updateFragmentProgress(boolean isLoading);
    void showFragmentProgress();
    void hideFragmentProgress();
    void onFragmentFinalResult(TaskRequest<SongLyric> request);
}
