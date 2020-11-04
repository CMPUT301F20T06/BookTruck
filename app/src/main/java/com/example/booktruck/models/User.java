package com.example.booktruck.models;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String password;
    private ArrayList<String> owned;
    private ArrayList<String> requested;
    private ArrayList<String> borrowed;
    private ArrayList<String> returned;
    private ArrayList<String> accepted;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.requested = new ArrayList<>();
        this.borrowed = new ArrayList<>();
        this.returned = new ArrayList<>();
        this.accepted = new ArrayList<>();
        this.owned = new ArrayList<>();
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

    public ArrayList<String> getRequested() {
        return requested;
    }

    public ArrayList<String> getBorrowed() {
        return borrowed;
    }

    public ArrayList<String> getReturned() {
        return returned;
    }

    public ArrayList<String> getAccepted() {
        return accepted;
    }

    public ArrayList<String> getOwned() {
        return owned;
    }
}