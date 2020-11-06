/*
 *  Classname: RequestMenu
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Xutong Li
 */
package com.example.booktruck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;

/*
 * RequestMenu class provides the information of current user's requested books.
 */
public class RequestMenu extends AppCompatActivity {

    private ListView bookListView;
    FirebaseFirestore db;
    private ArrayList<String> bookISBN = new ArrayList<>();
    private ArrayList<String> bookArray = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


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
     * @param savedInstanceState
     * onCreate method connects to the cloud firestore and extract
     * the current user's requested book list, then it uses the ISBN list to
     * load information of those books in the "Books" collection
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_menu);
        db = FirebaseFirestore.getInstance();
        bookListView = findViewById(R.id.bookList_name);

        Button btn1;
        btn1 = (Button) findViewById(R.id.find_book);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestMenu.this, SearchPage.class);
                startActivity(intent);
            }
        });

        DocumentReference docRef = db.collection("Users").document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists() && document.getData().containsKey("requested")) {
                        ArrayList<String> list = (ArrayList<String>) document.getData().get("requested");
                        for (int i=0; i<list.size(); i++) {
                            String ISBN = list.get(i);
                            int finalI = i;
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
//                                            bookArray.add(data.get("title").toString());
                                            String title_status = data.get("title").toString() +
                                                    " ---- " + data.get("status").toString();
                                            bookArray.add(title_status);
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

    public void setBookArray(ArrayList<String> bookArray) {
        this.bookArray = bookArray;
    }

    protected void showBooks() {

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.content, bookArray);
        bookListView.setAdapter(arrayAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent bookDetail = new Intent(RequestMenu.this, ShowBookDetail.class);
                bookDetail.putExtra("ParentClass", "ViewBook");
                bookDetail.putExtra("ISBN", bookISBN.get(position));
                startActivity(bookDetail);
            }
        });
        Log.i("LOGG",bookISBN.toString());
    }

    public ArrayList<String> getBookArray() {
        return bookArray;
    }
}

