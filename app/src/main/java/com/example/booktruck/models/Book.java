package com.example.booktruck.models;

public class Book {

    private String ISBN;
    private String title;
    private String author;
    private String status;
    private String description;
    private String owner;
    private String borrower;
    private String[] requests;

    public Book(String title, String author, String ISBN, String description) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = "active";
        this.borrower = "";
    }

    public Book(String title, String author, String ISBN, String description, String status,
                String owner, String borrower, String[] requests) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String[] getRequests() {
        return requests;
    }
}



