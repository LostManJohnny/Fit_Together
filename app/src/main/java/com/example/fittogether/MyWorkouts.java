package com.example.fittogether;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fittogether.Adapters.WorkoutAdapter;
import com.example.fittogether.Api.User;
import com.example.fittogether.Models.Class.Workout;
import com.example.fittogether.databinding.ActivityMyWorkoutsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class MyWorkouts extends AppCompatActivity {

    private static final Object TAG = "";
    // Firebase auth
    FirebaseAuth mAuth;
    FirebaseFirestore store;
    FirebaseUser currentUser;

    // View binding
    ActivityMyWorkoutsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyWorkoutsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        String userEmail = currentUser.getEmail();
        if(userEmail != null) {
            // Get the users workouts
            User.getUser(userEmail)
                    .addOnSuccessListener(task -> {
                        Toast.makeText(this, "Successfully retrieved workouts", Toast.LENGTH_SHORT).show();

                        Map<String, Object> results = null;
                        try {
                            results = task.getData();


                            Log.i(TAG + "results", results.toString());
                        } catch (Exception e) {
                            Toast.makeText(this, "Error parsing results", Toast.LENGTH_SHORT).show();
                            Log.d(TAG + "results-error", String.valueOf(results));
                            e.printStackTrace();
                        }

                        // Set the LayoutManager for the recyclerview
                        binding.rvcMyWorkouts.setLayoutManager(new LinearLayoutManager(this));

                        // Adapter class is initialized and list is passed through
                        WorkoutAdapter workoutAdapter = new WorkoutAdapter(this, new ArrayList<>());

                        // Adapter is set to the recycler view to inflate the items
                        binding.rvcMyWorkouts.setAdapter(workoutAdapter);


                    })
                    .addOnFailureListener(task -> Toast.makeText(this, "Error getting workouts", Toast.LENGTH_SHORT).show());
        }
        else{
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
    }
}