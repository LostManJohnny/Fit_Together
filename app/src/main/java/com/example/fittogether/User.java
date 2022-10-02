package com.example.fittogether;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for the User document on Firebase
 */

public class User {
    private String  first_name,
                    last_name,
                    email;

    private Account_Type account_type;

    /**
     * Empty constructor
     */
    public User(){
        this(null, null, null, Account_Type.CLIENT);
    }

    /**
     * Create a user model
     * @param first_name : First name of the user
     * @param last_name : Last name of the user
     * @param email : Email of the user (unique identifier)
     * @param account_type : Whether they are a client or trainer
     */
    public User(String first_name, String last_name, String email, Account_Type account_type){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.account_type = account_type;
    }

    /**
     * Creates a map based on the current user data
     * @return A hash map of the user
     */
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("First_Name", first_name);
        result.put("Last_Name", last_name);
        result.put("Email", email);
        result.put("Account_Type", account_type);

        return result;
    }

    //region Getters
    /**
     * Returns the first name of the user
     * @return : First Name
     */
    public String getFirstName(){
        return first_name;
    }

    /**
     * Returns the last name of the user
     * @return : Last Name
     */
    public String getLastName(){
        return last_name;
    }

    /**
     * Returns the email of the user
     * @return : Email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Returns the account type of the user
     * @return : Account Type
     */
    public Account_Type getAccountType(){
        return account_type;
    }
    //endregion

}
