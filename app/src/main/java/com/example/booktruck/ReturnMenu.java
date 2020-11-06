/*
 *  Classname: ReturnMenu
 *  Version: V1
 *  Date: 2020.11.02
 *  Copyright: Chuqing Fu
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*
 * ReturnMenu class provides buttons the user can click on to redirect to "return" relative pages.
 */
public class ReturnMenu extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_main);

        Button returnButton = (Button) findViewById(R.id.return_button);
        returnButton.setOnClickListener((View.OnClickListener) this);

        Button confirmReturnButton = (Button) findViewById(R.id.confirm_returning);
        confirmReturnButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_button:
                Intent gotoReturn = new Intent(this, ScanISBN.class);
                gotoReturn.putExtra("ParentClass", "Return");
                startActivity(gotoReturn);
                break;
            case R.id.confirm_returning:
                Intent gotoConfirmReturn = new Intent(this, ScanISBN.class);
                gotoConfirmReturn.putExtra("ParentClass", "ConfirmReturn");
                startActivity(gotoConfirmReturn);
                break;
        }}
}
