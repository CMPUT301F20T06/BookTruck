package com.example.booktruck;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.*;
/**
 * Here is the test for testing all function in borrow menu
 */
public class BorrowMenuTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<BorrowMenu> rule = new ActivityTestRule<>(BorrowMenu.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


}