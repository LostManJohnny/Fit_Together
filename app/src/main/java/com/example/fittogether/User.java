package com.example.fittogether;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for the User document on Firebase
 */

public class User {
    private String  uid,
                    first_name,
                    last_name,
                    email;

    /**
     * Empty constructor
     */
    public User(){

    }

    /**
     * Create a user model from a map
     * @param fromMap : Source user model
     */
    public User(Map<String, Object> fromMap){
        uid = (String) fromMap.get("UID");
        first_name = (String) fromMap.get("First_Name");
        last_name = (String) fromMap.get("Last_Name");
        email = (String) fromMap.get("Email");
    }

    /**
     * Creates a map based on the current user data
     * @return A hash map of the user
     */
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("UID", uid);
        result.put("First_Name", first_name);
        result.put("Last_Name", last_name);
        result.put("Email", email);

        return result;
    }
}
