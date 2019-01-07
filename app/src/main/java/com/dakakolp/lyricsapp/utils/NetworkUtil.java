package com.dakakolp.lyricsapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static final String USER_AGENT_STRING = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4)" +
            " AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12";
    public static final String USER_AGENT = "User-agent";
    public static final String LINK_SEARCH = "https://search.azlyrics.com/search.php?q=";// + str query for song
    public static final String SEARCH_SONGS = "&w=songs&p=";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
