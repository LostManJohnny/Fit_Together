package com.example.fittogether.Api;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fittogether.Admin;
import com.example.fittogether.HomeScreenActivity;
import com.example.fittogether.LoginActivity;
import com.example.fittogether.MyWorkouts;
import com.example.fittogether.Profile;
import com.example.fittogether.Settings;
import com.example.fittogether.SignupActivity;

public class Activity {


    private static void changeActivity(AppCompatActivity source, Class<?> destination, boolean finish){
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);

        if(finish)
            source.finish();
    }

    public static void goToRegister(AppCompatActivity source, boolean finish) {
        changeActivity(source, SignupActivity.class, finish);

    }

    public static void goToHome(AppCompatActivity source, boolean finish){
        changeActivity(source, HomeScreenActivity.class, finish);
    }

    public static void goToProfile(AppCompatActivity source, boolean finish){
        changeActivity(source, Profile.class, finish);
    }

    public static void goToChangePassword(AppCompatActivity source, boolean finish) {
//        changeActivity(source, ChangePassword.class, finish);
    }

    public static void goToLogin(AppCompatActivity source, boolean finish) {
        changeActivity(source, LoginActivity.class, finish);
    }

    public static void goToAdmin(AppCompatActivity source, boolean finish) {
        changeActivity(source, Admin.class, finish);
    }

    public static void goToSettings(AppCompatActivity source, boolean finish) {
        changeActivity(source, Settings.class, finish);
    }

    public static void goToMyWorkouts(AppCompatActivity source, boolean finish) {
        changeActivity(source, MyWorkouts.class, finish);
    }
}
