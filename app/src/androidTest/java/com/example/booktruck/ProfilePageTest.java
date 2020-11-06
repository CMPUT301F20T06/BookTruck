package com.example.booktruck;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

/**
 * Here is the test for testing all function in my profile page
 */

public class ProfilePageTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<ProfilePage> rule = new ActivityTestRule<>(ProfilePage.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testEditProfile(){
        solo.clickOnView(solo.getView(R.id.EditProfile));
        solo.enterText((EditText) solo.getView(R.id.email_addresss), generateString());
        solo.clickOnButton("Confirm");
    }

    private String generateString() {
        String str = "";
        Random rnd = new Random();
        for (int i=0; i<10; i++){
            char randomChar = (char) ('a' + rnd.nextInt(26));
            str += randomChar;
        }
        return str;
    }

}
