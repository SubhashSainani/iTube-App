package com.example.itubeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<VideoItem> playlist;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(VideoItem video);
    }

    public PlaylistAdapter(List<VideoItem> playlist, OnItemClickListener listener) {
        this.playlist = playlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        VideoItem item = playlist.get(position);
        holder.videoTitle.setText(item.getVideoTitle());
        holder.videoUrl.setText(item.getVideoUrl());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        TextView videoUrl;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitleTextView);
            videoUrl = itemView.findViewById(R.id.videoUrlTextView);
        }
    }
}