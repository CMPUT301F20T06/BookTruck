package com.example.booktruck.models;

import java.util.ArrayList;

public class Book {

    private String ISBN;
    private String title;
    private String author;
    private String status;
    private String owner;

    private String borrower;
    private ArrayList<String> requests;


    public Book(String title, String author, String ISBN) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.status = "available";
        this.borrower = "";
        this.requests = new ArrayList<>();
    }

    public Book(String title, String author, String ISBN, String status,
                String owner, String borrower, ArrayList<String> requests) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.status = status;
        this.owner = owner;
        this.borrower = borrower;
        this.requests = requests;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public String getBorrower() {
        return borrower;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
}




