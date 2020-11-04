package com.example.booktruck.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.booktruck.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookService {

    FirebaseFirestore db;
    CollectionReference bookRef;
    private UserService userService;

    public BookService () {
        db = FirebaseFirestore.getInstance();
        bookRef = db.collection("Books");
        userService = new UserService();
    }

    // create a book and save it into firebase
    public void createBook(String title, String author, String ISBN){
        Book book = new Book(title, author, ISBN);
        HashMap<String, Object> data = new HashMap<>();
        data.put("ISBN", book.getISBN());
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor());
        data.put("status", book.getStatus());
        data.put("borrower", book.getBorrower());
        data.put("requests", book.getRequests());
        data.put("owner", userService.getCurrentUsername());

        // save a new book into Firebase collection "Books"
        bookRef.document(book.getISBN()).set(data);
    }

    public Book getBookByISBN(String ISBN) {
        final Book[] book = new Book[1];
        final DocumentReference docRef = bookRef.document(ISBN);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i("GET_BOOK_BY_ISBN", "The book exists!");
                        Map<String, Object> data = document.getData();
                        Log.i("GET_BOOK_BY_ISBN", data.toString());
                        book[0] = new Book(
                                data.get("title").toString(), data.get("author").toString(),
                                data.get("ISBN").toString(),
                                data.get("status").toString(), data.get("owner").toString(),
                                data.get("borrower").toString(), (ArrayList<String>) data.get("requests"));
                    } else {
                        Log.i("GET_BOOK_BY_ISBN", "The book does not exist!");
                    }
                }
            }
        });
        Log.i("GET_BOOK_BY_ISBN", book[0].getTitle());
        return book[0];
    }

    public void changeBookStatus(String ISBN, String status){
        final DocumentReference docRef = bookRef.document(ISBN);
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);
        docRef.set(data);
    }

    public void changeBorrower(String ISBN, String username) {
        final DocumentReference docRef = bookRef.document(ISBN);
        Map<String, Object> data = new HashMap<>();
        data.put("borrower", username);
        docRef.set(data);
    }

    public ArrayList<String> getRequestsOfBook(String ISBN) {
        final ArrayList<String> usernames = new ArrayList<>();
        final DocumentReference docRef = bookRef.document(ISBN);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.getData().containsKey("requests")) {
                        for (String username : (ArrayList<String>) document.getData().get("requests")){
                            usernames.add(username);
                        }
                    }
                }
            }
        });
        return usernames;
    }

    public void deleteRequest(String ISBN, String username) {
        final DocumentReference docRef = bookRef.document(ISBN);
        final String name = username;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = new HashMap<>();
                        // check book request list, if the username is there, delete it
                        if (document.getData().containsKey("requests")) {
                            ArrayList<String> requests = (ArrayList<String>) document.getData().get("requests");
                            for (int i = 0; i < requests.size(); i++) {
                                if (requests.get(i).equals(name)) {
                                    requests.remove(i);
                                }
                            }
                            data.put("requests", requests);
                        }
                        docRef.set(data);
                    }
                }
            }
        });
    }

}
