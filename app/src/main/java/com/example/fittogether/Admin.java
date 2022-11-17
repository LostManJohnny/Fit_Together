package com.example.fittogether;

import com.example.fittogether.Testing.FirestoreSeed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.fittogether.databinding.ActivityAdminBinding;
import com.example.fittogether.databinding.ActivityLoginBinding;

public class Admin extends AppCompatActivity {

    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSeed.setOnClickListener(view -> {
            FirestoreSeed.seedFirestore(Admin.this);

        });
    }
}