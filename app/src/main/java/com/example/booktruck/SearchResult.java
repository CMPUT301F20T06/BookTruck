/*
 *
 *
 *
 */
package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchResult extends AppCompatActivity {

    ListView matchList;
    ArrayAdapter<String> matchAdapter;
    ArrayList<String> bookISBN;
    ArrayList<String> bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        matchList = findViewById(R.id.match_books);
        bookISBN = new ArrayList<>();
        bookTitle = new ArrayList<>();

        Intent intent = getIntent();
        int size = Integer.parseInt(intent.getStringExtra("number"));
        for (int i=0; i<size; i++) {
            bookISBN.add(intent.getStringExtra(String.valueOf(i)+"ISBN"));
            bookTitle.add(intent.getStringExtra(String.valueOf(i)+"Title"));
        }
        matchAdapter = new ArrayAdapter<>(this, R.layout.content, bookTitle);
        matchList.setAdapter(matchAdapter);
        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent bookDetail = new Intent(SearchResult.this, ShowBookDetail.class);
                bookDetail.putExtra("ParentClass", "SearchResult");
                bookDetail.putExtra("ISBN", bookISBN.get(position));
                startActivity(bookDetail);
            }
        });
    }
}
