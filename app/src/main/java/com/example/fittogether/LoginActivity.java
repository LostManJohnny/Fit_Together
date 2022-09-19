package com.example.fittogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    //Views
    TextView txt_Email, txt_Password;
    Button btn_Login, btn_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialized Views
        txt_Email = findViewById(R.id.et_LoginEmail);
        txt_Password = findViewById(R.id.et_LoginPassword);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Register = findViewById(R.id.btn_Register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Event Listeners
        btn_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
        });
        btn_Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

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

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
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

    private void updateUI(FirebaseUser user) {
    }

    private void reload() {

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