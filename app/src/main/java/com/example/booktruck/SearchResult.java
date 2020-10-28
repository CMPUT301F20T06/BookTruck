package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResult extends AppCompatActivity {

    ListView matchList;
    ArrayAdapter<String> matchAdapter;
    ArrayList<String> dataList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        matchList = findViewById(R.id.match_books);
        String []notifies = {"Match book 1", "Match book 2", "Match book 3", "Match book 4", "Match book 5"};
        dataList1 = new ArrayList<>();
        dataList1.addAll(Arrays.asList(notifies));
        matchAdapter = new ArrayAdapter<>(this, R.layout.content, dataList1);
        matchList.setAdapter(matchAdapter);

    }
}