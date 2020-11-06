package com.example.booktruck;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Test displayed functions
     * Test all activity and function in My Book Button
     **/
    @Test
    public void testClickMyBook() {
        solo.clickOnButton("My Books");
        solo.waitForActivity(MyBookList.class);
    }


    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testRequest() {
        solo.clickOnButton("Request");
        solo.waitForActivity(RequestMenu.class);
    }

    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testBorrow() {
        solo.clickOnButton("Borrow");
        solo.waitForActivity(BorrowMenu.class);
    }
    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testReturn() {
        solo.clickOnButton("Return");
        solo.waitForActivity(ReturnMenu.class);
    }


    /**
     * test click-able items in action bar
     */
    @Test
    public void testLogOutClickAble() {
        solo.clickOnActionBarItem(R.id.action_logout);
        solo.waitForActivity(SignUpActivity.class);
    }
    @Test
    public void testProfileClickAble() {
        solo.clickOnActionBarItem(R.id.action_profile);
        solo.waitForActivity(ProfilePage.class);
    }

    @Test
    public void testNotificationClickAble() {
        solo.clickOnActionBarItem(R.id.action_notify);
        solo.waitForActivity(NotificationPage.class);
    }

}