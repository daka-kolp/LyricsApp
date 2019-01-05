package com.dakakolp.lyricsapp.asynctasks;

import android.os.AsyncTask;

import com.dakakolp.lyricsapp.models.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseSongTask extends AsyncTask <String, Void, List<Song>>{
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";
    private static final String USER_AGENT = "User-agent";
    private static final String LINK_SEARCH = "https://search.azlyrics.com/search.php?q=";// + str query for song
    private static final String SEARCH_SONGS = "&w=songs&p="; // + number of page

    private int mPage;

    public ParseSongTask(int page) {
        mPage = page;
    }

    @Override
    protected List<Song> doInBackground(String... strings) {
        List<Song> songs = new ArrayList<>();
        try {
            Document mainDocument = Jsoup
                    .connect(LINK_SEARCH + strings[0] + SEARCH_SONGS + mPage)
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();
            Elements elements = mainDocument.getElementsByClass("text-left visitedlyr");
            for (Element element : elements) {
                List<String> songInfo = element.select("b").eachText();
                Song song = new Song();
                song.setSongTitle(songInfo.get(0));
                song.setSinger(songInfo.get(1));
                song.setLink(element.select("a[href]").first().attr("href"));
                songs.add(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }
}
