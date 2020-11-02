package com.example.booktruck.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserService {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    public UserService(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUsername() {
        String email = mAuth.getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
