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
 * Here is the test for testing all function in Request page
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test6RequestMenu {
    private Solo solo;

    @Rule
    public ActivityTestRule<RequestMenu> rule = new ActivityTestRule<>(RequestMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testAFindNoBook() {
        solo.clickOnButton(0);
        solo.waitForActivity(SearchPage.class, 3000);

        solo.typeText((EditText) solo.getView(R.id.searchEditText), "AAAAAAAAAAAAAAAAAAAAAAA");
        solo.clickOnButton(0);
        solo.waitForText("No Book Found, Try Another Keyword", 1, 3000);
    }

    @Test
    public void testBFindOneBook() {
        solo.clickOnButton(0);
        solo.waitForActivity(SearchPage.class, 3000);

        solo.typeText((EditText) solo.getView(R.id.searchEditText),"Book For Test");
        solo.clickOnButton(0);
        solo.waitForActivity(SearchResult.class, 3000);

        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);
        solo.clickOnButton(0);
        solo.waitForText("You cannot request your own book!", 1, 3000);
    }
}