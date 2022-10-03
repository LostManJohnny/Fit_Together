package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    //Auth variables
    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;
    private FirebaseUser currentUser;

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
        rdg_AccountTypes = findViewById(R.id.rdo_AccountType);

        btn_SignUp.setOnClickListener(new View.OnClickListener(){
            /**
             * Event Handler onClick
             * Starts the registration process for a new user
             * @param view : View calling the event
             */
            @Override
            public void onClick(View view) {
                //Retrieve all form data
                String first_name, last_name, email, password;
                Integer accountTypeID;
                Account_Type account_type;

                first_name = et_FirstName.getText().toString();
                last_name = et_LastName.getText().toString();
                email = et_Email.getText().toString();
                password = et_Password.getText().toString();
                accountTypeID = rdg_AccountTypes.getCheckedRadioButtonId();

                // Checks that all fields are valid
                if(first_name.equals("") || last_name.equals("") || email.equals("") || password.equals("") || accountTypeID == -1){
                    Toast.makeText(getApplicationContext(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Get the account type of the user
                    rdo_AccountType = (RadioButton) findViewById(accountTypeID);

                    if(rdo_AccountType == findViewById(R.id.rdo_Client)){
                        account_type = Account_Type.CLIENT;
                    } else {
                        account_type = Account_Type.TRAINER;
                    }

                    // Create the new users account
                    // .. on success, add the remaining user data to Firestore
                    if(createAccount(email, password)){
                        // Create a user map
                        User new_User = new User(first_name, last_name, email, account_type);

                        if(!addNewUser(new_User)){
                            Toast.makeText(getApplicationContext(),"There was an error writing user data to firestore", Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        });
    }

    /**
     * Adds the new user data to firestore
     * @param new_user : The new user being added
     * @return Whether the add was successful or not
     */
    private boolean addNewUser(User new_user) {
        final Boolean[] status = {true};

        String email = new_user.getEmail();

        // Add new user to the collection
        store.collection("users").document(email)
                .set(new_user.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added document for email : " + email);
                        status[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        status[0] = false;
                    }
                });

        return status[0];
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
                            Toast.makeText(SignupActivity.this, "Signup Successful : Signing in...", Toast.LENGTH_SHORT);
                            currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
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
            startActivity(i);
        } else{
            Toast.makeText(SignupActivity.this, "User is null", Toast.LENGTH_SHORT);
        }
    }
}