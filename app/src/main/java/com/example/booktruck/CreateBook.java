package com.example.booktruck;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.booktruck.models.Book;

import com.example.booktruck.services.BookService;


import java.util.HashMap;
import java.util.Random;

public class CreateBook extends AppCompatActivity {

    private String ISBN;
    private String author;
    private String title;
    private EditText titleText;
    private EditText authorText;
    private EditText ISBNText;

    private BookService bookService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book);

        bookService = new BookService();

        this.titleText = findViewById(R.id.bookName);
        this.authorText = findViewById(R.id.authorName);
        this.ISBNText = findViewById(R.id.ISBN_number);


        // disable ISBN user input, and generate an ISBN number
        this.ISBN = generateISBN();
        ISBNText.setEnabled(false);
        ISBNText.setText("ISBN: "+ this.ISBN);

    }

    private String generateISBN(){
        Random random = new Random();
        String ISBN = "";
        for (int i=0; i<13; i++) {
            int num = Math.abs(random.nextInt());
            num = num % 10;
            ISBN += String.valueOf(num);
        }
        return ISBN;


    }

    public void onCreateBook(View view){
        this.author = authorText.getText().toString();
        this.title = titleText.getText().toString();


        bookService.createBook(title, author, ISBN);

        NavUtils.navigateUpFromSameTask(CreateBook.this);
    }

}
