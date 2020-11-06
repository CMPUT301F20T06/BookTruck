package com.example.booktruck;

import android.widget.EditText;

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
public class ReturnMenuTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RequestMenu> rule = new ActivityTestRule<>(RequestMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testReturnBook() {

        solo.clickOnButton("Return");
        solo.assertCurrentActivity("Return Menu Entered", ScanISBN.class);
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1111111111111");
        solo.clickOnButton("Enter");

    }

}