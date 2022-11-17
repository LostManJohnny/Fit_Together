package com.example.fittogether.Models.Class;

import com.example.fittogether.Models.Enums.AccountType;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for the Authentication document on Firebase
 */

public class User {
    private final Map<String, Object> map;

    private AccountType account_type;

    /**
     * Empty constructor
     */
    public User(){
        this(null, null, null, AccountType.CLIENT);
    }

    /**
     * Create a user model
     * @param first_name : First name of the user
     * @param last_name : Last name of the user
     * @param email : Email of the user (unique identifier)
     * @param account_type : Whether they are a client or trainer
     */
    public User(String first_name, String last_name, String email, AccountType account_type){
        map = new HashMap<>();

        map.put("First Name", first_name);
        map.put("Last Name", last_name);
        map.put("Email", email);
        map.put("Account Type", account_type);
    }

    public User(Map<String, Object> map){
        this.map = map;
    }

    /**
     * Creates a map based on the current user data
     * @return A hash map of the user
     */
    @Exclude
    public Map<String, Object> toMap(){
        return map;
    }

    //region Getters
    /**
     * Returns the first name of the user
     * @return : First Name
     */
    public String getFirstName(){
        return (String) map.get("First Name");
    }

    /**
     * Returns the last name of the user
     * @return : Last Name
     */
    public String getLastName(){
        return (String) map.get("Last Name");
    }

    /**
     * Returns the email of the user
     * @return : Email
     */
    public String getEmail(){
        return (String) map.get("Email");
    }

    /**
     * Returns the account type of the user
     * @return : Account Type
     */
    public AccountType getAccountType(){
        return account_type;
    }

    public Map<String, Object> getWorkouts(){return (HashMap<String, Object>) map.get("Workouts");}
    //endregion

}
