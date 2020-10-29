package com.example.booktruck.models;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class Book {

    private String ISBN;
    private String title;
    private String author;
    private String status;
    private String description;
    private String borrower;
    private String[] requests;
    private String owner;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;


    // extract username from the email
    public String getUsername(String email){
        String[] arrOfStr = email.split("@");
        String username = "";
        for (int i=0; i<arrOfStr.length-1; i++) {
            username = username + arrOfStr[i];
        }
        return username;
    }

    // initial book information and save it into firebase
    public void saveBookIntoFirebase(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("ISBN", this.ISBN);
        data.put("title", this.title);
        data.put("author", this.author);
        data.put("status", this.status);
        data.put("description", this.description);
        data.put("borrower", "");
//        data.put("requests", this.requests);
        data.put("owner", this.owner);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Books");
        collectionReference
                .document(this.ISBN)    // using user id as the document id
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("SaveBook", "Book has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("SaveBook", "Book could not be added!" + e.toString());
                    }
                });
    }

    public Book(String title, String author, String ISBN, String description) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        mAuth = FirebaseAuth.getInstance();
        this.owner = getUsername(mAuth.getCurrentUser().getEmail());
        this.status = "active";

        saveBookIntoFirebase();
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

    public String[] getRequests() {
        return requests;
    }

    public String getOwner() {
        return owner;
    }
}




