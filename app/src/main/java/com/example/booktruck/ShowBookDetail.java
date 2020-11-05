package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.Book;
import com.example.booktruck.services.BookService;

public class ShowBookDetail extends AppCompatActivity {

    TextView authorText, availabilityText, ownerText, descriptionText;
    String ISBN;
    private BookService bookService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_detail);
        //显示的书必须满足条件
        //必需是owner拥有的书
        authorText = (TextView)findViewById(R.id.authorView);
        availabilityText = (TextView)findViewById(R.id.availabilityView);
        ownerText = (TextView)findViewById(R.id.ownerView);
        descriptionText = (TextView)findViewById(R.id.descriptionView);
//
        Intent gotoBook = getIntent();
        String ISBN = gotoBook.getStringExtra("isbn");
        getSupportActionBar().setTitle(ISBN);
//
//        Book book = BookService.getBookByISBN(ISBN);
//        String title = book.getTitle();
//        getSupportActionBar().setTitle(title);
//
//        String author = book.getAuthor();
//        String availability = book.getStatus();
//        String owner = book.getOwner();
//        String description = book.getDescription();
//
//        authorText.setText(author);
//        availabilityText.setText(availability);
//        ownerText.setText(owner);
//        descriptionText.setText(description);

        //如果找不到ISBN， 需要给出找不到的提示

        //在找到书之后，根据对应的parent 显示不同的界面
        //1. hand over a book, 需要明确一个pick up 地址，然后确认借出去
        //2. receive a book, 可以查看pick up location，然后确认收到
        //3. view book， 显示所有借到的书

        String parentClass = String.valueOf(getIntent().getStringExtra("ParentClass"));

        if(parentClass.equalsIgnoreCase("Receive")) {

            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_receiving);

        }
        else if(parentClass.equalsIgnoreCase("ViewBook")) {

        }
        else { // parentClass == "HandOver"
            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_handover);
        }
    }
}
