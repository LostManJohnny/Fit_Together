package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fittogether.Exceptions.InvalidEmailException;
import com.example.fittogether.Exceptions.NullCurrentUserException;
import com.example.fittogether.Helpers.Activity;
import com.example.fittogether.Helpers.Authentication;
import com.example.fittogether.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private static final String TAG = "Profile.java";
    // View binding
    private ActivityProfileBinding mainBinding;

    // Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Firebase Firestore
    private FirebaseFirestore store;

    // Global Variables
    Boolean update;
    String og_email = "";
    String og_firstName = "";
    String og_lastName = "";

    /**
     * Event handler onCreate for Profile activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Firebase Firestore
        store = FirebaseFirestore.getInstance();

        // Get Profile Data
        Task<DocumentSnapshot> task = setProfileData();
        update = false;

        // Ensure task is not null...
        if(task != null) {
            task.addOnCompleteListener(task1 -> {
                DocumentSnapshot profileData = task1.getResult();
                og_firstName = (String) profileData.get("First_Name");
                og_lastName = (String) profileData.get("Last_Name");
                og_email = (String) profileData.get("Email");

                bind_event_listeners();

                Log.i("PROVIDER ID", currentUser.getProviderData().toString());
            });
        }
        else{
            Toast.makeText(this, "Error getting profile data", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Binds all event listeners for the activity
     */
    private void bind_event_listeners(){
        mainBinding.btnProfileUpdate.setOnClickListener(view -> {
            if (update) {
                updateProfile();
            } else {
                Toast
                        .makeText(getApplicationContext(), "No changes have been made.", Toast.LENGTH_LONG)
                        .show();
            }
        });

        mainBinding.btnProfileCancel.setOnClickListener(view -> Activity.goToHome(Profile.this, true));

        mainBinding.btnProfileChangePassword.setOnClickListener(view -> Activity.goToChangePassword(Profile.this, true));

        mainBinding.etProfileEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textChanged();
            }
        });

        mainBinding.etProfileFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textChanged();
            }
        });

        mainBinding.etProfileLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textChanged();
            }
        });

        mainBinding.btnProfileUpdate.setEnabled(update);

        mainBinding.btnProfileDeleteAccount.setOnClickListener(view -> Authentication.deleteAccount(Profile.this));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // region Event Handlers
    private void updateProfile() {
        try {
            if(currentUser == null){
                throw new NullCurrentUserException();
            }
            String primary_key = currentUser.getEmail();
            if(primary_key == null){
                throw new InvalidEmailException();
            }
            Map<String, Object> data = new HashMap<>();
            data.put("First_Name", mainBinding.etProfileFirstName.getText().toString());
            data.put("Last_Name", mainBinding.etProfileLastName.getText().toString());
            data.put("Email", mainBinding.etProfileEmail.getText().toString());

            store
                    .collection("users")
                    .document(primary_key)
                    .update(data)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Update was successful", Toast.LENGTH_SHORT).show();

                            og_firstName = mainBinding.etProfileFirstName.getText().toString();
                            og_lastName = mainBinding.etProfileLastName.getText().toString();
                            og_email = mainBinding.etProfileEmail.getText().toString();

                            update = false;

                            mainBinding.btnProfileUpdate.setEnabled(false);
                        } else {
                            Toast.makeText(getApplicationContext(), "Update was not successful", Toast.LENGTH_SHORT).show();
                        }
                    });
            } catch(NullCurrentUserException e){
              Toast.makeText(getApplicationContext(), "Error updating profile.", Toast.LENGTH_SHORT).show();
              Log.d(TAG + ":updateProfile()", "Error updating profile - Null current user", e);
            } catch (InvalidEmailException e) {
              Toast.makeText(getApplicationContext(), "Error retrieving email", Toast.LENGTH_SHORT).show();
              Log.d(TAG + ":updateProfile()", "Error updating profile - Email was invalid", e);
            }
    }

    private void textChanged(){
        update = !og_email.equals(mainBinding.etProfileEmail.getText().toString());
        update = update || !og_firstName.equals(mainBinding.etProfileFirstName.getText().toString());
        update = update || !og_lastName.equals(mainBinding.etProfileLastName.getText().toString());

        mainBinding.btnProfileUpdate.setEnabled(update);
    }
    // endregion
    
    @Nullable
    private Task<DocumentSnapshot> setProfileData(){
        String user_primary_key = currentUser.getEmail();

        mainBinding.etProfileEmail.setText(currentUser.getEmail());

        if(user_primary_key != null) {
            return
                store
                    .collection("users")
                    .document(user_primary_key)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        /**
                         * Event Handler onComplete
                         * After the query is successful, the original values are updated
                         *
                         * @param documentSnapshot : Snapshot of the document
                         */
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                                mainBinding.etProfileFirstName.setText(
                                        (String)documentSnapshot.get("First_Name"));
                                mainBinding.etProfileLastName.setText(
                                        (String)documentSnapshot.get("Last_Name"));
                                mainBinding.etProfileEmail.setText(
                                        (String)documentSnapshot.get("Email"));

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        /**
                         * Event Handler onComplete
                         * If the query is unsuccessful, a warning toast is displayed
                         */
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast
                                .makeText(getApplicationContext(), "Error retrieving data", Toast.LENGTH_SHORT)
                                .show();

                            Log.d(TAG, "Document GET was unsuccessful. Primary key was null");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Error retrieving data", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Couldn't retrieve data from Firestore, primary_key is null");
        }

        return null;
    }
}