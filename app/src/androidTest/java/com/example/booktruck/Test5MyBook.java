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
 * Here is the test for testing all function in my book list
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test5MyBook {
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

    public String generateISBN(){
        String ISBN = "";
        Random rnd = new Random();
        for (int i=0; i<13; i++){
            int randomInt = Math.abs(rnd.nextInt());
            ISBN += String.valueOf(randomInt);
        }
        return ISBN;
    }

    @Rule
    public ActivityTestRule<MyBookList> rule = new ActivityTestRule<>(MyBookList.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testACreateBook(){
        solo.clickOnView(solo.getView(R.id.add_btn));
        solo.enterText((EditText) solo.getView(R.id.bookName), "Book For Delete");
        solo.enterText((EditText) solo.getView(R.id.authorName), generateString());
        solo.enterText((EditText) solo.getView(R.id.ISBN_number), generateISBN());
        solo.clickOnButton(0);
    }

    @Test
    public void testBEditBook() {
        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class, 3000);
        solo.clickOnButton(0);
        solo.waitForActivity(EditBook.class, 3000);
        solo.enterText((EditText) solo.getView(R.id.editAuthorView), generateString());
        solo.enterText((EditText) solo.getView(R.id.editISBNView), generateISBN());
        solo.clickOnButton(0);
    }

    @Test
    public void testCDeleteBook() {
        solo.clickInList(0,0);
        solo.waitForActivity(ShowBookDetail.class);
        solo.clickOnButton(2);
    }

    @Test
    public void testDCreateBook(){
        solo.clickOnView(solo.getView(R.id.add_btn));
        solo.enterText((EditText) solo.getView(R.id.bookName), "Book For Test");
        solo.enterText((EditText) solo.getView(R.id.authorName), generateString());
        solo.enterText((EditText) solo.getView(R.id.ISBN_number), generateISBN());
        solo.clickOnButton(0);
    }

}