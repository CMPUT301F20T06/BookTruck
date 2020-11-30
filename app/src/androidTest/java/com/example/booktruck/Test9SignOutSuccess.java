package com.example.booktruck;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
/**
 * Here is the test for testing all function in Sign Up page
 */

public class Test9SignOutSuccess {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSignOut(){
        solo.clickOnActionBarItem(R.id.action_logout);
        solo.waitForActivity(SignInActivity.class, 3000);
    }
}
