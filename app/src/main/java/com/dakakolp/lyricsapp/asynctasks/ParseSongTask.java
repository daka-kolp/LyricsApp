package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.ParseListener;
import com.dakakolp.lyricsapp.asynctasks.resultmodels.ParseResult;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.utils.NetworkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ParseSongTask extends BaseAsyncTask<ParseResult<List<Song>>> {
    private int mPage;

    public ParseSongTask(int page, ParseListener<ParseResult<List<Song>>> listener) {
        super(listener);
        mPage = page;
    }

    @Override
    protected ParseResult<List<Song>> doInBackground(String... strings) {
        ParseResult <List<Song>> result = new ParseResult<>();
        List<Song> songs = new ArrayList<>();
        try {
            Document mainDocument = Jsoup
                    .connect(NetworkUtil.LINK_SEARCH + strings[0] + NetworkUtil.SEARCH_SONGS + mPage)
                    .header(NetworkUtil.USER_AGENT, NetworkUtil.USER_AGENT_STRING)
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
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(e.getLocalizedMessage());
        }
        result.setResult(songs);
        return result;
    }
}
