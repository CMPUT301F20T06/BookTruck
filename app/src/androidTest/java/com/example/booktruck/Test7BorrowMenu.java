package com.example.booktruck;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Here is the test for testing all function in borrow menu
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test7BorrowMenu {
    private Solo solo;

    @Rule
    public ActivityTestRule<BorrowMenu> rule = new ActivityTestRule<>(BorrowMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void testAViewBorrowedBook () {
        solo.clickOnButton(0);
    }

    @Test
    public void testBHandOverBooks() {
        solo.clickOnButton("Hand Over Books");
        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"");
        solo.clickOnButton(1);
        solo.waitForText("Please Enter ISBN",1,3000);

        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"1111111111111");
        solo.clickOnButton(1);
        solo.waitForActivity(ShowBookDetail.class);
        solo.waitForText("Book Not Found",1,3000);
    }

    @Test
    public void testCReceiveBooks() {
        solo.clickOnButton("Receive Book");
        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"");
        solo.clickOnButton(1);
        solo.waitForText("Please Enter ISBN",1 ,3000);

        solo.enterText((EditText) solo.getView(R.id.ISBNcode),"11111111111111");
        solo.clickOnButton(1);
        solo.waitForActivity(ShowBookDetail.class, 3000);
        solo.waitForText("Book Not Found", 1, 3000);
    }


}