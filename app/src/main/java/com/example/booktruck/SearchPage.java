/*
 *
 *
 *
 */
package com.example.booktruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {

    private EditText searchEditText;
    private FirebaseFirestore db;
    private ArrayList<String> bookISBN;
    private ArrayList<String> bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        db = FirebaseFirestore.getInstance();
        searchEditText = findViewById(R.id.searchEditText);
    }

    public void onSearchButtonClick(View view) {
        String keyword = searchEditText.getText().toString();
        bookISBN = new ArrayList<>();
        bookTitle = new ArrayList<>();
        db.collection("Books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Boolean never = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SEARCH_BOOKS", "FOUND BOOK: "+
                                        document.get("ISBN").toString());
                                String title = document.get("title").toString();
                                String author = document.get("author").toString();
                                if (title.toLowerCase().contains(keyword.toLowerCase()) ||
                                        author.toLowerCase().contains(keyword.toLowerCase())) {
                                    never = false;
                                    bookISBN.add(document.get("ISBN").toString());
                                    bookTitle.add(document.get("title").toString());
                                    Intent intent=new Intent(SearchPage.this,SearchResult.class);
                                    int number = bookISBN.size();
                                    intent.putExtra("number", String.valueOf(number));
                                    for (int i=0; i<number; i++) {
                                        intent.putExtra(String.valueOf(i)+"ISBN", bookISBN.get(i));
                                        intent.putExtra(String.valueOf(i)+"Title", bookTitle.get(i));
                                    }
                                    startActivity(intent);
                                }
                            }
                            if (never) {
                                Toast.makeText(getApplicationContext(),"No Book Found, Try Another Keyword",
                                        Toast.LENGTH_SHORT).show();
                                searchEditText.setText("");
                            }
                        } else {
                            Log.d("SEARCH_BOOKS",
                                    "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}