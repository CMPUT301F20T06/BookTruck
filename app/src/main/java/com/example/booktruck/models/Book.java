package com.example.booktruck.models;

public class Book {

    private String ISBN;
    private String title;
    private String author;
    private String status;
    private String description;
    private String borrower;

    public Book(String title, String author, String ISBN, String description) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = "active";
        this.borrower = "";
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
}




