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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private Button editBtn, deleteBtn, returnBtn;
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
                    Log.i("SHOW_BOOK_DETAIL", "REVEIVE_BOOK");
                    // check valid receive
                    checkValidReceiveAndDeleteISBNInAccepted();
                }
            });
        } else if (parentClass.equalsIgnoreCase("MyBookList")) {
            editBtn = findViewById(R.id.editButton);
            deleteBtn = findViewById(R.id.deleteButton);
            editBtn.setVisibility(View.VISIBLE);
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
            Button button = (Button) findViewById(R.id.confirmButton);
            button.setText(R.string.confirm_handover);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("SHOW_BOOK_DETAIL", "HAND_OVER_BOOK");
                    setStatusToHandOvered();
                    Intent intent = new Intent(ShowBookDetail.this, BorrowMenu.class);
                    startActivity(intent);
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
     * returnUpdate method can update the database when a borrower returns the current book
     */
    public void returnUpdate(View view){
        String username = getCurrentUsername();
        DocumentReference userRef = this.userRef.document(username);

        validateReturnAction(username);

        bookRef.update("status", "returned");
        userRef.update("borrowed", FieldValue.arrayRemove(ISBN));
        bookRef.update("borrower", "");
    }

    /**
     * confirmReturnUpdate method can update the database when an owner receives the current book
     */
    public void confirmReturnUpdate(View view){

        validateConfirmReturnAction();
        bookRef.update("status", "available");
    }

    /**
     * validateReturnAction method can validate a return action by checking the book status and borrower
     */
    public void validateReturnAction(final String borrowerName) {
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    String status = (String) data.get("status");
                    String borrower = (String) data.get("borrower");
                    Boolean valid = (status.equals("borrowed") && borrower.equals(borrowerName));
                    //Log.d("status", status);
                    //Log.d("borrower", borrower);
                    if (!valid) {
                        Toast.makeText(getApplicationContext(),
                                "You do not have the permission to receive this book!",
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

    /**
     * validateConfirmReturnAction method can validate a confirm return action by checking the book status and owner
     */
    public void validateConfirmReturnAction() {
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
                        if (!valid) {
                            Toast.makeText(getApplicationContext(),
                                    "You do not have the permission to receive this book!",
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
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * setStatusToHandOvered method can change the current book's status to "handovered"
     */
    public void setStatusToHandOvered(){
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
                        data.put("status", "handovered");
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
                        Map<String, Object> data = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) data.get("owned");
                        if (ownedList.contains(ISBN)) {
                            Toast.makeText(getApplicationContext(),
                                    "You cannot request your own book!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            ArrayList<String> requestedList = (ArrayList<String>) data.get("requested");
                            requestedList.add(ISBN);
                            userRef.set(data);
                            // add username into book's requests list & set status as "requested"
                            addUserInBookRequestAndSetStatusAsRequested();
                        }
                        Intent intent = new Intent(ShowBookDetail.this, RequestMenu.class);
                        startActivity(intent);
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
