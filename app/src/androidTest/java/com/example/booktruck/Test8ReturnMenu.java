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
 * Here is the test for testing all function in return menu
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test8ReturnMenu {
    private Solo solo;

    @Rule
    public ActivityTestRule<ReturnMenu> rule = new ActivityTestRule<>(ReturnMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testAReturnBook() {
        solo.clickOnButton(0);

        // Test Empty Input
        solo.clickOnButton(1);
        solo.waitForText("Please Enter ISBN", 1, 3000);
    }

    @Test
    public void testBReturnBook() {
        solo.clickOnButton(0);

        // Test Wrong Input
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1111111111111");
        solo.clickOnButton(1);
        solo.waitForText("Book Not Found", 1, 3000);
    }

    @Test
    public void testCConfirmReturnBook() {
        solo.clickOnButton("Confirm Returning");

        // Test Empty Input
        solo.clickOnButton(1);
        solo.waitForText("Please Enter ISBN",1 , 3000);
    }

    @Test
    public void testDConfirmReturnBook() {
        solo.clickOnButton("Confirm Returning");

        // Test Wrong Input
        solo.enterText((EditText) solo.getView(R.id.ISBNcode), "1111111111111");
        solo.clickOnButton(1);
        solo.waitForText("Book Not Found",1,3000);
    }
}
