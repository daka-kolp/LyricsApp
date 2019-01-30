package com.dakakolp.lyricsapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AsyncTaskService extends Service {
    public AsyncTaskService() {
    }

    class AsyncTaskBinder extends Binder{
        AsyncTaskService getService(){
            return AsyncTaskService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }



}
