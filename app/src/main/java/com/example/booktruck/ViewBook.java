package com.example.booktruck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.Book;
import com.example.booktruck.models.User;
import com.example.booktruck.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ViewBook extends AppCompatActivity {

    private ListView bookListView;
    FirebaseFirestore db;
    CollectionReference userRef;
    private ArrayList<String> bookISBN = new ArrayList<String>();
    private ArrayList<String> bookArray = new ArrayList<String>();

    public ViewBook() {
    }

    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_layout);
        db = FirebaseFirestore.getInstance();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.view_book_layout, R.id.view_book_list, bookArray);

        DocumentReference docRef = db.collection("Users").document(getCurrentUsername());
        Log.i("Borrowed_Book","Successed");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("TASK","Successed");
                    Log.i("CURRENTUSER",getCurrentUsername());
                    DocumentSnapshot document = task.getResult();

                    if (document.exists() && document.getData().containsKey("borrowed")) {
                        Log.i("Document","Successed");

                        for (String ISBN : (ArrayList<String>) document.getData().get("borrowed")){
                            bookISBN.add(ISBN);
                            Log.i("Borrowed_Book",ISBN);
                            DocumentReference bookRef = db.collection("Books").document(ISBN);
                            bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " + document.getData().get("title").toString());
                                            Map<String, Object> data = document.getData();

                                            bookArray.add(data.get("title").toString());
                                        } else {
                                            Log.d("GET_BOOK_BY_ISBN", "No such document");
                                        }
                                    } else {
                                        Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                                    }
                                }
                            });
                            Log.i("ForLoop","Loding");
                        }
                        //Log.i("ForLoop","Loding");
                        bookListView = (ListView) findViewById(R.id.view_book_list);
                        bookListView.setAdapter(arrayAdapter);
                        Log.i("Array","show book");
                        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Book book = new Book("title","author","isbn");

                                Intent bookDetail = new Intent(ViewBook.this, ShowBookDetail.class);
                                bookDetail.putExtra("ParentClass", "ViewBook");

                                startActivity(bookDetail);

                            }
                        });
                    }
                }
            }
        });

        //get borrowed book




    }

    @Override
    protected void onStart() {
        super.onStart();



    }

}
