package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.Book;
import com.example.booktruck.models.User;
import com.example.booktruck.services.UserService;
import com.google.rpc.Code;

import java.util.ArrayList;

public class ReceiveBook extends AppCompatActivity implements View.OnClickListener {

    private EditText editISBN;
    private Button CodeSender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isbn_receive);

        editISBN = (EditText) findViewById(R.id.ISBNcode);

        CodeSender = (Button) findViewById(R.id.Code_Sender);
        CodeSender.setOnClickListener(this);


    }



    public void OnCodeScaner(View view) {
    }


    public void OnCodeEnter(View view) {
    }


    @Override
    public void onClick(View view) {
        String ISBN = editISBN.getText().toString();

        Intent gotoBook = new Intent(this, ShowBookDetail.class);
        gotoBook.putExtra("isbn", ISBN);
        startActivity(gotoBook);
    }
}



