package com.example.booktruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class showRequestInDetail extends AppCompatActivity {

    ListView requestListView;
    Button accept_Request, reject_Request;
    ArrayAdapter<String> arrayAdapter;
    FirebaseFirestore db;
    ArrayList<String> whoSentRequestList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_request_in_detail);
        requestListView = findViewById(R.id.RequestList);
        accept_Request = findViewById(R.id.accept_botton);
        reject_Request = findViewById(R.id.reject_botton);

        Intent bookIntent = getIntent();
        String ISBN = bookIntent.getStringExtra("ISBN");


        db = FirebaseFirestore.getInstance();
        DocumentReference bookRef = db.collection("Books").document(ISBN);
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

    protected void showRequestInDetail() {
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, whoSentRequestList);
        requestListView.setAdapter(arrayAdapter);

        Intent bookIntent = getIntent();
        String ISBN = bookIntent.getStringExtra("ISBN");

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if owner rejects the request
                reject_Request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //navigate to the rejected borrower, and update this borrower in database after rejection
                        DocumentReference rejectedPersonRef = db.collection("Users").document(whoSentRequestList.get(position));

                        rejectedPersonRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();

                                        Log.i("CHECK",whoSentRequestList.get(position));
                                        ArrayList<String> requests = (ArrayList<String>) document.getData().get("requested");
                                        requests.remove(ISBN);
                                        rejectedPersonRef.set(data);
                                    }
                                }
                            }
                        });
                        //update book in database after reject


                        //remove this borrower in listView
                        whoSentRequestList.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                    }
                });

                /*
                accept_Request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                 */
/*
                Intent requestDetail = new Intent(NotificationPage.this, showRequestInDetail.class);
                //requestDetail.putExtra("ListOfPeopleWhoRequest",requestArray);
                //requestDetail.putExtra("ParentClass", "ViewBook");
                requestDetail.putExtra("ISBN", bookISBN.get(position));
                startActivity(requestDetail);
*/
            }
        });
    }
}