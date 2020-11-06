/*
 *  Classname: CreateBook
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Yifan Fan
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.booktruck.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * CreateBook Class provides the EditText for user to input new book information
 * and it connects to the Cloud Firestore to save the new book into "Books" Collection
 */
public class CreateBook extends AppCompatActivity {

    private String ISBN;
    private String author;
    private String title;
    private EditText titleText;
    private EditText authorText;
    private EditText ISBNText;

    FirebaseFirestore db;
    CollectionReference bookRef;
    CollectionReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book);

        this.titleText = findViewById(R.id.bookName);
        this.authorText = findViewById(R.id.authorName);
        this.ISBNText = findViewById(R.id.ISBN_number);

        db = FirebaseFirestore.getInstance();
        bookRef = db.collection("Books");
        userRef = db.collection("Users");
    }

    /**
     *
     * @return current user's username
     */
    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    /**
     *
     * @param title     new book title
     * @param author    new book author
     * @param ISBN      new Book ISBN number
     */
    public void createBook(String title, String author, String ISBN){
        Book book = new Book(title, author, ISBN);
        HashMap<String, Object> data = new HashMap<>();
        data.put("ISBN", book.getISBN());
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor());
        data.put("status", book.getStatus());
        data.put("borrower", book.getBorrower());
        data.put("requests", book.getRequests());
        data.put("owner", getCurrentUsername());

        // save a new book into Firebase collection "Books"
        bookRef.document(book.getISBN()).set(data);
    }

    /**
     *
     * @param ISBN  new book's ISBN number
     * addBookIntoOwnedList method will add the new book's ISBN into current user's own book list
     */
    public void addBookIntoOwnedList(String ISBN) {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) data.get("owned");
                        ownedList.add(ISBN);
                        userRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * @param view
     * onCreateBook method will be triggered when the "createBook" button onClick
     * onCreateBook method will connect to the Firestore and save the new book into "Books" Collection
     */
    public void onCreateBook(View view){
        this.author = authorText.getText().toString();
        this.title = titleText.getText().toString();
        this.ISBN = ISBNText.getText().toString();

        if (author.equals("") || title.equals("") || ISBN.equals("")){
            Toast.makeText(getApplicationContext(),"Book's title, author and ISBN must not be empty!",
                    Toast.LENGTH_SHORT).show();
        } else if (ISBN.length() < 13){
            Toast.makeText(getApplicationContext(),"Book's ISBN must have 13 digits!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            createBook(title, author, ISBN);
            addBookIntoOwnedList(this.ISBN);
        }

        Intent gotoDestination = new Intent(this, MainActivity.class);
        startActivity(gotoDestination);
    }

}
