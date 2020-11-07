package com.example.booktruck;

import android.widget.EditText;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

/**
 * Here is the test for testing all function in Sign Up page
 */

@LargeTest
public class SignOutSuccess {

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
        solo.waitForActivity(SignUpActivity.class);
    }
}
