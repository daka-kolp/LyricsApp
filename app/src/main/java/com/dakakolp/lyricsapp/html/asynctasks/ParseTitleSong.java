package com.dakakolp.lyricsapp.html.asynctasks;

import android.os.AsyncTask;

import com.dakakolp.lyricsapp.html.modelshttp.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseTitleSong extends AsyncTask<Void, Void, List<Song>> {
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";
    private static final String USER_AGENT = "User-agent";
    @Override
    protected List<Song> doInBackground(Void... voids) {
        List<Song> songs = new ArrayList<>();

        try {
            Document mainDocument = Jsoup
                    .connect("https://www.azlyrics.com")
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();

            Elements elements;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return songs;
    }
}
