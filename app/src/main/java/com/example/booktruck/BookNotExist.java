/*
 *  Classname: NotificationPage
 *  Version: V3
 *  Date: 2020.11.05
 *  Copyright: Qi Song
 */
package com.example.booktruck;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BookNotExist extends AppCompatActivity {

    /*
    * render the page when book does not exist
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_not_exist);

    }
}
