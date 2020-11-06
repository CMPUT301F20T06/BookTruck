package com.example.booktruck;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class SearchResultUnitTest {

    FirebaseAuth mAuth;

    private SearchResult mockSearchResult() {
        SearchResult searchResult = new SearchResult();
        return searchResult;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    void TestgetBookTitle() {
        SearchResult searchResult = new SearchResult();
        ArrayList<String> bookTitleList =new ArrayList<String> ();
        bookTitleList.add("title1");
        bookTitleList.add("title2");
        bookTitleList.add("title3");

        searchResult.setBookTitle(bookTitleList);
        assertEquals(bookTitleList, searchResult.getBookTitle());
    }

    @Test
    void TestgetBookISBN() {
        SearchResult searchResult = new SearchResult();
        ArrayList<String> bookISBNList =new ArrayList<String> ();
        bookISBNList.add("ISBN1");
        bookISBNList.add("ISBN2");
        bookISBNList.add("ISBN3");

        searchResult.setBookTitle(bookISBNList);
        assertEquals(bookISBNList, searchResult.getBookTitle());
    }


}
