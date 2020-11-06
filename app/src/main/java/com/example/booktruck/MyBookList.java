/*
 *  Classname: MyBookList
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Yifan Fan
 */
package com.example.booktruck;

import  android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/*
 * MyBookList class provides a list of book that the current user owns.
 * And it also provides a button which can create new book.
 */
public class MyBookList extends AppCompatActivity {

    private ListView bookListView;
    FirebaseFirestore db;
    private ArrayList<String> bookISBN = new ArrayList<>();
    private ArrayList<String> bookArray = new ArrayList<>();
    private ArrayList<String> bookStatus = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    /**
     *
     * @return current user's username
     */
    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    /**
     *
     * @param savedInstanceState
     * onCreate method connects to the Cloud Firestore to get the current user's owned book list
     * in the "Users" collection, then, it gets the access to the "Books" Collection to get all
     * information of the books by the owned book list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybook_list);
        db = FirebaseFirestore.getInstance();
        bookListView = findViewById(R.id.bookList);

        FloatingActionButton add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBookList.this, CreateBook.class);
                startActivity(intent);

            }
        });

        DocumentReference docRef = db.collection("Users").document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists() && document.getData().containsKey("owned")) {
                        ArrayList<String> list = (ArrayList<String>) document.getData().get("owned");
                        for (int i=0; i<list.size(); i++) {
                            String ISBN = list.get(i);
                            DocumentReference bookRef = db.collection("Books").document(ISBN);
                            int finalI = i;
                            bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("GET_BOOK_BY_ISBN",
                                                    "DocumentSnapshot data: "+
                                                            document.getData().get("title").toString());
                                            Map<String, Object> data = document.getData();
                                            bookArray.add(data.get("title").toString());
                                            bookStatus.add(data.get("status").toString());
                                            bookISBN.add(ISBN);
                                            if (finalI == list.size()-1) {
                                                showBooks();
                                            }
                                        } else {
                                            Log.d("GET_BOOK_BY_ISBN", "No such document");
                                        }
                                    } else {
                                        Log.d("GET_BOOK_BY_ISBN", "get failed with ",
                                                task.getException());
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }

    /**
     * showBooks methods will adapt the book information ArrayList to ViewList in the layout
     * and when user clicks on the specific book, it will redirect to the book details page by
     * the book ISBN indexing
     */
    protected void showBooks() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, bookArray);
        bookListView.setAdapter(arrayAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent bookDetail = new Intent(MyBookList.this, ShowBookDetail.class);
                bookDetail.putExtra("ParentClass", "MyBookList");
                bookDetail.putExtra("ISBN", bookISBN.get(position));
                startActivity(bookDetail);
            }
        });
    }
}


