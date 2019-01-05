package com.dakakolp.lyricsapp.html.asynctasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.dakakolp.lyricsapp.html.modelshttp.Album;
import com.dakakolp.lyricsapp.ui.adapters.ListSongAdapter;
import com.dakakolp.lyricsapp.ui.adapters.listeners.OnClickSongListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseAlbumAsyncTask extends AsyncTask<Void, Void, List<Album>> {
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";
    private static final String USER_AGENT = "User-agent";
    private static final String RESOURCE = "https://www.azlyrics.com";

    @SuppressLint("StaticFieldLeak")
    private RecyclerView mRecyclerView;
    private OnClickSongListener mOnClickSongListener;

    public ParseAlbumAsyncTask(RecyclerView recyclerView, OnClickSongListener listener) {
        mRecyclerView = recyclerView;
        mOnClickSongListener = listener;
    }

    @Override
    protected List<Album> doInBackground(Void... voids) {
        List<Album> songs = new ArrayList<>();
        try {
            Document mainDocument = Jsoup
                    .connect(RESOURCE)
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();

            Elements elements = mainDocument.getElementsByClass("col-xs-6");
            for (Element element : elements) {
                String[] songInfo = element.text().split("\"");
                Album song = new Album();
                song.setSinger(songInfo[0].trim());
                song.setSongTitle(songInfo[1]);
                song.setImage(RESOURCE + element.select("img[src]").first().attr("src"));
                song.setLink(RESOURCE + element.select("a[href]").first().attr("href"));
                songs.add(song);
            }

            System.out.println(songs);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return songs;
    }

    @Override
    protected void onPostExecute(List<Album> songs) {
        ListSongAdapter listSongAdapter = new ListSongAdapter(songs, mOnClickSongListener);
        mRecyclerView.setAdapter(listSongAdapter);
    }
}
