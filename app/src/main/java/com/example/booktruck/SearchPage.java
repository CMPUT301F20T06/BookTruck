package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SearchPage extends AppCompatActivity {

    Button serach_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        serach_btn = (Button) findViewById(R.id.search);
        serach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchPage.this,SearchResult.class);
                startActivity(intent);
            }
        });

    }
}