package com.dakakolp.lyricsapp.asynctasks.asynclisteners;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;

public interface TaskListener<R extends TaskResult> {
    void showProgress();
    void hideProgress();
    void onFinalResult(TaskRequest<R> request);
}
