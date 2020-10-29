package com.example.booktruck;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.booktruck.models.Book;

public class CreateBook extends AppCompatActivity {

    private String ISBN;
    private String author;
    private String title;
    private String description;
    private EditText titleText;
    private EditText authorText;
    private EditText ISBNText;
    private EditText descriptionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book);

        this.titleText = findViewById(R.id.bookName);
        this.authorText = findViewById(R.id.authorName);
        this.ISBNText = findViewById(R.id.ISBN_number);
        this.descriptionText = findViewById(R.id.description);
    }

    public void onCreateBook(View view){
        this.ISBN = ISBNText.getText().toString();
        this.author = authorText.getText().toString();
        this.title = titleText.getText().toString();
        this.description = descriptionText.getText().toString();
        Book book = new Book(title, author, ISBN, description);
        book.saveBookIntoFirebase();
        NavUtils.navigateUpFromSameTask(CreateBook.this);
    }
}
