package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.services.BookService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ShowBookDetail extends AppCompatActivity {

    TextView authorText, statusText, ownerText, titleText;
    String titleContent, authorContent, statusContent, ownerContent;
    private BookService bookService;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.book_detail);
        //显示的书必须满足条件
        //必需是owner拥有的书
        titleText = (TextView)findViewById(R.id.titleView);
        authorText = (TextView)findViewById(R.id.authorView);
        statusText = (TextView)findViewById(R.id.statusView);
        ownerText = (TextView)findViewById(R.id.ownerView);
//
        Intent gotoBook = getIntent();
        String ISBN = gotoBook.getStringExtra("ISBN");
        Log.i("RECICVE_ISBN",ISBN);

        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " + document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        titleContent = data.get("title").toString();
                        authorContent = data.get("author").toString();
                        statusContent = data.get("status").toString();
                        ownerContent = data.get("owner").toString();
                        Log.i("CONTENT",authorContent);

                        getSupportActionBar().setTitle(titleContent);

                        titleText.setText(titleContent);
                        authorText.setText(authorContent);
                        statusText.setText(statusContent);
                        ownerText.setText(ownerContent);

                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });


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
