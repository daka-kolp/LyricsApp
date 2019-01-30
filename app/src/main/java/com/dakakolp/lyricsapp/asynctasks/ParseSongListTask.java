package com.dakakolp.lyricsapp.asynctasks;

import android.text.TextUtils;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongList;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskResult;
import com.dakakolp.lyricsapp.models.Song;
import com.dakakolp.lyricsapp.utils.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseSongListTask extends BaseAsyncTask<SongList> {
    private int mPage;

    public ParseSongListTask(int page, TaskListener<SongList> listener) {
        super(listener);
        mPage = page;
    }

    @Override
    protected TaskResult<SongList> doInBackground(String... strings) {
        if (TextUtils.isEmpty(strings[0])) {
            return new TaskResult<>("Your request is empty...");
        }
        int numberSongs;
        List<Song> songList;
        try {
            Document mainDocument = Jsoup
                    .connect(ParserHelper.SEARCH_LINK + strings[0] + ParserHelper.SEARCH_QUERY + mPage)
                    .header(ParserHelper.USER_AGENT_KEY, ParserHelper.USER_AGENT_VALUE)
                    .get();

            numberSongs = getNumberSongsForListSong(mainDocument);
            songList = getSongsForListSong(mainDocument);
        } catch (IOException e) {
            return new TaskResult<>("Error, check connection...");
        } catch (Exception e) {
            e.printStackTrace();
            return new TaskResult<>("An unknown exception...");
        }
        if (songList.isEmpty()) {
            return new TaskResult<>("Sorry, your search returned no results...");
        }
        return new TaskResult<>(new SongList(songList, numberSongs));
    }

    private int getNumberSongsForListSong(Document mainDocument) {
        Elements elemPanelHeading = mainDocument.getElementsByClass("panel-heading");
        if (!elemPanelHeading.isEmpty()) {
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