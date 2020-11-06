package com.example.booktruck;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;
/**
 * Here is the test for testing all function in my book list
 */
public class MyBookListTest {
    private Solo solo;

    public String generateString(){
        String str = "";
        Random rnd = new Random();
        for (int i=0; i<10; i++){
            char randomChar = (char) ('a' + rnd.nextInt(26));
            str += randomChar;
        }
        return str;
    }

    public String generateISBN(){
        String ISBN = "";
        Random rnd = new Random();
        for (int i=0; i<13; i++){
            int randomInt = rnd.nextInt();
            ISBN += String.valueOf(randomInt);
        }
        return ISBN;
    }

    @Rule
    public ActivityTestRule<MyBookList> rule = new ActivityTestRule<>(MyBookList.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testCreateBook(){
        solo.clickOnView(solo.getView(R.id.add_btn));
        solo.enterText((EditText) solo.getView(R.id.bookName), generateString());
        solo.enterText((EditText) solo.getView(R.id.authorName), generateString());
        solo.enterText((EditText) solo.getView(R.id.ISBN_number), generateISBN());
        solo.clickOnButton("Create the Book");
    }

    @Test
    public void testEditBook() {
        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);
        solo.clickOnButton("Edit");
        solo.enterText((EditText) solo.getView(R.id.editTitleView), generateString());
        solo.enterText((EditText) solo.getView(R.id.editAuthorView), generateString());
        solo.enterText((EditText) solo.getView(R.id.editISBNView), generateISBN());
        solo.clickOnButton("Confirm");
    }

    @Test
    public void testDeleteBook() {
        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);
        solo.clickOnButton("Delete");
    }

}