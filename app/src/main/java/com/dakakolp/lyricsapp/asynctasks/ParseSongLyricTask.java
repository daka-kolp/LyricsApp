package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.SongLyric;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.utils.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseSongLyricTask extends BaseAsyncTask<SongLyric> {

    public ParseSongLyricTask(TaskListener<SongLyric> listener) {
        super(listener);
    }

    @Override
    protected TaskRequest<SongLyric> doInBackground(String... strings) {
        String textSong;
        try {
            Document document = Jsoup
                    .connect(strings[0])
                    .header(ParserHelper.USER_AGENT_KEY, ParserHelper.USER_AGENT_VALUE)
                    .get();

            textSong = getLyric(document);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new TaskRequest<>("Error, check connection...");
        } catch (Exception e) {
            return new TaskRequest<>("An unknown exception...");
        }
        return new TaskRequest<>(new SongLyric(textSong));
    }

    private String getLyric(Document document) {
        Elements elements = document.getElementsByClass("col-xs-12 col-lg-8 text-center");
        int indexOfLyric = 7;
        if (elements.get(0).children().get(indexOfLyric).html().isEmpty()) {
            indexOfLyric = 9;
        }
        return elements.get(0)
                .children().get(indexOfLyric)
                .html().replaceAll("<br>", "")
                .replace("<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->", "");
    }
}
