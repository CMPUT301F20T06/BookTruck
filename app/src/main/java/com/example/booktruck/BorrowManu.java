package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class BorrowManu extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.borrow_layout);

    }

    public void onOpenViewBook(View view) {

        Intent gotoViewBook = new Intent(this,
                ViewBook.class);

        startActivity(gotoViewBook);

    }

    public void onOpenRecieve(View view) {

        Intent gotoRecieve = new Intent(this,
                RecieveBook.class);

        startActivity(gotoRecieve);

    }

    public void onOpenHandOver(View view) {

        Intent gotoHnadOver = new Intent(this,
                ViewBook.class);

        startActivity(gotoHnadOver);

    }

}
