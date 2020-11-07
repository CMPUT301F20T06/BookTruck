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
 * Here is the test for testing all function in Request page
 */
@LargeTest
public class RequestMenuTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RequestMenu> rule = new ActivityTestRule<>(RequestMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testFindBook() {
        solo.clickOnButton("Find");
        solo.waitForActivity(SearchPage.class);

        solo.typeText((EditText) solo.getView(R.id.searchEditText),"null");
        solo.clickOnButton("Search");
        solo.waitForText("No Book Found, Try Another Keyword");

        solo.typeText((EditText) solo.getView(R.id.searchEditText),"Example");
        solo.clickOnButton("Search");
        solo.waitForActivity(SearchResult.class);

        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);
        solo.clickOnButton("Request");

    }
}