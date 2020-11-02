package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BorrowMenu extends AppCompatActivity implements View.OnClickListener{

    private Button ViewButton, ReceiveButton, HandOverButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.borrow_layout);

        ViewButton = (Button) findViewById(R.id.view_book_button);
        ViewButton.setOnClickListener(this);

        ReceiveButton = (Button) findViewById(R.id.receive_book_button);
        ReceiveButton.setOnClickListener(this);

        HandOverButton = (Button) findViewById(R.id.handover_book_button);
        HandOverButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_book_button:
                //code here
                Intent gotoView = new Intent(this, ViewBook.class);
                startActivity(gotoView);
                break;
            case R.id.receive_book_button:
                //code here
                Intent gotoReceive = new Intent(this, ReceiveBook.class);
                startActivity(gotoReceive);
                break;
            case R.id.handover_book_button:
                //code here
                break;
            default:
                break;
        }
    }
}
