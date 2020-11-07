package com.example.booktruck;
import androidx.test.filters.SmallTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SignUpSuccess.class,
        MyBookTest.class,
        ProfilePageTest.class,
        MainActivityTest.class,
        RequestMenuTest.class,
        BorrowMenuTest.class,
        ReturnMenuTest.class,
        SignOutSuccess.class
})

public class SuiteTest {
}
