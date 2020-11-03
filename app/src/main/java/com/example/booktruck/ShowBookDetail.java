package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.Book;
import com.example.booktruck.services.BookService;

public class ShowBookDetail extends AppCompatActivity {

    String ISBN;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_detail);

        Intent gotoBook = getIntent();
        ISBN = gotoBook.getStringExtra("isbn");
        getSupportActionBar().setTitle(ISBN);
    }
}
