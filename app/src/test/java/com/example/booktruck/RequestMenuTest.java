package com.example.booktruck;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//import org.apache.log4j.Logger;


public class RequestMenuTest {


    private FirebaseAuth mAuth;

    private RequestMenu mockRequestMenu() {
        RequestMenu requestMenu = new RequestMenu();
        return requestMenu;
    };
    @Before
    public void signIn(){
        mAuth.signInWithEmailAndPassword("test@gmail.com", "test");
    }

    @Test
    void testGetCurrentUserName() {
        RequestMenu requestMenu = mockRequestMenu();

        requestMenu.setEmail("test@gamil.com");
        assertEquals("test", requestMenu.getCurrentUsername());

        requestMenu.setEmail("test222@gamil.com");
        assertEquals("test222", requestMenu.getCurrentUsername());

    }

    @Test
    void testShowBooks() {


    }


}
