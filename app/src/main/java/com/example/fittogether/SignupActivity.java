package com.example.fittogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fittogether.Models.Class.User;
import com.example.fittogether.Api.Validation.UserValidation;
import com.example.fittogether.Models.Enums.AccountType;
import com.example.fittogether.Api.Exceptions.IllegalAccountTypeException;
import com.example.fittogether.Api.Activity;
import com.example.fittogether.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    //Auth variables
    private static final String TAG = "SignupActivity.java";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    //Database
    private FirebaseFirestore store;

    // Binding
    ActivitySignupBinding binding;

    //Main thread handler
    Handler mainHandler = new Handler();

    // Views
    EditText et_FirstName, et_LastName, et_Email, et_Password;
    Button btn_SignUp;
    RadioGroup rdg_AccountTypes;
    RadioButton rdo_AccountType;

    /**
     * Event Handler onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and FireStore
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener(){
            /**
             * Starts the registration process for a new user
             * @param view : View calling the event
             */
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp(){
        //Retrieve all form data
        String first_name, last_name, email, password;
        AccountType account_type = null;

        first_name = binding.etFirstName.getText().toString();
        last_name = binding.etLastName.getText().toString();
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();

        // Validate all fields
        boolean valid = UserValidation.validateFirstName(binding.etFirstName) &&
                        UserValidation.validateLastName(binding.etLastName) &&
                        UserValidation.validateEmail(binding.etEmail) &&
                        UserValidation.validatePassword(binding.etPassword);

        // If all fields are valid
        if(valid){
            try {
                // Get the account type of the user

                if (binding.rdoAccountType.getCheckedRadioButtonId() ==
                        binding.rdoClient.getId()) {
                    account_type = AccountType.CLIENT;
                } else if (binding.rdoAccountType.getCheckedRadioButtonId() ==
                        binding.rdoTrainer.getId()) {
                    account_type = AccountType.TRAINER;
                } else {
                    throw new IllegalAccountTypeException("Unknown account type (" + account_type + ")");
                }

                // Create a user map
                User new_user = new User(first_name, last_name, email, account_type);

                // Create the new users account
                // .. on success, add the remaining user data to Firestore
                createAccount(email, password, new_user);
            }
            catch(IllegalAccountTypeException e){
                Log.d(TAG + "signUp:IllegalAccountTypeException", "Unknown account type " + "(" + account_type + ")");
            }
        }
    }

    /**
     * Adds the new user data to Firestore
     * @param new_user : The new user being added
     * @return Whether the add was successful or not
     */
    private boolean addNewUser(User new_user) {
        final Boolean[] status = {true};
        String user_primary_key = new_user.getEmail();

        // Add new user to the collection
        store.collection("users")
                .document(user_primary_key)
                .set(new_user.toMap())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully added document for user");
                    status[0] = true;
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    status[0] = false;
                });


        Toast.makeText(getApplicationContext(), "Finished adding data to Firestore", Toast.LENGTH_SHORT).show();

        return status[0];
    }

    /**
     * Process flow for creating a new user in Firestore Authentication
     * @param email : Email of the new user
     * @param password : Password of the new user
     */
    private void createAccount(String email, String password, User user){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(SignupActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();
                        currentUser = mAuth.getCurrentUser();

                        // Adds user data to Firestore
                        if (!addNewUser(user)) {
                            Toast.makeText(getApplicationContext(), "There was an error writing user data to Firestore", Toast.LENGTH_SHORT).show();
                            updateUI(currentUser);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(
                                        SignupActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    /**
     * Updates the UI based on the current user
     * @param user : Authentication to apply to the UI
     */
    private void updateUI(FirebaseUser user) {
        if(user != null){
            Activity.goToHome(SignupActivity.this, true);
        } else{
            Toast.makeText(SignupActivity.this, "Authentication is null", Toast.LENGTH_SHORT).show();
        }
    }
}