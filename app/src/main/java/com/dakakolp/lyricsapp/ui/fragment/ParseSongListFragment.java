package com.dakakolp.lyricsapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class ParseSongListFragment extends Fragment {

    private static final String PAGE_KEY = "page key";
    private static final String SEARCH_STRING_KEY = "search_string key";

    private int mPage;
    private String mSearchString;

    public static ParseSongListFragment newInstance(int page, String searchString) {
        ParseSongListFragment fragment = new ParseSongListFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_KEY, page);
        args.putString(SEARCH_STRING_KEY, searchString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPage = args.getInt(PAGE_KEY);
            mSearchString = args.getString(SEARCH_STRING_KEY);
        }
    }

}
