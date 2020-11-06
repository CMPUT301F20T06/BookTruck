/*
 *
 *
 *
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ShowBookDetail extends AppCompatActivity {

    TextView authorText, statusText, ownerText, titleText, ISBNView;
    String titleContent, authorContent, statusContent, ownerContent;
    FirebaseFirestore db;
    Button editBtn, deleteBtn;
    String ISBN;

    DocumentReference bookRef;
    CollectionReference userRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);

        //显示的书必须满足条件
        //必需是owner拥有的书
        titleText = findViewById(R.id.titleView);
        authorText = findViewById(R.id.authorView);
        statusText = findViewById(R.id.statusView);
        ownerText = findViewById(R.id.ownerView);
        ISBNView = findViewById(R.id.ISBNView);

        Intent gotoBook = getIntent();
        ISBN = gotoBook.getStringExtra("ISBN");
        Log.i("RECICVE_ISBN",ISBN);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        bookRef = db.collection("Books").document(ISBN);

        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " + document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        getSupportActionBar().setTitle(titleContent);
                        titleText.setText("Title: "+ data.get("title").toString());
                        authorText.setText("Author: "+ data.get("author").toString());
                        statusText.setText("Status: "+ data.get("status").toString());
                        ownerText.setText("Owner: "+ data.get("owner").toString());
                        ISBNView.setText("ISBN: "+ data.get("ISBN").toString());
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });



        //如果找不到ISBN， 需要给出找不到的提示

        //在找到书之后，根据对应的parent 显示不同的界面
        //1. hand over a book, 需要明确一个pick up 地址，然后确认借出去
        //2. receive a book, 可以查看pick up location，然后确认收到
        //3. view book， 显示所有借到的书

        String parentClass = String.valueOf(getIntent().getStringExtra("ParentClass"));

        if(parentClass.equalsIgnoreCase("Receive")) {

            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_receiving);

        }
        else if (parentClass.equalsIgnoreCase("ViewBook")) {

        } else if (parentClass.equalsIgnoreCase("MyBookList")) {
            editBtn = findViewById(R.id.editButton);
            deleteBtn = findViewById(R.id.deleteButton);
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else { // parentClass == "HandOver"
            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_handover);
        }
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

    public void deleteBookFromOwnedList() {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) data.get("owned");
                        ownedList.remove(ISBN);
                        userRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    public void onBookDetailDelete (View view) {
        bookRef.delete();
        deleteBookFromOwnedList();
        Intent gotoDestination = new Intent(this, MyBookList.class);
        startActivity(gotoDestination);

    }

    public void onBookDetailEdit(View view){
        Intent gotoDestination = new Intent(this, EditBook.class);
        gotoDestination.putExtra("ISBN", ISBN);
        startActivity(gotoDestination);
    }
}
