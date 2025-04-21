package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itubeapp.databinding.FragmentPlaylistBinding;

import java.util.List;

public class PlaylistFragment extends Fragment {
    private FragmentPlaylistBinding binding;
    private DatabaseHelper dbHelper;
    private PlaylistAdapter adapter;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireActivity());

        binding.playlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadPlaylist();
    }

    private void loadPlaylist() {
        int userId = ((MainActivity) requireActivity()).getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        List<VideoItem> playlist = dbHelper.getPlaylist(userId);
        if (playlist.isEmpty()) {
            Toast.makeText(getActivity(), "Your playlist is empty", Toast.LENGTH_SHORT).show();
        }

        adapter = new PlaylistAdapter(playlist, video -> {
            ((MainActivity) requireActivity()).loadFragment(HomeFragment.newInstance(video.getVideoUrl()), true);
        });
        binding.playlistRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}