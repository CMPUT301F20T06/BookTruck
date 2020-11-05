package com.example.booktruck;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.booktruck.models.Book;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;

public class CreateBook extends AppCompatActivity {

    private String ISBN;
    private String author;
    private String title;
    private EditText titleText;
    private EditText authorText;
    private EditText ISBNText;

    FirebaseFirestore db;
    CollectionReference bookRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book);

        this.titleText = findViewById(R.id.bookName);
        this.authorText = findViewById(R.id.authorName);
        this.ISBNText = findViewById(R.id.ISBN_number);


        // Setup and Firestore
        db = FirebaseFirestore.getInstance();
        bookRef = db.collection("Books");

    }

    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

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

    public void onCreateBook(View view){
        this.author = authorText.getText().toString();
        this.title = titleText.getText().toString();
        this.ISBN = ISBNText.getText().toString();

        if (author.equals("") || title.equals("") || ISBN.equals("")){
            Toast.makeText(getApplicationContext(),"Book's title, author and ISBN must not be empty!", Toast.LENGTH_SHORT).show();
        } else if (ISBN.length() < 13){
            Toast.makeText(getApplicationContext(),"Book's ISBN must have 13 digits!", Toast.LENGTH_SHORT).show();
        }
        else {
            createBook(title, author, ISBN);
        }


        NavUtils.navigateUpFromSameTask(CreateBook.this);
    }

}
