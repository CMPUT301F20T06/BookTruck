/*
 *  Classname: ShowBookDetail
 *  Version: V3
 *  Date: 2020.11.05
 *  Copyright: Chuqing Fu, Xutong Li, Jiachen Xu, Yifan Fan, Yanlin Chen, Qi Song
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;

/*
 * ShowBookDetail extracts a specific book's information from the firebase database,
 * and it presents different components based on parent activity
 */
public class ShowBookDetail extends AppCompatActivity {

    private TextView authorText, statusText, ownerText, titleText, ISBNView;
    private String titleContent, authorContent, statusContent, ownerContent;
    private FirebaseFirestore db;
    private Button editImgBtn, editDescBtn, deleteBtn, returnBtn;
    private String ISBN;
    private DocumentReference bookRef;
    private CollectionReference userRef;

    /**
     *
     * @param savedInstanceState
     * onCreate method connects the firebase database and extracts one specific book info
     */
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
        Log.i("RECICVE_ISBN", ISBN);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
        bookRef = db.collection("Books").document(ISBN);

        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        getSupportActionBar().setTitle(titleContent);
                        titleText.setText(data.get("title").toString());
                        authorText.setText(data.get("author").toString());
                        statusText.setText(data.get("status").toString());
                        ownerText.setText(data.get("owner").toString());
                        ISBNView.setText(data.get("ISBN").toString());
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });


        String parentClass = String.valueOf(getIntent().getStringExtra("ParentClass"));

        if (parentClass.equalsIgnoreCase("Receive")) {
            // when borrower receive a book
            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_receiving);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("SHOW_BOOK_DETAIL", "RECEIVE_BOOK");
                    // check valid receive
                    checkValidReceiveAndDeleteISBNInAccepted();
                }
            });

            Button getLocationBtn = (Button) findViewById(R.id.locationButton);
            getLocationBtn.setText("Check Location");
            getLocationBtn.setVisibility(View.VISIBLE);
            getLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Click check Location", "Check Location from hand over books");
                    Intent intent2 = new Intent(ShowBookDetail.this, MapsActivity.class);
                    intent2.putExtra("ParentClass", "ReceiveBook");
                    intent2.putExtra("ISBN", ISBN);
                    startActivity(intent2);
                }
            });


        } else if (parentClass.equalsIgnoreCase("MyBookList")) {
            editDescBtn = findViewById(R.id.editDescButton);
            editImgBtn = findViewById(R.id.editImgButton);
            deleteBtn = findViewById(R.id.deleteButton);
            editDescBtn.setVisibility(View.VISIBLE);
            editImgBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);

        } else if (parentClass.equalsIgnoreCase("Return")) {
            returnBtn = findViewById(R.id.returnButton);
            returnBtn.setVisibility(View.VISIBLE);
            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnUpdate(v);
                    startActivity(new Intent(ShowBookDetail.this, ReturnMenu.class));
                }
            });
        } else if (parentClass.equalsIgnoreCase("ConfirmReturn")) {
            returnBtn = findViewById(R.id.confirmReturnButton);
            returnBtn.setVisibility(View.VISIBLE);
            returnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmReturnUpdate(v);
                    startActivity(new Intent(ShowBookDetail.this, ReturnMenu.class));
                }
            });
        } else if (parentClass.equalsIgnoreCase("HandOver")) {
            Button setLocationBtn = (Button) findViewById(R.id.locationButton);
            setLocationBtn.setText("Check Location");
            setLocationBtn.setVisibility(View.VISIBLE);
            setLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Click Specify Location", "Specify Location from hand over books");
                    Intent intent2 = new Intent(ShowBookDetail.this, MapsActivity.class);
                    intent2.putExtra("ParentClass", "HandOver");
                    intent2.putExtra("ISBN", ISBN);
                    startActivity(intent2);
                }
            });

            EditText latitudeText = (EditText) findViewById(R.id.latitudeText);
            EditText longitudeText = (EditText) findViewById(R.id.longitudeText);
            Button setXYBtn = (Button) findViewById(R.id.setXYButton);
            latitudeText.setVisibility(View.VISIBLE);
            longitudeText.setVisibility(View.VISIBLE);
            setXYBtn.setVisibility(View.VISIBLE);
            setXYBtn.setText(R.string.confirm_handover);
            setXYBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locationX = latitudeText.getText().toString();
                    Double valueX = Double.valueOf(locationX);
                    String locationY = longitudeText.getText().toString();
                    Double valueY = Double.valueOf(locationY);
                    Log.d("X,Y", valueX.toString() + valueY.toString());
                    if (valueY < -180 || valueY > 180 || valueX < -90 || valueX > 90) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid location!", Toast.LENGTH_SHORT).show();
                    } else {
                        GeoPoint location = new GeoPoint(valueX, valueY);
                        setStatusToHandOverAndUpdateLocation(location);
                        Intent intent = new Intent(ShowBookDetail.this, BorrowMenu.class);
                        startActivity(intent);
                    }
                }
            });
        } else if (parentClass.equalsIgnoreCase("SearchResult")){
            Button button = findViewById(R.id.requestButton);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // check if book is user's owned book add ISBN into user's requested list
                    addBookToRequestededList();
                }
            });
        }
    }

    /**
     * setStatusToHandOvered method can change the current book's status to "handovered"
     */
    public void setStatusToHandOverAndUpdateLocation(GeoPoint location){
        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        // set status to handovered
                        data.put("status", "handovered");
                        // update the location
                        data.put("location", location);
                        bookRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
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

    public void addUserInBookRequestAndSetStatusAsRequested() {

        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        String status = data.get("status").toString();
                        if (status.equals("available") || status.equals("requested")) {
                            data.put("status", "requested");
                            ArrayList<String> requests = ( ArrayList<String>) data.get("requests");
                            requests.add(getCurrentUsername());
                            bookRef.set(data);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "You cannot request an unavailable book!",
                                    Toast.LENGTH_SHORT).show();
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

    public void checkValidReceiveAndDeleteISBNInAccepted(){
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> acceptedList = (ArrayList<String>) data.get("accepted");
                        Boolean valid = acceptedList.contains(ISBN);
                        if (!valid) {
                            Toast.makeText(getApplicationContext(),
                                    "You do not have the permission to receive this book!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ShowBookDetail.this, BorrowMenu.class);
                            startActivity(intent);
                        } else {
                            acceptedList.remove(ISBN);
                            ArrayList<String> borrowedList = (ArrayList<String>) data.get("borrowed");
                            borrowedList.add(ISBN);
                            userRef.set(data);
                            // mark book Borrowed & set borrower to current user
                            setStatusToBorrowedAndSetBorrower();
                            Intent intent = new Intent(ShowBookDetail.this, BorrowMenu.class);
                            startActivity(intent);
                        }
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * setBorrower method updated the book's borrower to the current username
     */
    public void setBorrower() {
        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        data.put("borrower", getCurrentUsername());
                        bookRef.set(data);
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
     * setBorrowerToEmpty method delete the book's borrower
     */
    public void setBorrowerToEmpty(){
        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " + document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        data.put("borrower","");
                        bookRef.set(data);
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
     * deleteBookFromOwnedList method remove the book ISBN from the current user's owned book list
     */
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
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * deleteBookFromOwnedList method remove the book ISBN from the current user's accepted book list
     */
    public void deleteBookFromAcceptedList() {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> acceptedList = (ArrayList<String>) data.get("accepted");
                        acceptedList.remove(ISBN);
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

    /**
     *
     * @param view
     * onBookDetailDelete method is triggered when user want to delete the current book
     */
    public void onBookDetailDelete (View view) {
        bookRef.delete();
        deleteBookFromOwnedList();
        Intent gotoDestination = new Intent(this, MyBookList.class);
        startActivity(gotoDestination);

    }

    /**
     *
     * @param view
     * onBookDetailEdit method is triggered when user want to confirm the changes of book info
     */
    public void onBookDetailEdit(View view){
        Intent gotoDestination = new Intent(this, EditBook.class);
        gotoDestination.putExtra("ISBN", ISBN);
        startActivity(gotoDestination);
    }

    /**
     *
     * @param view
     * onBookImageEdit method is triggered when user want to add or delete book images.
     */
    public void onBookImageEdit(View view){
        Intent gotoDestination = new Intent(this, EditImage.class);
        gotoDestination.putExtra("ISBN", ISBN);
        startActivity(gotoDestination);
    }

    /**
     * returnUpdate method can update the database when a borrower returns the current book
     */
    public void returnUpdate(View view){
        String username = getCurrentUsername();
        DocumentReference userRef = this.userRef.document(username);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String status = (String) data.get("status");
                        String borrower = (String) data.get("borrower");
                        Log.d("status", status);
                        Log.d("borrower", borrower);
                        if (status.equals("borrowed") && borrower.equals(username)) {
                            bookRef.update("status", "returned");
                            bookRef.update("borrower", "");
                            userRef.update("borrowed", FieldValue.arrayRemove(ISBN));
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "You do not have access to return this book!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * confirmReturnUpdate method can update the database when an owner receives the current book
     */
    public void confirmReturnUpdate(View view){
        String ownerName = getCurrentUsername();
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String status = (String) data.get("status");
                        String owner = (String) data.get("owner");
                        Boolean valid = (status.equals("returned") && owner.equals(ownerName));
                        if (valid) {
                            bookRef.update("status", "available");
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "You do not have access to receive this book!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * setStatusToAvailable method can change the current book's status to "available"
     */
    public void setStatusToAvailable(){
        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        data.put("status","available");
                        bookRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    public void setStatusToBorrowedAndSetBorrower(){
        DocumentReference bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        data.put("status", "Borrowed");
                        data.put("borrower", getCurrentUsername());
                        bookRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * deleteBookFromBorrowedList method remove the ISBN from the current user's borrowed book list
     */
    public void deleteBookFromBorrowedList() {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> borrowedList = (ArrayList<String>) data.get("borrowed");
                        borrowedList.remove(ISBN);
                        userRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * addBookToBorrowedList method adds the ISBN to the current user's borrowed book list
     */
    public void addBookToBorrowedList() {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> borrowedList = (ArrayList<String>) data.get("borrowed");
                        borrowedList.add(ISBN);
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

    public void addBookToRequestededList(){
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> userData = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) userData.get("owned");
                        if (ownedList.contains(ISBN)) {
                            Toast.makeText(getApplicationContext(),
                                    "You cannot request your own book!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            // add username into book's requests list & set status as "requested"
                            DocumentReference bookRef = db.collection("Books").document(ISBN);
                            bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                                    document.getData().get("title").toString());
                                            Map<String, Object> bookData = document.getData();
                                            String status = bookData.get("status").toString();
                                            if (status.equals("available") || status.equals("requested")) {
                                                bookData.put("status", "requested");
                                                ArrayList<String> requestedList = (ArrayList<String>) userData.get("requested");
                                                requestedList.add(ISBN);
                                                userRef.set(userData);
                                                ArrayList<String> requests = ( ArrayList<String>) bookData.get("requests");
                                                requests.add(getCurrentUsername());
                                                bookRef.set(bookData);
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        "You cannot request an unavailable book!",
                                                        Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(ShowBookDetail.this, RequestMenu.class);
                        startActivity(intent);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }
}
