package com.example.booktruck.models;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private String username;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}