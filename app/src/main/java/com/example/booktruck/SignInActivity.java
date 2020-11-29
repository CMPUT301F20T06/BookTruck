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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 *  SignUpActivity provides Sign In methods.
 */
public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signInEmailText;
    private EditText signInPasswordText;
    private Button signInVisible;
    private FirebaseFirestore db;


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
                                    NavUtils.navigateUpFromSameTask(SignInActivity.this);
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                                // If password and email address don't match, alert user by AlertDialog
                                new AlertDialog.Builder(SignInActivity.this)
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


    public void gotoSignup(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    /**
     *
     * @param savedInstanceState
     * onCreate method sets password EditText as invisible when the activity creates.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        signInPasswordText = findViewById(R.id.signin_password);
        signInEmailText = findViewById(R.id.signin_email);

        mAuth = FirebaseAuth.getInstance();
        signInVisible = findViewById(R.id.second_visible);

        //set both password invisible first
        signInPasswordText.setInputType(129);

        // give user option to either hide or show password
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