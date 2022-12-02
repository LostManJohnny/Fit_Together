package com.example.fittogether;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fittogether.Adapters.ExerciseAdapter;
import com.example.fittogether.Api.Exceptions.IllegalExerciseTypeException;
import com.example.fittogether.Models.Class.Exercise;
import com.example.fittogether.Models.Class.Timer;
import com.example.fittogether.Models.Enums.ExerciseType;
import com.example.fittogether.databinding.ActivityWorkoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class ActivityWorkout extends AppCompatActivity {

    private static final String TAG = "ActivityWorkout";
    ActivityWorkoutBinding binding;
    ArrayList<Exercise> exerciseList;
    FirebaseFirestore store;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        store = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        binding.btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyWorkouts.class);
            startActivity(intent);
            finish();
        });

        binding.btnSave.setOnClickListener(v -> {
            // TODO: Update Firestore with new values
        });

        binding.btnAddExercise.setOnClickListener(v -> {
            // TODO:
        });

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("name");
        Log.i(TAG + ":title", "Title => " + title);

        if (title != null && !title.equals("")) {
            binding.tvWorkoutTitle.setText(title);

        } else {
            Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show();
            finish();
        }

        exerciseList = new ArrayList<>();

        // Create Layout Management
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvcExercises.setLayoutManager(llm);

        // Create ExerciseAdapter
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this, exerciseList);
        binding.rvcExercises.setAdapter(exerciseAdapter);

        String email = currentUser.getEmail();
        if (email != null) {
            store
                    .collection("users")
                    .document(email)
                    .get()
                    .addOnSuccessListener(task -> {
                        try {
                            Map<String, Object> results = task.getData();

                            Log.i(TAG + ":getUser", "results -> " + results);

                            Gson gson = new Gson();
                            String json_string = gson.toJson(results, Map.class);
                            Log.i(TAG + ":getUser", "json_string -> " + json_string);

                            JsonObject user = gson.fromJson(json_string, JsonObject.class);

                            Log.i(TAG + ":getUser", "json_object -> " + user);
                            Log.i(TAG + ":workouts:title", title);

                            JsonObject workouts_all = user.get("workouts").getAsJsonObject();
                            Log.i(TAG + ":workouts:all", workouts_all.toString());
                            JsonObject workout_personal = workouts_all.get("personal").getAsJsonObject();
                            Log.i(TAG + ":workouts:personal", workout_personal.toString());

                            if(workout_personal.has(title)) {
                                JsonObject current_workout = workout_personal.get(title).getAsJsonObject();

                                // If workout is successfully retrieved
                                if (current_workout != null) {
                                    Log.i(TAG + ":workouts:current", current_workout.toString());
                                    // Retrieve the exercises
                                    JsonArray current_exercises = current_workout.get("exercises").getAsJsonArray();

                                    String weight_unit = user.get("stored_unit").getAsString();

                                    // Iterate over all the exercises
                                    for (int j = 0; j < current_exercises.size(); j++) {
                                        // Get current exercise
                                        JsonObject current_exercise = current_exercises.get(j).getAsJsonObject();

                                        String current_type = current_exercise.get("type").getAsString();

                                        Log.i(TAG + ":getUser:current_exercise", "curr_ex -> " + current_exercise);
                                        Log.i(TAG + ":getUser:current_exercise:Type", "Type -> " + current_type);

                                        // Create new exercise object
                                        String current_name = current_exercise.get("name").getAsString();
                                        Exercise new_ex = new Exercise(current_name);

                                        new_ex.setSetList(current_exercise.get("sets").getAsJsonArray());
                                        try {
                                            new_ex.setType(current_type);
                                        } catch (IllegalArgumentException e) {
                                            e.printStackTrace();
                                        }

                                        exerciseList.add(new_ex);
                                        exerciseAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(this, "There was an error retrieving the workout (" + title + ")", Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                Toast.makeText(this, title + " - is an invalid key", Toast.LENGTH_SHORT).show();
                                Log.i(TAG + ":title", title + " - is an invalid key");
                                Log.i(TAG + ":personal_workouts", workout_personal.toString());
                                Log.i(TAG + ":keys", workout_personal.keySet().toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG + ":getUser", "Error retrieving exercises for workout " + title);
                        }
                    });
        } else {
            Toast.makeText(this, "Email is null", Toast.LENGTH_SHORT).show();
        }
    }
}