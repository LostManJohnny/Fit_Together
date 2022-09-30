package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = HomeScreenActivity.class.getName();
    // Firebase connections
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;
    private FirebaseUser currentUser;

    // Views
    TextView tv_Welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Initialize Firebase Connections
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        // Retrieve Vies
        tv_Welcome = findViewById(R.id.tv_Welcome);



        if(currentUser != null){
            updateUI();
        }
        else{
            tv_Welcome.setText("Null User");
        }
    }

    private void updateUI() {
        String uid = currentUser.getUid();

        DocumentReference docRef = store.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        tv_Welcome.setText(document.getData().toString());
                    } else {
                        Log.d(TAG, "No such document");
                        tv_Welcome.setText("No Such Document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    tv_Welcome.setText("Get Failed");
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
////            reload();
//        }
    }

    private void reload() {
    }

    //Prevents the user from returning back to the login screen after signing in by closing the app
    //Same as if they pressed the home button
    //TODO: Place a warning / request to press the back button again to close the app
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}