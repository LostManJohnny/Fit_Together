package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SignupActivity extends AppCompatActivity {

    //Auth variables
    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;

    //Main thread handler
    Handler mainHandler = new Handler();

    // Views
    EditText et_FirstName, et_LastName, et_Email, et_Password;
    Button btn_SignUp;
    ProgressBar pb_Signup;

    //Login Credentials
    String email;
    String password;

    /**
     * Event Handler onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and FireStore
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        //Initialize views
        et_FirstName = findViewById(R.id.et_FirstName);
        et_LastName = findViewById(R.id.et_LastName);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        btn_SignUp = findViewById(R.id.btn_SignUp);
        pb_Signup = findViewById(R.id.pb_Signup);

        //Set Loading Wheel to GONE
        pb_Signup.setVisibility(View.GONE);

        btn_SignUp.setOnClickListener(new View.OnClickListener(){
            /**
             * Event Handler onClick
             * Starts the registration process for a new user
             * @param view : View calling the event
             */
            @Override
            public void onClick(View view) {
                //Retrieve all form data
                String fname, lname;

                fname = et_FirstName.getText().toString();
                lname = et_LastName.getText().toString();
                email = et_Email.getText().toString();
                password = et_Password.getText().toString();

                // Checks that all fields are valid
                if(fname.equals("") || lname.equals("") || email.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Create the new users account
                    // .. on success, add the remaining user data to Firestore
                    if(createAccount(email, password)){
                        // Create a user map
                        Map<String, Object> user = new HashMap<>();
                        user.put("First_Name", fname);
                        user.put("Last_Name", lname);

                        // Add new user to the collection
                        store
                            .collection("users")
                            .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener(){
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                }
            }
        });
    }

    /**
     * Process flow for creating a new user in Firestore Authentication
     * @param email : Email of the new user
     * @param password : Password of the new user
     * @return Whether the creation of the new user was successful or not
     */
    private boolean createAccount(String email, String password){
        final Boolean[] status = {true};
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            status[0] = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            status[0] = false;
                        }
                    }
                });

        return status[0];
    }

    /**
     * Updates the UI based on the current user
     * @param user : User to apply to the UI
     */
    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent i = new Intent(this, HomeScreenActivity.class)
                    .putExtra("current_User", user);
        } else{

        }
    }
}