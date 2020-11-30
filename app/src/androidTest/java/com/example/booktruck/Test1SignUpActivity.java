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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Here is the test for testing all function in Sign Up page
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test1SignUpActivity {

    private Solo solo;

    public String generateString(){
        String str = "";
        Random rnd = new Random();
        for (int i=0; i<10; i++){
            char randomChar = (char) ('a' + rnd.nextInt(26));
            str += randomChar;
        }
        return str;
    }

    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testASignupWithInfoEmpty(){
        solo.enterText((EditText) solo.getView(R.id.signup_email),"fake_email");
        solo.clickOnButton(1);
        solo.waitForText("Username, Email or Password must not be empty!",1,3000);
    }

    @Test
    public void testBSignupWithTheExistingAccount(){
        solo.enterText((EditText) solo.getView(R.id.signup_email),"qsong");
        solo.enterText((EditText) solo.getView(R.id.signup_contact),"kaysong@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signup_password),"qsong1");
        solo.clickOnButton(1);
        solo.waitForText("Try again",1,3000);
    }

    @Test
    public void testCSignupWithRightPassword(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), generateString());
        solo.enterText((EditText) solo.getView(R.id.signup_contact), generateString());
        solo.enterText((EditText) solo.getView(R.id.signup_password), generateString());
        solo.clickOnButton(1);
        solo.waitForView(R.id.myBook, 1, 3000);
    }
}

