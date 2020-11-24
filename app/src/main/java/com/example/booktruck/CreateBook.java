/*
 *  Classname: CreateBook
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Yifan Fan
 */
package com.example.booktruck;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.booktruck.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * CreateBook Class provides the EditText for user to input new book information
 * and it connects to the Cloud Firestore to save the new book into "Books" Collection
 */
public class CreateBook extends AppCompatActivity {

    private String ISBN;
    private String author;
    private String title;
    private EditText titleText;
    private EditText authorText;
    private EditText ISBNText;
    private Button uploadBtn, showBtn;
    private ImageView imageView;
    private ProgressBar progressBar;


    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    private FirebaseFirestore db;
    private CollectionReference bookRef;
    private CollectionReference userRef;

    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book);

        this.titleText = findViewById(R.id.bookName);
        this.authorText = findViewById(R.id.authorName);
        this.ISBNText = findViewById(R.id.ISBN_number);

        db = FirebaseFirestore.getInstance();
        bookRef = db.collection("Books");
        userRef = db.collection("Users");

        this.uploadBtn = findViewById(R.id.uploadImageBtn);
        this.showBtn = findViewById(R.id.viewImagesBtn);
        this.imageView = findViewById(R.id.imageView);
        this.progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chooseImage();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        showBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(CreateBook.this, ShowImage.class));
            }
        });
    }

    private void chooseImage(){
        Intent Intent = new Intent();
        Intent.setType("image/*");
        Intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadImage(){
        if (imageUri != null){
            uploadUriToFirebase(imageUri);
        }else{
            Toast.makeText(CreateBook.this, "Please Select Image !", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadUriToFirebase(Uri uri){

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UrlModel urlModel = new UrlModel(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(urlModel);
                        progressBar.setVisibility((View.INVISIBLE));
                        Toast.makeText(CreateBook.this, "Uploaded Successful !", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(CreateBook.this, "Uploading Filed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }





    /**
     *
     * @return current user's username
     */
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
     * @param title     new book title
     * @param author    new book author
     * @param ISBN      new Book ISBN number
     */
    public void createBook(String title, String author, String ISBN){
        Book book = new Book(title, author, ISBN);
        HashMap<String, Object> data = new HashMap<>();
        data.put("ISBN", book.getISBN());
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor());
        data.put("status", book.getStatus());
        data.put("borrower", book.getBorrower());
        data.put("requests", book.getRequests());
        data.put("owner", getCurrentUsername());

        // save a new book into Firebase collection "Books"
        bookRef.document(book.getISBN()).set(data);
    }

    /**
     *
     * @param ISBN  new book's ISBN number
     * addBookIntoOwnedList method will add the new book's ISBN into current user's own book list
     */
    public void addBookIntoOwnedList(String ISBN) {
        DocumentReference userRef = this.userRef.document(getCurrentUsername());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<String> ownedList = (ArrayList<String>) data.get("owned");
                        ownedList.add(ISBN);
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
     * @param view
     * onCreateBook method will be triggered when the "createBook" button onClick
     * onCreateBook method will connect to the Firestore and save the new book into "Books" Collection
     */
    public void onCreateBook(View view){
        this.author = authorText.getText().toString();
        this.title = titleText.getText().toString();
        this.ISBN = ISBNText.getText().toString();

        if (author.equals("") || title.equals("") || ISBN.equals("")){
            Toast.makeText(getApplicationContext(),"Book's title, author and ISBN must not be empty!",
                    Toast.LENGTH_SHORT).show();
        } else if (ISBN.length() < 13){
            Toast.makeText(getApplicationContext(),"Book's ISBN must have 13 digits!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            createBook(title, author, ISBN);
            addBookIntoOwnedList(this.ISBN);
        }

        Intent gotoDestination = new Intent(this, MainActivity.class);
        startActivity(gotoDestination);
    }

}
