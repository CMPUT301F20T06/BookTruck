/*
 *  Classname: ScanISBN
 *  Version: V2
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/*
 * ScanISBN class provides an EditText that user can input or Scan the ISBN barcode.
 */

public class ScanISBN extends AppCompatActivity implements View.OnClickListener {

    private EditText editISBN;
    private Button CodeSender;
    FirebaseFirestore db;
    DocumentReference docRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isbn_receive);

        editISBN = (EditText) findViewById(R.id.ISBNcode);

        CodeSender = (Button) findViewById(R.id.Code_Sender);
        CodeSender.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String ISBN = editISBN.getText().toString();
        if (ISBN.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter ISBN", Toast.LENGTH_SHORT).show();
        } else {
            String parentClass = String.valueOf(getIntent().getStringExtra("ParentClass"));
            Intent gotoBook = new Intent(this, ShowBookDetail.class);
            if (parentClass.equalsIgnoreCase("BorrowHandOver")) {
                gotoBook.putExtra("ParentClass", "HandOver");
            } else if (parentClass.equalsIgnoreCase("BorrowReceive")) {
                gotoBook.putExtra("ParentClass", "Receive");

            } else if (parentClass.equalsIgnoreCase("Return")) {
                gotoBook.putExtra("ParentClass", "Return");
            } else if (parentClass.equalsIgnoreCase("ConfirmReturn")) {
                gotoBook.putExtra("ParentClass", "ConfirmReturn");
            }
            gotoBook.putExtra("ISBN", ISBN);
            startActivity(gotoBook);
        }

    }
}

