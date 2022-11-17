package com.example.fittogether.Api;

import static com.example.fittogether.Api.Activity.goToLogin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Models.Enums.RequestCode;
import com.example.fittogether.Api.Exceptions.InvalidEmailException;
import com.example.fittogether.Api.Exceptions.NullCurrentUserException;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authentication {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseUser currentUser = mAuth.getCurrentUser();

    private static final String TAG = "Helpers:Authentication";

    /**
     * Signs out the current user
     */
    public static void Logout(AppCompatActivity source){
        mAuth.signOut();

        goToLogin(source, true);
    }

    /**
     * The signIn process flow with Firebase
     * @param email : The email of the user attempting to sign in
     * @param password : The plaintext password of the user attempting to sign in
     */
    public static Task<AuthResult> signIn(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    /**
     * The signIn process flow for sign-in with Google
     */
    public static void signIn_Google(Activity source, GoogleSignInClient gsc){
        Intent intent = gsc.getSignInIntent();
        source.startActivityForResult(intent, RequestCode.GOOGLE_SIGN_IN.ordinal());
    }

    /**
     * Deletes the current signed in user
     * @param source : The source activity
     */
    public static void deleteAccount(AppCompatActivity source) {
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        try {
            if (currentUser == null) {
                throw new NullCurrentUserException();
            }

            String email = currentUser.getEmail();

            if(email == null){
                throw new InvalidEmailException();
            }

            store.collection("users")
                    .document(email)
                    .delete();

            currentUser
                    .delete()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(source.getApplicationContext(), "Authentication has been deleted", Toast.LENGTH_SHORT).show();

                            goToLogin(source, true);
                        }
                        else{
                            Toast.makeText(source.getApplicationContext(), "Error deleting your account", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch(NullCurrentUserException e){
            Log.e(TAG + ":deleteAccount", "Current user is null. - " + e.getMessage());
            e.printStackTrace();
        } catch(InvalidEmailException e){
            Log.e(TAG + ":deleteAccount", "Invalid email is being used. - " + e.getMessage());
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
