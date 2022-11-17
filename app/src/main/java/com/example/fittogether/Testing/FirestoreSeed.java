package com.example.fittogether.Testing;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Models.Class.Exercise;
import com.google.firebase.firestore.FirebaseFirestore;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class FirestoreSeed {
    public static void seedFirestore(AppCompatActivity source){
        try {

            Toast.makeText(source.getApplicationContext(), "Firebase Seed Started...", Toast.LENGTH_SHORT).show();

            FirebaseFirestore store = FirebaseFirestore.getInstance();
            InputStream is = source.getAssets().open("fitness_exercises.csv");
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

                Exercise exercise = new Exercise(bodyPart, equipment, gifURL, name, target);

                store
                        .collection("exercises")
                        .document(id)
                        .set(exercise.toMap())
                        .addOnCompleteListener(task -> {
                            Toast.makeText(source.getApplicationContext(), "Firebase Seed Successful : " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        });
            }

        } catch (Exception e) {
            Log.d("FirestoreSeed:seedFirestore()", "Error parsing data", e);
            e.printStackTrace();
        }
    }
}
