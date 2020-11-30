package com.example.booktruck;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class Test3MainActivity {
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
        solo.clickOnButton(0);
        solo.waitForActivity(MyBookList.class, 3000);
    }

    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testRequest() {
        solo.clickOnButton(1);
        solo.waitForActivity(RequestMenu.class, 3000);
    }

    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testBorrow() {
        solo.clickOnButton(2);
        solo.waitForActivity(BorrowMenu.class, 3000);
    }
    /**
     * Test all activity and function in My Book Button
     **/
    @Test
    public  void testReturn() {
        solo.clickOnButton(3);
        solo.waitForActivity(ReturnMenu.class, 3000);
    }


    /**
     * test click-able items in action bar
     */
    @Test
    public void testProfileClickAble() {
        solo.clickOnActionBarItem(R.id.action_profile);
        solo.waitForActivity(ProfilePage.class, 3000);
    }

    @Test
    public void testNotificationClickAble() {
        solo.clickOnActionBarItem(R.id.action_notify);
        solo.waitForActivity(NotificationPage.class, 3000);
    }

}