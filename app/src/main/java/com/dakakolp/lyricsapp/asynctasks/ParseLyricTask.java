package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.TaskListener;
import com.dakakolp.lyricsapp.asynctasks.asyncmodels.TaskRequest;
import com.dakakolp.lyricsapp.utils.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseLyricTask extends BaseAsyncTask<String> {

    public ParseLyricTask(TaskListener<String> listener) {
        super(listener);
    }

    @Override
    protected TaskRequest<String> doInBackground(String... strings) {
        TaskRequest<String> request = new TaskRequest<>();
        String textSong;
        try {
            Document document = Jsoup
                    .connect(strings[0])
                    .header(ParserHelper.USER_AGENT, ParserHelper.USER_AGENT_STRING)
                    .get();

            Elements elements = document.getElementsByClass("col-xs-12 col-lg-8 text-center");
            int indexOfLyric = 7;
            if(elements.get(0).children().get(indexOfLyric).html().isEmpty())
            {
                indexOfLyric = 9;
            }
            textSong = elements.get(0)
                    .children().get(indexOfLyric)
                    .html().replaceAll("<br>", "")
                    .replace("<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->", "");
        } catch (IOException ex) {
            ex.printStackTrace();
            request.setError("Error, check connection...");
            return request;
        } catch (Exception e) {
            request.setError("An unknown error...");
            return request;
        }
        request.setResult(textSong);
        return request;
    }
}
