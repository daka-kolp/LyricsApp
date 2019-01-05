package com.dakakolp.lyricsapp.asynctasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseLyricTask extends AsyncTask<String, Void, String> {
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";
    private static final String USER_AGENT = "User-agent";

    @Override
    protected String doInBackground(String... strings) {
        String textSong = null;
        try {
            Document document = Jsoup
                    .connect(strings[0])
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();

            Elements elements = document.getElementsByClass("col-xs-12 col-lg-8 text-center");
            textSong = elements.get(0)
                    .children().get(7)
                    .html().replaceAll("<br>", "")
                    .replace("<!-- Usage of azlyrics.com content by any third-party lyrics provider is prohibited by our licensing agreement. Sorry about that. -->", "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return textSong;
    }
}
