/*
 *  Classname: showRequestInDetail
 *  Version: V3
 *  Date: 2020.11.05
 *  Copyright: Yanlin Chen, Qi Song
 */
package com.example.booktruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;

/**
 * show request in detail
 * The current user has the option to either accept the request on his book
 * or decline it
 */
public class ShowRequestInDetail extends AppCompatActivity {

    private ListView requestListView;
    private Button accept_Request, reject_Request;
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseFirestore db;
    private ArrayList<String> whoSentRequestList;
    private String ISBN;
    private DocumentReference bookRef;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_request_in_detail);
        requestListView = findViewById(R.id.RequestList);
        accept_Request = findViewById(R.id.accept_botton);
        reject_Request = findViewById(R.id.reject_botton);

        Intent bookIntent = getIntent();
        ISBN = bookIntent.getStringExtra("ISBN");
        whoSentRequestList =  new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        bookRef = db.collection("Books").document(ISBN);
        //show all borrowers who sent request on this book
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        for(String whoSentRequest: (ArrayList<String>) document.getData().get("requests")){
                            whoSentRequestList.add(whoSentRequest);
                        }
                        showRequestInDetail();
                    }
                }
            }
        });
    }

    /**
     * manipulate database according to current user's different option on the requests he received
     * both books and users collections are manipulation
     * based on how the current user decide.
     */
    protected void showRequestInDetail() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, whoSentRequestList);
        requestListView.setAdapter(arrayAdapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private View lastView;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.WHITE);
                }
                lastView = view;
                view.setBackgroundColor(Color.GRAY);

                //if owner accepts on of the requests
                accept_Request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(String username : whoSentRequestList){
                            DocumentReference whoRequestRef = db.collection("Users").document(username);
                            whoRequestRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Map<String, Object> individualData = document.getData();
                                            ArrayList<String> requestedList = (ArrayList<String>) individualData.get("requested");
                                            requestedList.remove(ISBN);
                                            if (username.equals(whoSentRequestList.get(position))) {
                                                ArrayList<String> acceptedList = (ArrayList<String>) individualData.get("accepted");
                                                acceptedList.add(ISBN);
                                            }
                                            whoRequestRef.set(individualData);
                                            if (username.equals(whoSentRequestList.get(whoSentRequestList.size()-1))) {
                                                bookRef = db.collection("Books").document(ISBN);
                                                bookRef.update("status","accepted");
                                                bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()){
                                                                Map<String, Object> data = document.getData();
                                                                ArrayList<String> requests = (ArrayList<String>) data.get("requests");
                                                                requests.clear();
                                                                bookRef.set(data);
                                                                Intent intent = new Intent(ShowRequestInDetail.this, NotificationPage.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });

                //if owner rejects the request
                reject_Request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //navigate to the rejected borrower, and update this borrower in database after rejection
                        DocumentReference rejectedPersonRef = db.collection("Users").document(whoSentRequestList.get(position));
                        rejectedPersonRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();
                                        ArrayList<String> requestedList = (ArrayList<String>) data.get("requested");
                                        requestedList.remove(ISBN);
                                        rejectedPersonRef.set(data);
                                        deleteRequests(whoSentRequestList.get(position));
                                        //remove this borrower in listView
                                        whoSentRequestList.remove(position);
                                        showRequestInDetail();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * For the requested book, remove the rejected username from the "requests" list
     * @param username
     */
    public void deleteRequests(String username){
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> requests = (ArrayList<String>) data.get("requests");
                        requests.remove(username);
                        bookRef.set(data);
                    }
                }
            }
        });
    }
}
