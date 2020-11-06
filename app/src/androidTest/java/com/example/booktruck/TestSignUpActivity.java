package com.example.booktruck;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.By;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestSignUpActivity {

    private Solo solo;

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
        solo.enterText((EditText) solo.getView(R.id.signup_email),"testaccount3");
        solo.enterText((EditText) solo.getView(R.id.signup_contact),"testcontact3");
        solo.enterText((EditText) solo.getView(R.id.signup_password),"testpassword3");
        solo.clickOnButton("Sign Up");
        solo.waitForView(R.id.myBook);
    }

}
