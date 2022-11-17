package com.example.fittogether.Api.Validation;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.example.fittogether.Models.Enums.AccountType;

public class UserValidation {
    public static boolean validateEmail(EditText et){
        int result = validateEmail(et.getText().toString());

        if(result == 1){
            et.setError("Email cannot be blank");
        } else if(result == 2){
            et.setError("Invalid email format");
        } else {
            return true;
        }

        return false;
    }

    public static boolean validateFirstName(EditText et) {
        int result = validateName(et.getText().toString());

        if (result == 1) {
            et.setError("First name cannot be blank.");
        } else {
            return true;
        }

        return false;
    }

    public static boolean validateLastName(EditText et) {
        int result = validateName(et.getText().toString());

        if (result == 1) {
            et.setError("Last name cannot be blank.");
        } else {
            return true;
        }

        return false;
    }

    public static boolean validatePassword(EditText et) {
        int result = validatePassword(et.getText().toString());

        if (result == 1) {
            et.setError("Password must be 8 - 16 characters.");
        } else {
            return true;
        }

        return false;
    }

    public static boolean validateAccountType(AccountType accountType) {


        return false;
    }

    private static int validateEmail(String email){
        boolean invalid = false;

        // Check empty string
        if(TextUtils.isEmpty(email))
            return 1;

        // Matches email address format
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return 2;

        // 0 => Good
        return 0;
    }

    private static int validateName(String name) {
        boolean invalid = false;

        // Check empty string
        if(TextUtils.isEmpty(name))
            return 1;

        // 0 => Good
        return 0;
    }

    private static int validatePassword(String password) {
        if(password.length() < 8 || password.length() > 16){
            return 1;
        }

        // 0 => Good
        return 0;
    }
}
