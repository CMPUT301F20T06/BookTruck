package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booktruck.models.Book;
import com.example.booktruck.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditText emailaddress;
    private Button Confirm_email_Button;
    FirebaseFirestore db;
    static CollectionReference userRef;
    
    public String getCurrentUsername() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String username = "";
        String[] array = email.split("@");
        for (int i=0; i<array.length-1; i++) {
            username += array[i];
        }
        return username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users");

        emailaddress = findViewById(R.id.email_addresss);
        Confirm_email_Button = findViewById(R.id.confirm_number);
        final DocumentReference userDoc = userRef.document(getCurrentUsername());

        Confirm_email_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("username");
                            String password = documentSnapshot.getString("password");
                            String email = documentSnapshot.getString("email");

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("email", emailaddress.getText().toString());
                            data.put("username",userName);
                            data.put("password",password);

                            userRef.document(getCurrentUsername()).set(data);

                            Intent i = new Intent(EditProfile.this,ProfilePage.class);
                            i.putExtra("result_email",emailaddress.getText().toString());
                            setResult(34,i);
                            finish();

                        }
                    }
                });
            }
        });
    }
}
