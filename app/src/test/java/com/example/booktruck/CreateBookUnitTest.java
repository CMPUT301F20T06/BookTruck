package com.example.booktruck;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateBookUnitTest {

    FirebaseAuth mAuth;


    private CreateBook mockCreateBook() {
        CreateBook createBook = new CreateBook();
        return createBook;
    };

    @Before
    public void signIn(){
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void TestGetISBN() {
        CreateBook createBook = mockCreateBook();
        createBook.setISBN("ISBN001");
        assertEquals("ISBN001", createBook.getISBN());
        createBook.setISBN("ISBN002");
        assertEquals("ISBN002", createBook.getISBN());
        createBook.setISBN("ISBN003");
        assertEquals("ISBN003", createBook.getISBN());
    }

    @Test
    public void TestGetAuthor() {
        CreateBook createBook = mockCreateBook();

        createBook.setAuthor("Author_1");
        assertEquals("Author_1", createBook.getAuthor());
        createBook.setAuthor("Author_2");
        assertEquals("Author_2", createBook.getAuthor());
        createBook.setAuthor("Author_3");
        assertEquals("Author_3", createBook.getAuthor());

    }


}
