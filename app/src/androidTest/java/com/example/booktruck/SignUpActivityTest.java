package com.example.booktruck;

import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class SignUpActivityTest {

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
    public void testSignupWithInfoEmpty(){
        solo.enterText((EditText) solo.getView(R.id.signup_email),"fake_email");
        solo.clickOnButton("Sign Up");
        solo.waitForText("Username, Email or Password must not be empty!");
    }

    @Test
    public void testSignupWithTheExistingAccount(){
        solo.enterText((EditText) solo.getView(R.id.signup_email),"fake_email");
        solo.enterText((EditText) solo.getView(R.id.signup_contact),"fake_contact");
        solo.enterText((EditText) solo.getView(R.id.signup_password),"fake_password");
        solo.clickOnButton("Sign Up");
        solo.waitForText("Try again");
    }

    @Test
    public void testSignupWithRightPassword(){
        solo.enterText((EditText) solo.getView(R.id.signup_email), generateString());
        solo.enterText((EditText) solo.getView(R.id.signup_contact), generateString());
        solo.enterText((EditText) solo.getView(R.id.signup_password), generateString());
        solo.clickOnButton("Sign Up");
        solo.waitForView(R.id.myBook);
    }

}
