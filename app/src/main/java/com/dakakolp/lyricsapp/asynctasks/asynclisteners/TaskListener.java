package com.dakakolp.lyricsapp.asynctasks.asynclisteners;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;

public interface TaskListener<T> {
    void initProgressBar();
    void getFinalResult(TaskRequest<T> t);
}
