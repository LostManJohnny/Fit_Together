package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

        // Initialize variables
        while(!task.isComplete()){

        }

        if(task.isComplete()){
            DocumentSnapshot profileData = task.getResult();
            og_firstName = (String) profileData.get("First_Name");
            og_lastName = (String) profileData.get("Last_Name");
            og_email = (String) profileData.get("Email");
        }

        update = false;

        String et_emailText = mainBinding.etProfileEmail.getText().toString();
        String et_fnameText = mainBinding.etProfileFirstName.getText().toString();
        String et_lnameText = mainBinding.etProfileLastName.getText().toString();

//        if(et_emailText != null){
//            og_email = et_emailText.toString();
//        }
//
//        if(et_fnameText!= null){
//            og_firstName = et_fnameText.toString();
//        }
//
//        if(et_lnameText != null){
//            og_lastName = et_lnameText.toString();
//        }

        Log.i(TAG, "og_email : " + og_email);
        Log.i(TAG, "og_Fname : " + og_firstName);
        Log.i(TAG, "og_Lnaem : " + og_lastName);
        Log.i(TAG, "og_Update? : " + update.toString());

        mainBinding.btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update){
                    updateProfile();
                }
                else{
                    Toast
                        .makeText(getApplicationContext(), "No changes have been made.", Toast.LENGTH_LONG)
                        .show();
                }
            }
        });
        mainBinding.btnProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });
        mainBinding.btnProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChangePassword();
            }
        });
        mainBinding.etProfileEmail.addTextChangedListener(new TextWatcher(){
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
        mainBinding.etProfileFirstName.addTextChangedListener(new TextWatcher(){
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
        mainBinding.etProfileLastName.addTextChangedListener(new TextWatcher(){
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
        mainBinding.btnProfileDeleteAccount.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    deleteAccount();
                }
            });
        Log.i(TAG, "og_update?2 : " + update);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // region Event Handlers
    private void updateProfile() {
        String primary_key = currentUser.getEmail();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("First_Name", mainBinding.etProfileFirstName.getText().toString());
        data.put("Last_Name", mainBinding.etProfileLastName.getText().toString());
        data.put("Email", mainBinding.etProfileEmail.getText().toString());

        store
            .collection("users")
            .document(primary_key)
            .update(data)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Update was successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Update was not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void textChanged(){
        update = !og_email.equals(mainBinding.etProfileEmail.getText().toString());
        update = update || !og_firstName.equals(mainBinding.etProfileFirstName.getText().toString());
        update = update || !og_lastName.equals(mainBinding.etProfileLastName.getText().toString());

        mainBinding.btnProfileUpdate.setEnabled(update);
    }

    private void goToChangePassword() {

    }

    private void goToHome(){
        Intent intent = new Intent(Profile.this, HomeScreenActivity.class);
        startActivity(intent);
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

                                String og_fname = (String)documentSnapshot.get("First_Name");
                                String og_lname = (String)documentSnapshot.get("Last_Name");
                                String og_email = (String)documentSnapshot.get("Email");

                                mainBinding.etProfileFirstName.setText(og_fname);
                                mainBinding.etProfileLastName.setText(og_lname);
                                mainBinding.etProfileEmail.setText(og_email);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
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

    private void deleteAccount() {
        String email = currentUser.getEmail();
        store.collection("users").document(email).delete();
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"User has been deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Profile.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}