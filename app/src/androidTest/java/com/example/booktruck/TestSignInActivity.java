package com.example.booktruck;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestSignInActivity {

    private Solo solo;

    @Rule
    public ActivityTestRule<SignUpActivity> rule = new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testSignupWithInfoEmpty(){
        solo.enterText((EditText) solo.getView(R.id.signin_email),"fake_email");
        solo.clickOnButton("Sign In");
        solo.waitForText("Username or Password must not be empty!");
    }

    @Test
    public void testSignupWithWrongPassword(){
        solo.enterText((EditText) solo.getView(R.id.signin_email),"fake_email");
        solo.enterText((EditText) solo.getView(R.id.signin_password),"fake_password");
        solo.clickOnButton("Sign In");
        solo.waitForText("Try again");
    }

    @Test
    public void testSignupWithRightPassword(){
        solo.enterText((EditText) solo.getView(R.id.signin_email),"testaccount");
        solo.enterText((EditText) solo.getView(R.id.signin_password),"testpassword");
        solo.clickOnButton("Sign In");
        solo.waitForView(R.id.myBook);
    }

}
