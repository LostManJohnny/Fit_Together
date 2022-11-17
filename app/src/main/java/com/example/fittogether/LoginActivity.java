package com.example.fittogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Models.Enums.RequestCode;
import com.example.fittogether.Api.Activity;
import com.example.fittogether.Api.Authentication;
import com.example.fittogether.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class LoginActivity extends AppCompatActivity {

    // Logging
    private static final String TAG = LoginActivity.class.toString();

    // Initialize Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;
    private FirebaseUser currentUser;

    // Google Auth
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    // Databinding
    ActivityLoginBinding binding;

    // region Activity Event Handlers
    /**
     * Event Handler onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Databinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        // Initialize Google Auth
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().requestProfile().build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Event Listeners
        binding.btnLogin.setOnClickListener(this::btnLogin_onClick);

        binding.btnRegister.setOnClickListener(this::btnRegister_onClick);

        binding.btnGoogleSignIn.setOnClickListener(v -> Authentication.signIn_Google(LoginActivity.this, gsc));


    }

    /**
     * Event Handler onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Toast.makeText(LoginActivity.this, "Logging in as ... " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Logging in as " + currentUser.getEmail());
            updateUI(currentUser);
        }
    }

    /**
     * Event handler for activity results
     * @param requestCode : Code for the requesting activity / action
     * @param resultCode : Result code from activity / action
     * @param data : Data returned from activity / action
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google Sign In activity result
        if (requestCode == RequestCode.GOOGLE_SIGN_IN.ordinal()) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = task.getResult(ApiException.class);
                // Connect Google account to Firebase user account
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(task1 -> {
                    Toast.makeText(getApplicationContext(), "Logged in with google", Toast.LENGTH_LONG).show();
                    currentUser = mAuth.getCurrentUser();
                    updateUI(currentUser);
                });
            } catch (ApiException e) {
                Toast.makeText(this, "Sign In Failed: failed code = " + e.getStatusCode(), Toast.LENGTH_LONG).show();
            }

        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }
    // endregion

    //region View Event Handlers

    /**
     * Starts registration activity
     * @param v : The View calling the action
     */
    public void btnRegister_onClick(View v){
        Activity.goToRegister(LoginActivity.this, true);
    }

    /**
     *     1.  Retrieves the email and password from the text inputs
     *     2.  Validates them such that they are
     *              a) Not null
     *              b) Not empty strings
     *     3.  Alerts the user if there is an issue during sign
     *     4.  If there are no issues, and authentication succeeds, signs the user in and starts the
     *             home screen activity
     * @param v : The View calling the action
     */
    public void btnLogin_onClick(View v){
        // Instantiate variables
        String email, password;

        // Retrieve variables
        email = binding.etLoginEmail.getText().toString();
        password = binding.etLoginPassword.getText().toString();

        // Verify information is not blank
        // ... if so, alert the user
        if(password.equals("") || email.equals("")){
            if(!password.equals("")){
                Toast.makeText(getApplicationContext(), "Username cannot be blank", Toast.LENGTH_LONG).show();
            }
            else if(!email.equals("")){
                Toast.makeText(getApplicationContext(), "Password cannot be blank", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Username and Password cannot be blank", Toast.LENGTH_LONG).show();
            }
        }
        // ... otherwise, attempt a sign in
        else {
            try {
                Authentication.signIn(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                // Update the current user
                                currentUser = mAuth.getCurrentUser();

                                updateUI(currentUser);

                            } else {
                                // SignIn Failed
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                // Display message to the user
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                updateUI(null);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //endregion

    // region Authentication


    // endregion

    // region Helpers
    /**
     * Updates the user to use data specific to the user
     * @param user : Authentication to based the UI upon
     */
    private void updateUI(FirebaseUser user) {
        // Verify that the user is not null
        if(user != null){
            goMainActivity();
        }
        else{
            Toast.makeText(this, "An error occurred when signing in, please try again", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sends the user to the main activity
     */
    private void goMainActivity() {
        // Create intent to Home Screen with the current user as an extra
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(intent);

        // Completes this activity so backPress doesn't return the user to this screen
        finish();
    }

    //endregion
}