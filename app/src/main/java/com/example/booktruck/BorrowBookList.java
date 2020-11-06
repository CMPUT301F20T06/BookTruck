/*
 *  Classname: BorrowBookList
 *  Version: V2
 *  Date: 2020.11.01
 *  Copyright: Jiachen Xu
 */
package com.example.booktruck;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/*
 *  BorrowBookList class provides the information of current user's borrowed books
 */
public class BorrowBookList extends AppCompatActivity {

    private ListView bookListView;
    private FirebaseFirestore db;
    private ArrayList<String> bookISBN = new ArrayList<>();
    private ArrayList<String> bookArray = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;


    public static String email = "";
    public static void setEmail(String email) {
        BorrowBookList.email = email;
    };

    public String getCurrentUsername() {
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        // for junit test
        if (email == "") {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
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
     * onCreate method connects to the Cloud Firestore to get the current user's borrowed book list,
     * then use the ISBN in the list to extract information of those books in "Books" collection.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book_layout);
        db = FirebaseFirestore.getInstance();
        bookListView = findViewById(R.id.view_book_list);

        DocumentReference docRef = db.collection("Users").document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists() && document.getData().containsKey("borrowed")) {
                        final ArrayList<String> list = (ArrayList<String>) document.getData().get("borrowed");
                        for (int i=0; i<list.size(); i++) {
                            final String ISBN = list.get(i);
                            DocumentReference bookRef = db.collection("Books").document(ISBN);
                            final int finalI = i;
                            bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " + document.getData().get("title").toString());
                                            Map<String, Object> data = document.getData();
                                            bookArray.add(data.get("title").toString());
                                            bookISBN.add(ISBN);
                                            if (finalI == list.size()-1) {
                                                showBooks();
                                            }
                                        } else {
                                            Log.d("GET_BOOK_BY_ISBN", "No such document");
                                        }
                                    } else {
                                        Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    protected void showBooks() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, bookArray);
        bookListView.setAdapter(arrayAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent bookDetail = new Intent(BorrowBookList.this, ShowBookDetail.class);
                bookDetail.putExtra("ParentClass", "ViewBook");
                bookDetail.putExtra("ISBN", bookISBN.get(position));
                startActivity(bookDetail);
            }
        });
    }
}
