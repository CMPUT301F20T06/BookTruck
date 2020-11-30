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

/**
 * Here is the test for testing all function in Sign In page
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test2SignInActivity {

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
    public ActivityTestRule<SignInActivity> rule = new ActivityTestRule<>(SignInActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testASigninWithInfoEmpty(){
        solo.enterText((EditText) solo.getView(R.id.signin_email),"fake_email");
        solo.clickOnButton(1);
        solo.waitForText("Username or Password must not be empty!",1,3000);
    }

    @Test
    public void testBSigninWithWrongPassword(){
        solo.enterText((EditText) solo.getView(R.id.signin_email), generateString());
        solo.enterText((EditText) solo.getView(R.id.signin_password), generateString());
        solo.clickOnButton(1);
        solo.waitForText("Try again",1,3000);
    }

    @Test
    public void testCSigninWithRightPassword(){
        solo.enterText((EditText) solo.getView(R.id.signin_email),"account_for_test");
        solo.enterText((EditText) solo.getView(R.id.signin_password),"password");
        solo.clickOnButton(1);
        solo.waitForView(R.id.myBook, 1, 3000);
    }

}
