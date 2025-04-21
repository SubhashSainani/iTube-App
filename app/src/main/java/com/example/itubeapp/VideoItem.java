package com.example.itubeapp;

public class VideoItem {
    private int playlistId;
    private String videoUrl;
    private String videoTitle;

    public VideoItem(int playlistId, String videoUrl, String videoTitle) {
        this.playlistId = playlistId;
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}