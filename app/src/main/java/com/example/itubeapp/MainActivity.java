package com.example.itubeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private int currentUserId = -1;
    private String currentUsername = "";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Check if user is logged in, if not show login fragment
        if (currentUserId == -1) {
            loadFragment(new LoginFragment(), false);
        } else {
            loadFragment(new HomeFragment(), false);
            updateNavigation(true, currentUsername);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (id == R.id.nav_playlist) {
                    selectedFragment = PlaylistFragment.newInstance();
                } else if (id == R.id.nav_logout) {
                    logoutUser();
                    return true;
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, true);
                }
                return true;
            };

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Add animation if desired
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );

        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void updateNavigation(boolean isLoggedIn, String username) {
        TextView welcomeText = findViewById(R.id.welcomeText);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (isLoggedIn) {
            welcomeText.setText("Welcome, " + username);
            welcomeText.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            welcomeText.setVisibility(View.GONE);
            bottomNav.setVisibility(View.GONE);
        }
    }

    private void logoutUser() {
        currentUserId = -1;
        currentUsername = "";
        updateNavigation(false, "");
        loadFragment(new LoginFragment(), false);
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}