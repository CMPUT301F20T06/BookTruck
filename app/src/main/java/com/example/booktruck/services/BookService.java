package com.example.booktruck.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.booktruck.models.Book;
import com.example.booktruck.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class BookService {

    FirebaseFirestore db;
    final CollectionReference collectionReference;
    UserService userService;

    public BookService () {
        userService = new UserService();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Books");
    }

    // create a book and save it into firebase
    public void createBook(String title, String author, String ISBN, String description){
        Book book = new Book(title, author, ISBN, description);
        HashMap<String, Object> data = new HashMap<>();
        data.put("ISBN", book.getISBN());
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor());
        data.put("status", book.getStatus());
        data.put("description", book.getDescription());
        data.put("borrower", book.getBorrower());
        data.put("owner", userService.getCurrentUsername());

        // save a new book into Firebase collection "Books"
        collectionReference
                .document(book.getISBN())    // using book's ISBN as the document ID
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
}
