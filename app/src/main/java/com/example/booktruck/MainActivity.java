/*
 *  Classname: MainActivity
 *  Version: V1
 *  Date: 2020.10.20
 *  Copyright: Qi Song
 */
package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 *  MainActivity provides Four buttons to redirect to 4 different blocks: "My Book",
 *          "Request", "Borrow", and "Return" and 3 icons in the app bar to redirect to
 *          "profile", "notification" and "SignIn" page.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * checkAuth helps the MainActivity to check if the current user has SignIn.
     * if not, the app will automatically redirect the page to Sign In page.
     */
    public void checkAuth() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    /**
     * @param destinationClass an Activity class where it should redirect
     * navigate is a helper method for 4 buttons to redirect to different pages
     */
    // super class for go to next page button 
    public void navigate (Class destinationClass) {
        Intent gotoDestination = new Intent(this,
                destinationClass);
        startActivity(gotoDestination);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        checkAuth();
    }

    @Override
    // add actions icon in the app bar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // what action happens when use click on each icon
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // User chose the "logout" icon will sign out immediately
                FirebaseAuth.getInstance().signOut();
                checkAuth();
                return true;
            case R.id.action_profile:
                // click profile icon to go to profile page
                navigate(ProfilePage.class);
                return true;
            case R.id.action_notify:
                navigate(NotificationPage.class);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // button onClick to "Borrow" page
    public void onBorrow(View view) { navigate(BorrowMenu.class); }

    // button onClick to "Request" page
    public void onRequest(View view) { navigate(RequestMenu.class); }

    // button onClick to "My Book" page
    public void onMyBook(View view) { navigate(MyBookList.class); }

    // button onClick to "Return" page
    public void onReturn(View view) { navigate(ReturnMenu.class); }
}
