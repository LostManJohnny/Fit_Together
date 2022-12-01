package com.example.fittogether.Testing;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Models.Class.ListExercise;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FirestoreSeed {
    private static final String TAG = "FirestoreSeed";

    public static void seedFirestore(AppCompatActivity source){
        Toast.makeText(source.getApplicationContext(), "Firebase Seed Started...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        Log.i(TAG, "Firebase Seed Started...");

        // seedExercises(store, source);

        seedUsers(store, source);

    }

    private static void seedUsers(FirebaseFirestore store, AppCompatActivity source) {
        Log.i(TAG + ":seedUsers", "Starting users seed...");

        try {
            Gson gson = new Gson();

            // Get and parse file
            InputStream is_users = source.getAssets().open("seed_users.json");
            InputStreamReader isr_users = new InputStreamReader(is_users, StandardCharsets.UTF_8);

            StringBuilder user_string = new StringBuilder();
            Scanner s = new Scanner(isr_users);
            while(s.hasNext()){
                user_string.append(s.next());
            }
            Log.i(TAG + ":seedUsers", "user_string : " + user_string);

            JsonObject json_object = JsonParser.parseString(user_string.toString()).getAsJsonObject();
            Log.i(TAG + ":seedUsers", "jsonObject : " + json_object);

            JsonArray users = json_object.getAsJsonArray("users");
            Log.i(TAG + ":seedUsers", "users : " + users);

            for(int i=0; i < users.size(); i++){
                JsonObject user = users.get(i).getAsJsonObject();

                String email = user.get("email").getAsString();

                Map<String, Object> user_map = (HashMap<String, Object>) gson.fromJson(user, HashMap.class);

                try {
                    store
                            .collection("users")
                            .document(email)
                            .update(user_map);
                } catch(Exception e){
                    Toast.makeText(source, "Error updating Firestore", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        } catch (Exception e){
            Log.d(TAG + ":seedUsers", "An error has occurred");
            e.printStackTrace();
        }

        Log.i(TAG + ":seedUsers", "Finished users seed...");
    }

    private static void seedExercises(FirebaseFirestore store, AppCompatActivity source){
        try {
            Log.i(TAG + ":Exercises", "Starting exercises seed...");
            InputStream is = source.getAssets().open("seed_exercises.csv");
            InputStreamReader isr = new InputStreamReader(is);

            CSVReader reader = new CSVReader(isr);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String bodyPart = nextLine[0];
                String equipment = nextLine[1];
                String gifURL = nextLine[2];
                String id = nextLine[3];
                String name = nextLine[4];
                String target = nextLine[5];

                ListExercise listExercise = new ListExercise(bodyPart, equipment, gifURL, name, target);

                store
                        .collection("exercises")
                        .document(id)
                        .set(listExercise.toMap())
                        .addOnCompleteListener(task -> {
                            Toast.makeText(source.getApplicationContext(), "Firebase Seed Successful : " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        });
            }
            Log.i(TAG + ":Exercises", "Finished seeding exercises...");
        } catch (Exception e) {
            Log.d(TAG + ":Exercises", "Error parsing exercise data", e);
            e.printStackTrace();
        }
    }
}
