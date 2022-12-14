package com.example.fittogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fittogether.databinding.ActivityAddExerciseBinding;

public class ActivityAddExercise extends AppCompatActivity {

    ActivityAddExerciseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddExercise.inflate(getLayoutInflater());

        setContentView(R.layout.activity_add_exercise);
    }
}