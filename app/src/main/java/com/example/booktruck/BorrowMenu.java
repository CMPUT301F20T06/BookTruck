/*
 *  Classname: BorrowMenu
 *  Version: V2
 *  Date: 2020.11.01
 *  Copyright: Jiachen Xu
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
 * BorrowMenu class provides three buttons that user can click on and redirect to other pages
 * about "Borrow" functionality.
 */
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
                Intent gotoView = new Intent(this, BorrowBookList.class);
                startActivity(gotoView);
                break;
            case R.id.receive_book_button:
                Intent gotoReceive = new Intent(this, ScanISBN.class);
                gotoReceive.putExtra("ParentClass", "BorrowReceive");
                startActivity(gotoReceive);
                break;
            case R.id.handover_book_button:
                Intent gotoHandOver = new Intent(this, ScanISBN.class);
                gotoHandOver.putExtra("ParentClass", "BorrowHandOver");
                startActivity(gotoHandOver);
                break;
            default:
                break;
        }
    }
}
