package com.example.booktruck;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize layout
        setContentView(R.layout.view_book_layout);

        bookList = (ListView) findViewById(R.id.view_book_list);

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser != null) {
            // Name
            String name = currentuser.getDisplayName();
        }

        db = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.view_book_layout, bookArray);
        bookList.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();


        //final DatabaseReference bookListRef = FirebaseDatabase.getInstance().getReference().child('Book List');

        //FirebaseRecyclerOptions<>
    }
}
