/*
 *  Classname: Book
 *  Version: V1
 *  Date: 2020.10.20
 *  Copyright: Qi Song
 */
package com.example.booktruck.models;

import java.util.ArrayList;

/*
 *  Book Class is the Book Schema, and provides some basic getter methods
 */
public class Book {

    private String ISBN;
    private String title;
    private String author;
    private String status;
    private String owner;
    private String borrower;
    private ArrayList<String> requests;
    private ArrayList<Double> locatioin;

    /**
     * @param title     book title
     * @param author    book author
     * @param ISBN      book ISBN number
     */
    public Book(String title, String author, String ISBN) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.status = "available";
        this.borrower = "";
        this.locatioin.add(53.526530);
        this.locatioin.add(-113.523932);
        this.requests = new ArrayList<>();
    }

    /**
     * @param title     book title
     * @param author    book author
     * @param ISBN      book ISBN number
     * @param status    book status
     * @param owner     book owner
     * @param borrower  book borrower
     * @param requests  users who requested this book
     */
    public Book(String title, String author, String ISBN, String status,
                String owner, String borrower, ArrayList<String> requests) {
        this.ISBN = ISBN;
        this.locatioin.add(53.526530);
        this.locatioin.add(-113.523932);
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

    public ArrayList<Double> getLocatioin() {return locatioin;}
}




