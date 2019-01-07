package com.dakakolp.lyricsapp.asynctasks;

import com.dakakolp.lyricsapp.asynctasks.asynclisteners.ParseListener;
import com.dakakolp.lyricsapp.asynctasks.resultmodels.ParseResult;
import com.dakakolp.lyricsapp.utils.NetworkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ParseLyricTask extends BaseAsyncTask<ParseResult<String>> {

    public ParseLyricTask(ParseListener<ParseResult<String>> listener) {
        super(listener);
    }

    @Override
    protected ParseResult<String> doInBackground(String... strings) {
        ParseResult<String> result = new ParseResult<>();
        String textSong = null;
        try {
            Document document = Jsoup
                    .connect(strings[0])
                    .header(NetworkUtil.USER_AGENT, NetworkUtil.USER_AGENT_STRING)
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
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setError(ex.getMessage());
        }
        result.setResult(textSong);
        return result;
    }


}
