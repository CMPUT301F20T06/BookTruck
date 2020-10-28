package com.example.booktruck;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ViewBook extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_book_layout);

    }
}
