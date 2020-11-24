package com.example.booktruck;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Here is the test for testing all function in borrow menu
 */

public class Test7BorrowMenu {
    private Solo solo;

    @Rule
    public ActivityTestRule<BorrowMenu> rule = new ActivityTestRule<>(BorrowMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void testViewBorrowedBook () {
        solo.clickOnButton("Borrowed Books");
    }

    @Test
    public void testHandOverBooks() {
        solo.clickOnButton("Hand Over Books");
        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"");
        solo.clickOnButton("Enter");
        solo.waitForText("Please Enter ISBN");

        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"123");
        solo.clickOnButton("Enter");
        solo.waitForActivity(ShowBookDetail.class);
        solo.waitForText("Book Not Found");
    }

    @Test
    public void testReceiveBooks() {
        solo.clickOnButton("Receive Book");
        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"");
        solo.clickOnButton("Enter");
        solo.waitForText("Please Enter ISBN");

        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"123");
        solo.clickOnButton("Enter");
        solo.waitForActivity(ShowBookDetail.class);
        solo.waitForText("Book Not Found");
    }


}