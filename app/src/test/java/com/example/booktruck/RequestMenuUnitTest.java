package com.example.booktruck;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//import org.apache.log4j.Logger;


public class RequestMenuUnitTest {

    FirebaseAuth mAuth;


    private RequestMenu mockRequestMenu() {
        RequestMenu requestMenu = new RequestMenu();
        return requestMenu;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword("test@gmail.com", "test");
    }

//    @Test
//    void testGetCurrentUserName() {
//        RequestMenu requestMenu = mockRequestMenu();
//        assertEquals("test@gmail.com", mAuth.getCurrentUser().getEmail());
//
//        requestMenu.setEmail("test@gamil.com");
//        assertEquals("test", requestMenu.getCurrentUsername());
//
//        requestMenu.setEmail("test222@gamil.com");
//        assertEquals("test222", requestMenu.getCurrentUsername());
//    }

    @Test
    void testShowBooks() {
        RequestMenu requestMenu = mockRequestMenu();
        ArrayList<String> bookList =new ArrayList<String> ();
        bookList.add("b1");
        bookList.add("b2");
        requestMenu.setBookArray(bookList);
//        requestMenu.getBookArray();
        assertEquals(bookList, requestMenu.getBookArray());
    }


}
