package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.utils.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseSongTask extends BaseAsyncTask<List<Song>> {
    private int mPage;

    public ParseSongTask(int page, TaskListener<List<Song>> listener) {
        super(listener);
        mPage = page;
    }

    @Override
    protected TaskRequest<List<Song>> doInBackground(String... strings) {
        TaskRequest<List<Song>> request = new TaskRequest<>();
        List<Song> songs = new ArrayList<>();
        try {
            Document mainDocument = Jsoup
                    .connect(ParserHelper.LINK_SEARCH + strings[0] + ParserHelper.SEARCH_SONGS + mPage)
                    .header(ParserHelper.USER_AGENT, ParserHelper.USER_AGENT_STRING)
                    .get();

            Elements elements = mainDocument.getElementsByClass("text-left visitedlyr");
            for (Element element : elements) {
                List<String> songInfo = element.select("b").eachText();
                Song song = new Song();
                song.setSongTitle(songInfo.get(0));
                if (songInfo.size() == 2)
                    song.setSinger(songInfo.get(1));
                else
                    song.setSinger("An unknown singer");
                song.setLink(element.select("a[href]").first().attr("href"));
                songs.add(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
            request.setError("Error, check connection...");
            return request;
        } catch (Exception e) {
            e.printStackTrace();
            request.setError("An unknown exception...");
            return request;
        }
        if (songs.isEmpty()) {
            request.setError("Sorry, your search returned no results...");
            return request;
        }
        request.setResult(songs);
        return request;
    }
}