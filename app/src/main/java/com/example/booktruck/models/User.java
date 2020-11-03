package com.example.booktruck.models;

public class User {
    private String username;
    private String email;
    private String password;
    private String[] requested;
    private String[] borrowed;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String[] getRequested() {
        return requested;
    }

    public String[] getBorrowed() {
        return borrowed;
    }
}