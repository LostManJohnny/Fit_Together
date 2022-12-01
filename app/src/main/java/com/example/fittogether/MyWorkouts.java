package com.example.fittogether;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fittogether.Adapters.WorkoutAdapter;
import com.example.fittogether.Api.Exceptions.IllegalExerciseTypeException;
import com.example.fittogether.Api.User;
import com.example.fittogether.Models.Class.Exercise;
import com.example.fittogether.Models.Class.Workout;
import com.example.fittogether.Models.Class.WorkoutPreview;
import com.example.fittogether.Models.Enums.ExerciseType;
import com.example.fittogether.databinding.ActivityMyWorkoutsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class MyWorkouts extends AppCompatActivity {

    private static final Object TAG = "ActivityMyWorkouts";
    // Firebase auth
    FirebaseAuth mAuth;
    FirebaseFirestore store;
    FirebaseUser currentUser;

    // View binding
    ActivityMyWorkoutsBinding binding;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyWorkoutsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Create LayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        // Set the LayoutManager for the recyclerview
        binding.rvcMyWorkouts.setLayoutManager(llm);

        // List of workout previews for list
        ArrayList<WorkoutPreview> previews = new ArrayList<>();
        // Create WorkoutAdapter
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(this, previews);
        // Set the WorkoutAdapter to the recycler view to inflate the items
        binding.rvcMyWorkouts.setAdapter(workoutAdapter);

        if(currentUser != null) {
            String userEmail = currentUser.getEmail();

            if (userEmail != null) {
                // Get the users workouts
                User.getUser(userEmail)
                        .addOnSuccessListener(task -> {
                            Toast.makeText(this, "Successfully retrieved workouts", Toast.LENGTH_SHORT).show();

                            try {
                                // Get query results
                                Map<String, Object> results = task.getData();

                                if (results != null) {
                                    // Get personal workouts
                                    JSONObject personal_workouts = getPersonalWorkouts(results);
                                    Iterator<String> keys = personal_workouts.keys();

                                    while(keys.hasNext()){
                                        String key = keys.next();
                                        JSONObject workout = personal_workouts.getJSONObject(key);
                                        previews.add(
                                                new WorkoutPreview(
                                                        workout.getString("title"),
                                                        workout.getInt("size"))
                                        );

                                        workoutAdapter.notifyDataSetChanged();
                                    }
                                    Log.d(TAG + ":valid-results", "Results are " + results);

                                } else {
                                    Log.d(TAG + ":results-error", "Results are null");
                                }

                            } catch (JSONException e) {
                                Log.d(TAG + ":json()", "Error parsing JSON", e);
                                e.printStackTrace();
                            } catch (Exception e) {
                                Log.d(TAG + ":results-error", "Error parsing workouts - ", e);
                                Log.d(TAG + ":stack-trace", Arrays.toString(e.getStackTrace()));
                                e.printStackTrace();
                            }


                        })
                        .addOnFailureListener(task -> Toast.makeText(this, "Error getting workouts", Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show();
        }

    }

    @NonNull
    private ArrayList<Exercise> getExercisesFromWorkout(@NonNull JSONObject workout) throws JSONException, IllegalExerciseTypeException {
        // List of exercises to be returned
        ArrayList<Exercise> exercises = new ArrayList<>();

        // Parse out exercises for ith workout
        JSONArray exerciseList = workout.getJSONArray("exercises");

        // For-Each exercise in ActivityWorkout
        for (int i = 0; i < exerciseList.length(); i++) {
            // Get jth exercise from ith workout
            JSONObject curr_exercise = exerciseList.getJSONObject(i);
            JSONObject exerciseDetails = curr_exercise.getJSONObject("exercise");

            // Add exercise to list based on type
            ExerciseType type = Workout.getExerciseType(curr_exercise.getString("type"));
            Exercise exercise = null;
            switch(type){
                case REP_ONLY:

                    break;
                case REP_TIME:

                    break;
                case TIME_ONLY:

                    break;
                case REP_WEIGHT:

                    break;
                case ONE_REP_MAX:

                    break;
                default:
                    throw new IllegalExerciseTypeException("Invalid exercise type (" + type + ") was used");
            }
            if(exercise != null)
                exercises.add(exercise);

        }

        return exercises;
    }

    private JSONObject getPersonalWorkouts(Map<String, Object> results) throws JSONException {
        // Convert results to JSON object
        JSONObject results_json = new JSONObject(results);
        // Parse out workouts
        JSONObject workouts_json = results_json.getJSONObject("workouts");
        // Parse out personal workouts
        return workouts_json.getJSONObject("personal");
    }
}