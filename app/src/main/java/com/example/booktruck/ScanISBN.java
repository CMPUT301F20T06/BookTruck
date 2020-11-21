/*
 *  Classname: ScanISBN
 *  Version: V2
 *  Date: 2020.11.05
 *  Copyright: Chuqing Fu, Xutong Li, Jiachen Xu, Yifan Fan, Yanlin Chen, Qi Song
 */
package com.example.booktruck;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/*
 * ScanISBN class provides an EditText that user can input or Scan the ISBN barcode.
 */

public class ScanISBN extends AppCompatActivity implements View.OnClickListener {

    private EditText editISBN;
    private Button CodeSender;
    private Button CodeScanner;
    private Uri imgUri;
    private String ISBN;
    private InputImage image;
    FirebaseFirestore db;
    DocumentReference docRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isbn_receive);

        editISBN = (EditText) findViewById(R.id.ISBNcode);

        CodeSender = (Button) findViewById(R.id.Code_Sender);
        CodeSender.setOnClickListener(this);

        CodeScanner = (Button) findViewById(R.id.Code_Scanner);
        CodeScanner.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Code_Sender:
                ISBN = editISBN.getText().toString();
                break;
            case R.id.Code_Scanner:
                selectImg();
                break;
        }
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

    private void selectImg() {
        Log.i("img", "start");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Log.i("img", "selecting");
        startActivityForResult(intent, 1);
        Log.d("img", "start scanning?");
        scanBarcode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("img", "in listener");
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Log.d("img", "passed");
            imgUri = data.getData();
        }
    }

    private void scanBarcode() {
        // set detector options
        Log.d("img", "scanning");
        BarcodeScannerOptions option =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_EAN_13)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(option);

        try {
            image = InputImage.fromFilePath(getApplicationContext(), imgUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // [START run_detector]
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        for (Barcode barcode: barcodes) {
                            ISBN = barcode.getRawValue();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Barcode Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

