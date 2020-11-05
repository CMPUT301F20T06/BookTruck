package com.example.booktruck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.Book;
import com.example.booktruck.models.User;
import com.example.booktruck.services.BookService;
import com.example.booktruck.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Code;

import java.util.ArrayList;
import java.util.Map;

public class ReceiveBook extends AppCompatActivity {

    private EditText editISBN;
    private Button CodeSender;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    ArrayList<String> Books;
/*
    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    public ArrayList<String> getAcceptedBooks() {
        ArrayList<String> AcceptedBooks = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists() && document.getData().containsKey("accepted")) {
                        for (String ISBN : (ArrayList<String>) document.getData().get("accepted")) {
                            AcceptedBooks.add(ISBN);
                        }
                    }
                }
            }
        });
        return AcceptedBooks;
    }

*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isbn_receive);

        editISBN = (EditText) findViewById(R.id.ISBNcode);
        CodeSender = (Button) findViewById(R.id.Code_Sender);

        CodeSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ISBN = editISBN.getText().toString();
                //Books = getAcceptedBooks();
                Toast.makeText(getApplicationContext(),ISBN,Toast.LENGTH_SHORT).show();
                //Intent gotoBook = new Intent(ReceiveBook.this, ShowBookDetail.class);
                //gotoBook.putExtra("ISBN", ISBN);
                //startActivity(gotoBook);


            }
        });

    }

}



