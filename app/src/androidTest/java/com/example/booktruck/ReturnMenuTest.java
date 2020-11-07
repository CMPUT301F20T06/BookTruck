package com.example.booktruck;

import android.widget.EditText;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Here is the test for testing all function in return menu
 */
@LargeTest
public class ReturnMenuTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ReturnMenu> rule = new ActivityTestRule<>(ReturnMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testReturnBook() {

        solo.clickOnButton("Return");

        // Test Empty Input
        solo.clickOnButton("Enter");
        solo.waitForText("Please Enter ISBN");

        // Test Wrong Input
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1");
        solo.clickOnButton("Enter");
        solo.waitForText("Book Not Found");

        // Test Book with no access
        solo.goBack();
        solo.sendKey(Solo.DELETE);
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1111111111111");
        solo.clickOnButton("Enter");
        solo.clickOnButton("Confirm");
        solo.waitForText("You do not have access to return this book!");

    }

    @Test
    public void testConfirmReturnBook() {

        solo.clickOnButton("Confirm Returning");

        // Test Empty Input
        solo.clickOnButton("Enter");
        solo.waitForText("Please Enter ISBN");

        // Test Wrong Input
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1");
        solo.clickOnButton("Enter");
        solo.waitForText("Book Not Found");

        // Test Book with no access
        solo.goBack();
        solo.sendKey(Solo.DELETE);
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1111111111111");
        solo.clickOnButton("Enter");
        solo.clickOnButton("Confirm");
        solo.waitForText("You do not have access to receive this book!");

    }

}