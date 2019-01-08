package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.ListSong;
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

public class ParseSongTask extends BaseAsyncTask<ListSong> {
    private int mPage;

    public ParseSongTask(int page, TaskListener<ListSong> listener) {
        super(listener);
        mPage = page;
    }

    @Override
    protected TaskRequest<ListSong> doInBackground(String... strings) {
        TaskRequest<ListSong> request = new TaskRequest<>();
        ListSong songs = new ListSong();

        try {
            Document mainDocument = Jsoup
                    .connect(ParserHelper.SEARCH_LINK + strings[0] + ParserHelper.SEARCH_QUERY + mPage)
                    .header(ParserHelper.USER_AGENT_KEY, ParserHelper.USER_AGENT_VALUE)
                    .get();

            songs.setNumberSongs(getNumberSongsForListSong(mainDocument));

            songs.setSongs(getSongsForListSong(mainDocument));

        } catch (IOException e) {
            request.setError("Error, check connection...");
            return request;
        } catch (Exception e) {
            request.setError("An unknown exception...");
            return request;
        }
        if (songs.getSongs().isEmpty()) {
            request.setError("Sorry, your search returned no results...");
            return request;
        }
        request.setResult(songs);
        return request;
    }

    private int getNumberSongsForListSong(Document mainDocument) {
        Elements elemPanelHeading = mainDocument.getElementsByClass("panel-heading");
        if(!elemPanelHeading.isEmpty()) {
            Element element = elemPanelHeading.first();
            String dataListSong = element.select("small")
                    .first()
                    .text();
            int startIndex = dataListSong.indexOf("of");
            int endIndex = dataListSong.indexOf("total");
            if (startIndex != -1 && endIndex != -1) {
                String resultCount = dataListSong.substring(startIndex + 2, endIndex);
                int countOfSongs = Integer.parseInt(resultCount.trim());
                if (countOfSongs > 0) {
                    return countOfSongs;
                }
            }
        }
        return 0;
    }

    private List<Song> getSongsForListSong(Document mainDocument) {
        List<Song> songs = new ArrayList<>();
        Elements elemVisitedLyr = mainDocument.getElementsByClass("text-left visitedlyr");
        for (Element element : elemVisitedLyr) {
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
        return songs;
    }
}