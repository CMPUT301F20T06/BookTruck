package com.example.booktruck;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testSignupWithNopassword(){
        solo.enterText((EditText) solo.getView(R.id.signup_email),"tester");
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wong Activity", SignUpActivity.class);
    }

}
