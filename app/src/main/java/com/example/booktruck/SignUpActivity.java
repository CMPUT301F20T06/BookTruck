/*
 *  Classname: SignUpActivity
 *  Version: V1
 *  Date: 2020.10.20
 *  Copyright: Qi Song
 */
package com.example.booktruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.booktruck.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/*
 *  SignUpActivity provides Sign Up and Sign In methods.
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signUpEmailText;
    private EditText signUpContactText;
    private EditText signInEmailText;
    private EditText signUpPasswordText;
    private EditText signInPasswordText;
    private Button signUpVisible;
    private Button signInVisible;
    private FirebaseFirestore db;
    private User newUser;

    /**
     *
     * @param view
     * signUp provides Sign Up method
     *  1. check if the information is valid
     *  2. use firebase authentication email and password method
     *  3. save user information into cloud firestore database "Users" collection
     */
    public void signUp(View view){
        String email = signUpEmailText.getText().toString().toLowerCase();
        String contact = signUpContactText.getText().toString();
       String password = signUpPasswordText.getText().toString();
        if (password.equals("") || contact.equals("") || email.equals("")) {
            Toast.makeText(getApplicationContext(),"Username, Email or Password must not be empty!", Toast.LENGTH_SHORT).show();
        } else {
            // create a new user
            newUser = new User(email, contact, password);
            mAuth.createUserWithEmailAndPassword(email + "@gmail.com", password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignUp", "createUserWithUsername:success");

                                // then save user information into User Database
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("username", newUser.getUsername());
                                data.put("email", newUser.getEmail());
                                data.put("password", newUser.getPassword());
                                data.put("borrowed", newUser.getBorrowed());
                                data.put("requested", newUser.getRequested());
                                data.put("accepted", newUser.getAccepted());
                                data.put("returned", newUser.getReturned());
                                data.put("owned", newUser.getOwned());

                                db = FirebaseFirestore.getInstance();
                                final CollectionReference collectionReference = db.collection("Users");
                                collectionReference
                                        .document(newUser.getUsername())    // using user id as the document id
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("SaveUser", "Data has been added successfully!");
                                                NavUtils.navigateUpFromSameTask(SignUpActivity.this);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("SaveUser", "Data could not be added!" + e.toString());
                                            }
                                        });

                            } else {
                                // If sign in fails, display a message to the user.
                                String error = task.getException().getMessage();
                                Log.i("SIGNUP:failure", error);
                                // if the username is in use
                                if (error == "The email address is already in use by another account.") {
                                    new AlertDialog.Builder(SignUpActivity.this)
                                            .setTitle("The username is already in use by another account")
                                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    signUpPasswordText.setText("");
                                                    signUpEmailText.setText("");
                                                }
                                            }).show();
                                } else {
                                    // if the password format is wrong
                                    new AlertDialog.Builder(SignUpActivity.this)
                                            .setTitle("Password should be at least 6 characters")
                                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    signUpPasswordText.setText("");
                                                }
                                            }).show();
                                }
                            }
                        }
                    });
        }
    }

    /**
     *
     * @param view
     * signIn method uses Friebase Authentication to verify the user's username and password
     */
    public void signIn(View view){
        String email = signInEmailText.getText().toString().toLowerCase() + "@gmail.com";
        String password = signInPasswordText.getText().toString();
        if (password.equals("") || email.equals("@gmail.com")){
            Toast.makeText(getApplicationContext(),"Username or Password must not be empty!",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGNIN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // check the current user, if not null, then navigate back to main page
                                    NavUtils.navigateUpFromSameTask(SignUpActivity.this);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                                // If password and email address don't match, alert user by AlertDialog
                                new AlertDialog.Builder(SignUpActivity.this)
                                        //.setIcon(android.R.drawable.notify)
                                        .setTitle("Username does not exist or Username and password do not match")
                                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                signInPasswordText.setText("");
                                            }
                                        }).show();
                            }
                        }
                    });
        }
    }

    /**
     *
     * @param savedInstanceState
     * onCreate method sets password EditText as invisible when the activity creates.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);

        signInPasswordText = findViewById(R.id.signin_password);
        signUpPasswordText = findViewById(R.id.signup_password);
        signUpEmailText = findViewById(R.id.signup_email);
        signInEmailText = findViewById(R.id.signin_email);
        signUpContactText = findViewById(R.id.signup_contact);

        mAuth = FirebaseAuth.getInstance();
        signUpVisible = findViewById(R.id.first_visible);
        signInVisible = findViewById(R.id.second_visible);

        //set both password invisible first
        signUpPasswordText.setInputType(129);
        signInPasswordText.setInputType(129);

        // give user option to either hide or show password
        signUpVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signUpPasswordText.getInputType() == 128){
                    signUpPasswordText.setInputType(129);
                    signUpVisible.setText("show");
                }else {
                    signUpPasswordText.setInputType(128);
                    signUpVisible.setText("hide");
                }
            }
        });
        signInVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signInPasswordText.getInputType() == 128){
                    signInPasswordText.setInputType(129);
                    signInVisible.setText("show");
                }else {
                    signInPasswordText.setInputType(128);
                    signInVisible.setText("hide");
                }
            }
        });

    }

}