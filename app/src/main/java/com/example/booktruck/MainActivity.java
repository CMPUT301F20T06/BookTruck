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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public void checkAuth() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    // super class for go to next page button 
    public void navigate (Class destination_class) {
        Intent gotoDestination = new Intent(this,
                destination_class);
        startActivity(gotoDestination);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
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

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //create a button to go to next page
    public void OnBorrow(View view) { 
        navigate(BorrowMenu.class);



    // create request button on main page (Xutong Li)

    public void OnRequest(View view) {navigate(RequestMenu.class);}

    public void onMyBook(View view) {navigate(MyBookList.class);}


    // click profile icon to go to profile page
    public void openProfile(MenuItem mItem) {navigate(ProfilePage.class);}

    // create a button to return page
    public void OnReturn(View view) {navigate(ReturnMenu.class);}
}
