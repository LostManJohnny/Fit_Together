package com.example.fittogether.Helpers;

import static com.example.fittogether.Helpers.Activity.goToLogin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Enums.RequestCode;
import com.example.fittogether.Exceptions.InvalidEmailException;
import com.example.fittogether.Exceptions.NullCurrentUserException;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
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
     * @return : If the logout was successful (i.e. the current user is null)
     */
    public static boolean Logout(AppCompatActivity source){
        mAuth.signOut();

        goToLogin(source, true);

        return mAuth.getCurrentUser() == null;
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
                        Toast.makeText(source.getApplicationContext(), "Authentication has been deleted", Toast.LENGTH_SHORT).show();

                        goToLogin(source, true);
                    });
        } catch(NullCurrentUserException e){
            Log.e(TAG + ":deleteAccount", "Current user is null. - " + e.getMessage());
        } catch(InvalidEmailException e){
            Log.e(TAG + ":deleteAccount", "Invalid email is being used. - " + e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
