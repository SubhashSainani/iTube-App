package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itubeapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireActivity());

        binding.loginButton.setOnClickListener(v -> loginUser());
        binding.signupLink.setOnClickListener(v -> navigateToSignUp());
    }

    private void loginUser() {
        String username = binding.usernameEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValid = dbHelper.checkUser(username, password);
        if (isValid) {
            int userId = dbHelper.getUserId(username);
            ((MainActivity) requireActivity()).setCurrentUserId(userId);
            ((MainActivity) requireActivity()).setCurrentUsername(username);
            ((MainActivity) requireActivity()).updateNavigation(true, username);
            ((MainActivity) requireActivity()).loadFragment(new HomeFragment(), false);
        } else {
            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSignUp() {
        ((MainActivity) requireActivity()).loadFragment(new SignUpFragment(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}