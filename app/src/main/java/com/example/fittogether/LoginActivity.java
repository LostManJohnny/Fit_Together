package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 *
 */
public class LoginActivity extends AppCompatActivity {

    private static final String PREFERENCES = "My_Preferences";
    private FirebaseAuth mAuth;
    private FirebaseFirestore store;
    private static final String TAG = LoginActivity.class.toString();
    private FirebaseUser currentUser;

    // Shared Preferences
    SharedPreferences sharedPref;

    //Views
    TextView txt_Email, txt_Password;
    Button btn_Login, btn_Register;

    /**
     * Event Handler onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    /**
     * Event Handler onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialized Views
        txt_Email = findViewById(R.id.et_LoginEmail);
        txt_Password = findViewById(R.id.et_LoginPassword);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Register = findViewById(R.id.btn_Register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        // Event Listeners
        btn_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnLogin_onClick(v);
            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnRegister_onClick(v);
            }
        });
    }

    //region Event Handlers

    /**
     * Starts registration activity
     * @param v : The View calling the action
     */
    public void btnRegister_onClick(View v){
        Intent intent = new Intent(v.getContext(), SignupActivity.class);
        startActivity(intent);
    }

    /**
     *     1.  Retrieves the email and password from the text inputs
     *     2.  Validates them such that they are
     *         a) Not null
     *         b) Not empty strings
     *     3.  Alerts the user if there is an issue during sign
     *     4.  If there are no issues, and authentication succeeds, signs the user in and starts the
     *             home screen activity
     * @param v : The View calling the action
     */
    public void btnLogin_onClick(View v){
        String email, password;
        email = txt_Email.getText().toString();
        password = txt_Password.getText().toString();

        Log.i(TAG, "\nEmail: " + email +
                "\nPassword: " + password +
                "\nNull?: " + (password.equals("") || email.equals("")));

        //Verify information is not ""
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

        else {
            try {
                signIn(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //endregion

    /**
     * Process flow for creating a new account in Firebase
     * @param email : The email of the account being created
     * @param password : The plaintext password of the account being created
     */
    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * The signIn process flow with Firebase
     * @param email : The email of the user attempting to sign in
     * @param password : The plaintext password of the user attempting to sign in
     */
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();


                            DocumentReference docRef = store.collection("users").document(uid);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                                /**
                                 * Given the retrieval was a success, parse the data and store in SharedPreferences
                                 * @param documentSnapshot
                                 */
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    // If the document exists, parse the data and store in Shared Preferences
                                    if(documentSnapshot.exists()){
                                        Map<String, Object> data = documentSnapshot.getData();

                                        String  firstName = null,
                                                lastName = null,
                                                email = null;

                                        // If there is a data entry for the user, retrieve the information and store it in the sharedPreferences
                                        if (data != null) {
                                            firstName = (String) data.get("First_Name");
                                            lastName = (String) data.get("Last_Name");
                                            email = (String) data.get("Email");

                                            sharedPref.edit()
                                                    .putString("FirstName", firstName)
                                                    .putString("LastName", lastName)
                                                    .putString("email", email)
                                                    .apply();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data from Firestore", Toast.LENGTH_SHORT).show();
                                        }

                                        updateUI(user);

                                    }
                                    else{
                                        updateUI(null);
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Updates the user to use data specific to the user
     * @param user : User to based the UI upon
     */
    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "An error occured when signing in, please try again", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Reloads the activity
     */
    private void reload() {
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }
}

/*
CoordinatorLayout - cdl
ContraintLayout - csl
AppBarLayout - abl
Button - btn
EditText - et
TextView - tv
ProgressBar - pb
Checkbox - chk
Radiobutton - rb
ToggleButton - tb
Spinner - spn
Menu - mnu
 */