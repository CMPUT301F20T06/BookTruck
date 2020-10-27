package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class BorrowManu extends Activity implements View.OnClickListener{

    private Button ViewButton, RecieveButton, HandOverButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.borrow_layout);

        ViewButton = (Button) findViewById(R.id.view_book_button);
        ViewButton.setOnClickListener(this);

        RecieveButton = (Button) findViewById(R.id.recieve_book_button);
        RecieveButton.setOnClickListener(this);

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
            case R.id.recieve_book_button:
                //code here
                Intent gotoRecieve = new Intent(this, RecieveBook.class);
                startActivity(gotoRecieve);
                break;
            case R.id.handover_book_button:
                //code here
                break;
            default:
                break;
        }
    }
}
