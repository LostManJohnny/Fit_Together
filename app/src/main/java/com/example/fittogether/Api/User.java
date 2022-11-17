package com.example.fittogether.Api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    public static Task<DocumentSnapshot> getUser(String userID){
        FirebaseFirestore store = FirebaseFirestore.getInstance();

        return store.collection("users")
                .document(userID)
                .get();
    }
}
