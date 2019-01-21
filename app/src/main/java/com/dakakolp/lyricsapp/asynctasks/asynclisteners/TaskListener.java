package com.dakakolp.lyricsapp.asynctasks.asynclisteners;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public interface TaskListener<R> {
    void showProgress();
    void hideProgress();
    void onFinalResult(TaskResult<R> request);
}
