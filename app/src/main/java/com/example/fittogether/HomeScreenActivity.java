package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fittogether.Helpers.Activity;
import com.example.fittogether.Helpers.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = HomeScreenActivity.class.getName();
    // Firebase connections
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;
    private FirebaseUser currentUser;

    // Views
    TextView tv_Welcome;

    /**
     * Event Handler onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Initialize Firebase Connections
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Retrieve Vies
        tv_Welcome = findViewById(R.id.tv_Welcome);

        if(currentUser != null){
            updateUI(currentUser);
        }
        else{
            Toast.makeText(getApplicationContext(), "Authentication error", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Event Handler onStart
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Activity.goToLogin(HomeScreenActivity.this, true);
        }
    }

    //region Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();

        if (itemId == R.id.mni_Profile) {
            showProfile();
        }
        else if (itemId == R.id.mni_Logout) {
            Authentication.Logout(HomeScreenActivity.this);
        }
        else if (itemId == R.id.mni_Settings) {
            showSettings();
        }
        else{
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Changes from the homescreen activity to the settings activity
     */
    private void showSettings() {
    }

    /**
     * Changes from the homescreen activity to the profile activity
     */
    private void showProfile() {
        Activity.goToProfile(HomeScreenActivity.this, false);
    }
    //endregion

    /**
     * Updates the UI based on a user
     * @param user : Authentication to base the UI upon
     */
    private void updateUI(FirebaseUser user) {
        String user_primary_key = user.getEmail();

        if(user_primary_key != null) {
            DocumentReference docRef =
                    store
                            .collection("users")
                            .document(user_primary_key);

            docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        /**
                         * Event Handler onComplete
                         * After the query is successful, the UI is updated with the values
                         *
                         * @param task : The task being executed
                         */
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String first_name = (String) document.get("First_Name");
                                    tv_Welcome.setText("Welcome " + first_name);
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
        else{
            Toast.makeText(getApplicationContext(), "Couldn't retrieve data", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Couldn't retrieve data from Firestore as primary key was null");
        }
    }

    /**
     * Event Handler onBackPressed
     * Prevents the user from returning back to the login screen after signing in by closing the app
     * Same as if they pressed the home button
     * TODO: Place a warning / request to press the back button again to close the app
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}