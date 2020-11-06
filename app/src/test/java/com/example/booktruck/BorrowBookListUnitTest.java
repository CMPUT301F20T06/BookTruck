package com.example.booktruck;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class BorrowBookListUnitTest {
    FirebaseAuth mAuth;

    private BorrowBookList mockBorrwoBookList() {
        BorrowBookList borrowBookList = new BorrowBookList();
        return borrowBookList;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
    }

//    @Test
//    void testGetCurrentUserName() {
//        BorrowBookList borrowBookList = mockBorrwoBookList();
////        assertEquals("test@gmail.com", mAuth.getCurrentUser().getEmail());
//
//        borrowBookList.setEmail("test@gamil.com");
////        assertEquals("test", borrowBookList.getCurrentUsername());
//
//        borrowBookList.setEmail("test222@gamil.com");
////        assertEquals("test222", borrowBookList.getCurrentUsername());
//
//    }

    @Test
    void testShowBooks() {
        BorrowBookList borrowBookList = mockBorrwoBookList();
        ArrayList<String> bookList =new ArrayList<String> ();
        bookList.add("b1");
        bookList.add("b2");
        borrowBookList.setBookArray(bookList);
//        requestMenu.getBookArray();
        assertEquals(bookList, borrowBookList.getBookArray());
    }



}
