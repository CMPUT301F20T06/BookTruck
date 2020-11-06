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

import static org.junit.Assert.*;
/**
 * Here is the test for testing all function in my book list
 */
public class MyBookListTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MyBookList> rule = new ActivityTestRule<>(MyBookList.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testCreateBook(){

        solo.clickOnView(solo.getView(R.id.add_btn));
        solo.enterText((EditText) solo.getView(R.id.bookName),"ExampleBookName");
        solo.enterText((EditText) solo.getView(R.id.authorName),"Example Author");
        solo.enterText((EditText) solo.getView(R.id.ISBN_number),"1234567891234");

        solo.clickOnButton("Create the Book");
    }

    @Test
    public void testEditBook() {

        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);

        solo.clickOnButton("Edit");

        solo.enterText((EditText) solo.getView(R.id.bookName),"ExampleBookEdit");
        solo.enterText((EditText) solo.getView(R.id.authorName),"Edit Author");
        solo.enterText((EditText) solo.getView(R.id.ISBN_number),"9876543219874");
        solo.clickOnButton("Confirm");


    }

    @Test
    public void TestDeleteBook() {

    }

}