package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.User;
import com.example.booktruck.services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBook extends AppCompatActivity {

    private ListView bookList;
    private ArrayList<String> bookArray = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_layout);

        bookList = (ListView) findViewById(R.id.view_book_list);
        //get user name
        String user = UserService.getCurrentUsername();
        //get borrowed book
        ArrayList borrowed = UserService.getBorrowedBook();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.view_book_layout, borrowed);
        bookList.setAdapter(arrayAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String bookname = arrayAdapter.getItem(position);
                Intent bookdetail = new Intent(ViewBook.this, ShowBookDetail.class);

            }
        });


    }
}
