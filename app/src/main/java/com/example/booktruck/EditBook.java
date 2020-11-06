/*
 *  Classname: CreateBook
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Yifan Fan
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/*
 * EditBook Class provides the EditText for user to change his/her book information
 * and it connects to the Cloud Firestore to save the changed information into "Books" Collection
 */
public class EditBook extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText title;
    private EditText author;
    private EditText isbn;
    private String ISBN;

    private DocumentReference bookDoc;
    private CollectionReference bookRef;
    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_detail);

        Intent gotoBook = getIntent();
        ISBN = gotoBook.getStringExtra("ISBN");

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        bookRef = db.collection("Books");
        bookDoc = db.collection("Books").document(ISBN);

        title = findViewById(R.id.editTitleView);
        author = findViewById(R.id.editAuthorView);
        isbn = findViewById(R.id.editISBNView);

        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        setUpData(data.get("author").toString(),data.get("title").toString(),
                                data.get("ISBN").toString());
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     *
     * @param author    current book's author
     * @param title     current book's title
     * @param isbn      current book's ISBN number
     * setUpData methods initialize the current book's information
     */
    public void setUpData(String author, String title, String isbn){
        this.title.setText(title);
        this.author.setText(author);
        this.isbn.setText(isbn);

    }

    /**
     *
     * @param view
     * confirmEditData method will update the current book details based on user's input
     */
    public void confirmEditData(View view){
        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data = document.getData();
                        data.put("author",author.getText().toString());
                        data.put("title",title.getText().toString());
                        data.put("ISBN",isbn.getText().toString());

                        // delete old ISBN and add new ISBN into user's owned list
                        deleteAndAddBookFromOwnedList(isbn.getText().toString());
                        // create new book
                        bookRef.document(isbn.getText().toString()).set(data);
                        // delete the old Book
                        bookDoc.delete();

                        Intent gotoDestination = new Intent(EditBook.this, MainActivity.class);
                        startActivity(gotoDestination);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });

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

    /**
     *
     * @param newISBN   current book's new ISBN number
     * deleteAndAddBookFromOwnedList method will delete the old ISBN number and add the new ISBN
     * number into the current user's owned book list.
     */
    public void deleteAndAddBookFromOwnedList(String newISBN) {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) data.get("owned");
                        ownedList.add(newISBN);
                        ownedList.remove(ISBN);
                        userRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "cannot find the user");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }
}
