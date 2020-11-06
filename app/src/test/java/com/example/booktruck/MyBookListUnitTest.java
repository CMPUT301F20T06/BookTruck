package com.example.booktruck;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class MyBookListUnitTest {
    FirebaseAuth mAuth;

    private MyBookList mockMyBookList() {
        MyBookList myBookList = new MyBookList();
        return myBookList;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void TestGetBookISBN() {
        MyBookList myBookList = mockMyBookList();
        ArrayList<String> bookISBNList =new ArrayList<String> ();
        bookISBNList.add("ISBN001");
        bookISBNList.add("ISBN002");
        bookISBNList.add("ISBN003");
        myBookList.setBookISBN(bookISBNList);
        assertEquals(bookISBNList, myBookList.getBookISBN());
    }

    @Test
    public void TestGetBookArray() {
        MyBookList myBookList = mockMyBookList();
        ArrayList<String> bookList =new ArrayList<String> ();
        bookList.add("Book1");
        bookList.add("Book2");
        bookList.add("Book3");
        myBookList.setBookISBN(bookList);
        assertEquals(bookList, myBookList.getBookISBN());
    }

    @Test
    public void TestGetBookStatus() {
        MyBookList myBookList = mockMyBookList();
        ArrayList<String> bookStatus =new ArrayList<String> ();
        bookStatus.add("ISBN001");
        bookStatus.add("ISBN002");
        bookStatus.add("ISBN003");
        myBookList.setBookISBN(bookStatus);
        assertEquals(bookStatus, myBookList.getBookISBN());
    }

}
