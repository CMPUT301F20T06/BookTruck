package com.example.booktruck;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktruck.models.MyAdapter;
import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class EditImage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<UrlModel> list = new ArrayList<>();

    private MyAdapter mAdapter;
    private ImageView imageView;
    private Button uploadBtn;

    private ProgressBar progressBar;
    private Button refreshBtn;

    private FirebaseFirestore db;
    private String ISBN;
    private DocumentReference bookDoc;

    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_images);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        Intent gotoBook = getIntent();
        ISBN = gotoBook.getStringExtra("ISBN");

        db = FirebaseFirestore.getInstance();
        bookDoc = db.collection("Books").document(ISBN);
        Log.d("ISBN", String.valueOf(ISBN));

        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<UrlModel> images = (ArrayList<UrlModel>) data.get("images");
                        Log.d("LIST_OF_IMAGES", String.valueOf(images));
                        list = images;
                        showImages();
                    }
                }
            }
        });

        // using the URL array to generate a list of image. onClick => delete, cancel

        this.uploadBtn = findViewById(R.id.uploadImageBtn);
        this.imageView = findViewById(R.id.imageView);
        this.progressBar = findViewById(R.id.progressBar);
        this.refreshBtn = findViewById(R.id.refreshBtn);

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
        refreshBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                startActivity(getIntent());
            }
        });

    }

    protected void showImages(){
        mAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                new AlertDialog.Builder(EditImage.this)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this image?" )
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which ){
                                    deleteFromBookImages(position);
                                    mAdapter.deleteItem(position);
                                    Toast.makeText(EditImage.this,"Delete successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
                }
        });
    }

    public void deleteFromBookImages(int position){
        Log.d("ISBN FOR DELETE", String.valueOf(ISBN));
        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        ArrayList<UrlModel> images = (ArrayList<UrlModel>) data.get("images");
                        Log.d("LIST_BEFORE_DELETE", String.valueOf(images));
                        images.remove(position);
                        bookDoc.set(data);
                        Log.d("LIST_AFTER_DELETE", String.valueOf(images));
                    }
                }
            }
        });
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
        if (imageUri != null) {
            uploadUriToFirebase(imageUri);
        }
    }


    private void chooseImage(){
        Intent Intent = new Intent();
        Intent.setType("image/*");
        Intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent, 2);
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

    private void uploadUriToFirebase(Uri uri){
        StorageReference fileRef = reference.child(getCurrentUsername()).child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("URI", String.valueOf(uri));
                        // add image into Book database
                        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();
                                        ArrayList<String> images = (ArrayList<String>) data.get("images");
                                        images.add(String.valueOf(uri));
                                        bookDoc.set(data);
                                    }
                                }
                            }
                        });
                        progressBar.setVisibility((View.INVISIBLE));
                        Toast.makeText(EditImage.this, "Uploaded Successful !", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i("URI", "Fail");
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
                Toast.makeText(EditImage.this, "Uploading Filed !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }



}
