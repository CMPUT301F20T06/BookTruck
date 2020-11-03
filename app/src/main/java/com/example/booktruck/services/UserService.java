package com.example.booktruck.services;

import androidx.annotation.NonNull;

import com.example.booktruck.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    static FirebaseUser firebaseUser;
    static CollectionReference userRef;

    public UserService(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");
    }

    public FirebaseUser getCurrentUser() {
        return firebaseUser;
    }

    public static String getCurrentUsername() {
        String email = firebaseUser.getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    public void logout(){
        firebaseUser = null;
        FirebaseAuth.getInstance().signOut();
    }

    public static ArrayList<Book> getBorrowedBook() {
        final ArrayList<Book> books = new ArrayList<>();
        final DocumentReference docRef = userRef.document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.getData().containsKey("borrowed")) {
                        for (String ISBN : (String[]) document.getData().get("borrowed")){
                            books.add(BookService.getBookByISBN(ISBN));
                        }
                    }
                }
            }
        });
        return books;
    }

    public static ArrayList<Book> getOwnedBook() {
        final ArrayList<Book> books = new ArrayList<>();
        final DocumentReference docRef = userRef.document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.getData().containsKey("owned")) {
                        for (String ISBN : (String[]) document.getData().get("owned")){
                            books.add(BookService.getBookByISBN(ISBN));
                        }
                    }
                }
            }
        });
        return books;
    }

    public static ArrayList<Book> getRequestedBook() {
        final ArrayList<Book> books = new ArrayList<>();
        final DocumentReference docRef = userRef.document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && document.getData().containsKey("requested")) {
                        for (String ISBN : (String[]) document.getData().get("requested")){
                            books.add(BookService.getBookByISBN(ISBN));
                        }
                    }
                }
            }
        });
        return books;
    }

    public static void handOverBook(String ISBN, String username){
        // change the book status to "accepted"
        BookService.changeBookStatus(ISBN, "accepted");
        // check if the request is in the book, delete it
        BookService.deleteRequest(ISBN, username);
    }

    public static void receiveBook(String ISBN) {
        // change book status to "borrowed"
        BookService.changeBookStatus(ISBN, "borrowed");
        // change the borrower in the book
        BookService.changeBorrower(ISBN, getCurrentUsername());
        // add book into user borrowed list and delete it in requested list
        addBorrowWithDeletingRequest(ISBN);
    }

    public static void returnBook(String ISBN) {
        // change the book status to "returned"
        BookService.changeBookStatus(ISBN, "returned");
    }

    public static void confirmReturn(String ISBN, String username) {
        // change the book status to "active"
        BookService.changeBookStatus(ISBN, "active");
        // delete the book in user's borrowed list
        deleteBookInBorrowedList(ISBN, username);
        // delete the book's borrower
        BookService.changeBorrower(ISBN, "");
    }

    private static void addBorrowWithDeletingRequest(String ISBN){
        final String bookISBN = ISBN;
        final DocumentReference docRef = userRef.document(getCurrentUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = new HashMap<>();
                        // check user request list, if the book is there, delete it
                        if (document.getData().containsKey("requested")){
                            ArrayList<String> requested = (ArrayList<String>) document.getData().get("requested");
                            for(int i=0; i<requested.size(); i++) {
                                if (requested.get(i).equals(bookISBN)) {
                                    requested.remove(i);
                                }
                            }
                            data.put("requested", requested);
                        }
                        // add the book into user's borrowed list
                        ArrayList<String> borrowed = (ArrayList<String>) document.getData().get("borrowed");
                        borrowed.add(bookISBN);
                        data.put("borrowed", borrowed);
                        docRef.set(data);
                    }
                }
            }
        });
    }

    private static void deleteBookInBorrowedList(String ISBN, String username){
        final String bookISBN = ISBN;
        final DocumentReference docRef = userRef.document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = new HashMap<>();
                        // check user request list, if the book is there, delete it
                        if (document.getData().containsKey("borrowed")){
                            ArrayList<String> borrowed = (ArrayList<String>) document.getData().get("requested");
                            for(int i=0; i<borrowed.size(); i++) {
                                if (borrowed.get(i).equals(bookISBN)) {
                                    borrowed.remove(i);
                                }
                            }
                            data.put("borrowed", borrowed);
                        }
                        docRef.set(data);
                    }
                }
            }
        });
    }
}
