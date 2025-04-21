package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itubeapp.databinding.FragmentHomeBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private YouTubePlayer youTubePlayer;
    private String currentVideoId = "";
    private DatabaseHelper dbHelper;

    private static final String ARG_VIDEO_URL = "video_url";

    public static HomeFragment newInstance(String videoUrl) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireActivity());

        if (getArguments() != null && getArguments().containsKey(ARG_VIDEO_URL)) {
            String url = getArguments().getString(ARG_VIDEO_URL);
            currentVideoId = extractVideoId(url);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set URL from arguments if available
        if (getArguments() != null && getArguments().containsKey(ARG_VIDEO_URL)) {
            String url = getArguments().getString(ARG_VIDEO_URL);
            binding.youtubeUrlEditText.setText(url);
        }

        // Initialize YouTube Player
        getLifecycle().addObserver(binding.youtubePlayerView);
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                if (!currentVideoId.isEmpty()) {
                    player.cueVideo(currentVideoId, 0);
                }
            }
        });

        binding.playButton.setOnClickListener(v -> playVideo());
        binding.addToPlaylistButton.setOnClickListener(v -> addToPlaylist());
        binding.myPlaylistButton.setOnClickListener(v -> navigateToPlaylist());
    }

    private void playVideo() {
        String url = binding.youtubeUrlEditText.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        String videoId = extractVideoId(url);
        if (videoId == null) {
            Toast.makeText(getActivity(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // Launch PlayerFragment with the video
        ((MainActivity) requireActivity()).loadFragment(PlayerFragment.newInstance(url), true);
    }

    private String extractVideoId(String url) {
        String videoId = null;
        if (url.contains("v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            int ampersandPosition = videoId.indexOf('&');
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.indexOf("youtu.be/") + 9);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
        }
        return videoId;
    }

    private void addToPlaylist() {
        String url = binding.youtubeUrlEditText.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        String videoId = extractVideoId(url);
        if (videoId == null) {
            Toast.makeText(getActivity(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = ((MainActivity) requireActivity()).getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String videoTitle = "YouTube Video: " + videoId;
        boolean success = dbHelper.addToPlaylist(userId, url, videoTitle);
        if (success) {
            Toast.makeText(getActivity(), "Added to playlist", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to add to playlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToPlaylist() {
        int userId = ((MainActivity) requireActivity()).getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        ((MainActivity) requireActivity()).loadFragment(PlaylistFragment.newInstance(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.youtubePlayerView.release();
        binding = null;
    }
}