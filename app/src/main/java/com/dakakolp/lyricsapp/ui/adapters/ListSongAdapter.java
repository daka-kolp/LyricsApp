package com.dakakolp.lyricsapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.html.modelshttp.Song;
import com.dakakolp.lyricsapp.ui.adapters.listeners.OnClickSongListener;

import java.util.List;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ListSongViewHolder> {

    private List<Song> mSongList;
    private OnClickSongListener mListener;
    private View mView;

    public ListSongAdapter(List<Song> songList, OnClickSongListener listener) {
        mSongList = songList;
        mListener = listener;
    }

    @NonNull
    @Override
    public ListSongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_list_song, null);
        return new ListSongViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSongViewHolder listSongViewHolder, int i) {

        Song song = mSongList.get(i);

        Glide
                .with(mView)
                .load(song.getImage())
                .into(listSongViewHolder.mImageSong);

        listSongViewHolder.mSinger.setText(song.getSongTitle());
        listSongViewHolder.mSongTitle.setText(song.getSinger());

    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    class ListSongViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageSong;
        private TextView mSinger;
        private TextView mSongTitle;
        private CardView mCardSong;

        public ListSongViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageSong = itemView.findViewById(R.id.song_image);
            mSinger = itemView.findViewById(R.id.singer_name);
            mSongTitle = itemView.findViewById(R.id.song_title);
            mCardSong = itemView.findViewById(R.id.song_card);

            mCardSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickSong(getAdapterPosition());
                }
            });
        }
    }
}
