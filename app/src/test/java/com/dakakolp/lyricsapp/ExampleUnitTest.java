package com.dakakolp.lyricsapp;

import com.dakakolp.lyricsapp.asynctasks.asyncmodels.ListSong;
import com.dakakolp.lyricsapp.models.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void linkSongs(){

        String USER_AGENT = "User-agent";
        String LINK_SEARCH = "https://search.azlyrics.com/search.php?q=";// + str query for song
        String SEARCH_SONGS = "&w=songs&p="; // + number of page

        int page = 1;
        String string = "green";

        List<Song> songs = new ArrayList<>();
        try {
            Document mainDocument = Jsoup
                    .connect(LINK_SEARCH + string + SEARCH_SONGS + page)
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();
            Elements elements = mainDocument.getElementsByClass("text-left visitedlyr");
            for (Element element : elements) {
                List<String> songInfo = element.select("b").eachText();
                Song song = new Song();
                song.setSinger(songInfo.get(1));
                song.setSongTitle(songInfo.get(0));
                song.setLink(element.select("a[href]").first().attr("href"));
                System.out.println(song.getSinger() + " " + song.getSongTitle() + " " + song.getLink() + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lyricSong(){
        String textSong = null;
        try {
            Document document = Jsoup
                    .connect("https://www.azlyrics.com/lyrics/selenagomez/goodforyou.html")
                    .header("User-agent", USER_AGENT_STRING)
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
            System.out.println(textSong);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void totalNumberSongs(){

        String USER_AGENT = "User-agent";
        String LINK_SEARCH = "https://search.azlyrics.com/search.php?q=";// + str query for song
        String SEARCH_SONGS = "&w=songs&p="; // + number of page

        int page = 1;
        String string = "On My Own Three Days Grace";

        try {
            Document mainDocument = Jsoup
                    .connect(LINK_SEARCH + string + SEARCH_SONGS + page)
                    .header(USER_AGENT, USER_AGENT_STRING)
                    .get();
            Elements elements = mainDocument.getElementsByClass("panel-heading");
            String str = null;
            if(!elements.isEmpty()) {
                Element element = elements.first();
                str = element.select("small")
                        .first()
                        .text();
                int startIndex = str.indexOf("of");
                int endIndex = str.indexOf("total");
                if (startIndex != -1 && endIndex != -1) {
                    String resultCount = str.substring(startIndex + 2, endIndex);
                    int count = Integer.parseInt(resultCount.trim());
                    if (count > 0) {
                        System.out.println(count);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void f(){
        System.out.println(11%20);
    }

}