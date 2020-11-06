/*
 *  Classname: ScanISBN
 *  Version: V2
 *  Date: 2020.11.05
 *  Copyright: Chuqing Fu, Xutong Li, Jiachen Xu, Yifan Fan, Yanlin Chen, Qi Song
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
 * ScanISBN class provides an EditText that user can input or Scan the ISBN barcode.
 */
public class ScanISBN extends AppCompatActivity implements View.OnClickListener {

    private EditText editISBN;
    private Button CodeSender;

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
        if (ISBN == ""){
            Toast.makeText(getApplicationContext(),"Please Enter ISBN",Toast.LENGTH_SHORT).show();
        }
        else {
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
//        Book book = BookService.getBookByISBN(ISBN);
//
//        if (book == null) {
//            Context context = getApplicationContext();
//            CharSequence warningText = "Book Not exist";
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context,warningText,duration);
//        }
//        else {
//            Intent gotoBook = new Intent(this, ShowBookDetail.class);
//            //gotoBook.putExtra("isbn", ISBN);
//            gotoBook.putExtra("ParentClass", "ReceiveBook");
//            startActivity(gotoBook);
//        }
    }
}



