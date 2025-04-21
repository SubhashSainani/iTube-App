package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itubeapp.databinding.FragmentSignupBinding;

public class SignUpFragment extends Fragment {
    private FragmentSignupBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireActivity());

        binding.createAccountButton.setOnClickListener(v -> createAccount());
        binding.loginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void createAccount() {
        String fullName = binding.fullNameEditText.getText().toString().trim();
        String username = binding.usernameEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = dbHelper.addUser(fullName, username, password);
        if (isSuccess) {
            Toast.makeText(getActivity(), "Account created successfully", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        } else {
            Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        ((MainActivity) requireActivity()).loadFragment(new LoginFragment(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}