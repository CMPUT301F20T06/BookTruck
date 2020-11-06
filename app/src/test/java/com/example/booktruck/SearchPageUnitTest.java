package com.example.booktruck;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SearchPageUnitTest {

    FirebaseAuth mAuth;

    private SearchPage mockSearchPage() {
        SearchPage searchPage = new SearchPage();
        return searchPage;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
    }


    @Test
    void TestgetBookTitle() {
        SearchPage searchPage = new SearchPage();
        ArrayList<String> bookTitleList =new ArrayList<String> ();
        bookTitleList.add("title1");
        bookTitleList.add("title2");
        bookTitleList.add("title3");

        searchPage.setBookTitle(bookTitleList);
        assertEquals(bookTitleList, searchPage.getBookTitle());
    }


    @Test
    void TestgetBookISBN() {
        SearchPage searchPage = new SearchPage();
        ArrayList<String> bookISBNList =new ArrayList<String> ();
        bookISBNList.add("ISBN1");
        bookISBNList.add("ISBN2");
        bookISBNList.add("ISBN3");

        searchPage.setBookTitle(bookISBNList);
        assertEquals(bookISBNList, searchPage.getBookTitle());
    }

}
