package com.dakakolp.lyricsapp.asynctasks.asynclisteners;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;

public interface TaskListener<T> {
    void showProgress();
    void hideProgress();
    void onFinalResult(TaskRequest<T> t);
}
