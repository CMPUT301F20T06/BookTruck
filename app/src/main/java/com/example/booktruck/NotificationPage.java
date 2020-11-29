/*
 *  Classname: NotificationPage
 *  Version: V3
 *  Date: 2020.11.05
 *  Copyright: Yanlin Chen, Qi Song
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/**
 *notification page show current user two types of notification he received:
 * 1, acceptance of his request on other books
 * 2, request from other users on his own books
 */
public class NotificationPage extends AppCompatActivity {

    FirebaseFirestore db;
    static CollectionReference userRef;
    static CollectionReference bookRef;
    DocumentReference userDoc;

    private ArrayList<String> bookISBN = new ArrayList<>();
    private ArrayList<String> bookStatus = new ArrayList<>();
    private ArrayList<String> bookArray = new ArrayList<>();
    private ArrayList<String> combined = new ArrayList<>();
    private ArrayList<String> requestArray = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView notifyListView;
    private int ownedSize;
    private int acceptedSize;

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
        setContentView(R.layout.notification_page);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        userDoc = userRef.document(getCurrentUsername());

        notifyListView = findViewById(R.id.notify_list);
        ArrayList<String> requestedList = new ArrayList<>();
        DocumentReference userDoc = userRef.document(getCurrentUsername());

        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                //find books by corresponding ISBN
                if (document.exists()) {

                    ArrayList<String> ownedBooks = (ArrayList<String>) document.getData().get("owned");
                    ArrayList<String> acceptedBooks = (ArrayList<String>) document.getData().get("accepted");

                    if(ownedBooks != null){
                        if (ownedBooks.size() != 0){
                            ownedSize = ownedBooks.size();
                            combined.addAll(ownedBooks);
                            Log.i("OWNEDSIZE",String.valueOf(ownedSize));
                        }
                    }
                    else{
                        ownedSize = 0;
                    }

                    if(acceptedBooks != null){
                        if (acceptedBooks.size() != 0){
                            acceptedSize = acceptedBooks.size();
                            combined.addAll(acceptedBooks);
                            Log.i("ACCEPTESIZE",String.valueOf(acceptedSize));
                        }
                    }
                    else{
                        acceptedSize = 0;
                    }


                    /**
                     * combine all the books current user owned and books whose requests are accepted by owner to "combined" list
                     * books user owned and requested and books which are agreed to be borrowed
                     * they all need to put on notification page
                     */
                    for (String ISBN : combined){
                        DocumentReference bookRef = db.collection("Books").document(ISBN);
                        Task<DocumentSnapshot> documentSnapshotTask = bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        if (document.getData().get("status").toString().equals("requested")) {
                                            ArrayList<String> the_array = (ArrayList<String>) document.getData().get("requests");
                                            Log.d("REQUESTED_BOOK", the_array.toString());
                                            if (the_array.size() != 0) {
                                                Map<String, Object> data = document.getData();
                                                Log.d("REQUESTED_BOOK", data.get("title").toString());
                                                bookArray.add("Requested: \n" + data.get("title").toString());
                                                bookStatus.add(data.get("status").toString());
                                                bookISBN.add(ISBN);
                                            }
                                        } else if (document.getData().get("status").toString().equals("accepted")) {
                                            Map<String, Object> data = document.getData();
                                            Log.d("ACCEPTED_BOOK", data.get("title").toString());
                                            bookArray.add("Accepted: \n" + data.get("title").toString());
                                            bookStatus.add(data.get("status").toString());
                                            bookISBN.add(ISBN);
                                        }
                                        if (combined.get(combined.size() - 1).equals(ISBN)) {
                                            showRequestInDetail();
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * put different notification information(either acceptance or request from other users) in ListView
     * if current user tap on acceptance he received, it will turn to the page showing book detail(ShowBookDetail)
     * if current user tap on request, it will redirect to page ShowRequestInDetail, where he has the option to
     * either accept the request or decline it.
     */
    protected void showRequestInDetail() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, bookArray);
        notifyListView.setAdapter(arrayAdapter);
        notifyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (bookStatus.get(position).equals("accepted")) {
                    Intent bookDetail = new Intent(NotificationPage.this, ShowBookDetail.class);
                    bookDetail.putExtra("ParentClass", "AcceptedBook");
                    bookDetail.putExtra("ISBN", bookISBN.get(position));
                    startActivity(bookDetail);
                } else {
                    Intent bookDetail = new Intent(NotificationPage.this, ShowRequestInDetail.class);
                    bookDetail.putExtra("ISBN", bookISBN.get(position));
                    startActivity(bookDetail);
                }
            }
        });
    }
}

