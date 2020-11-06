/*
 *  Classname: ProfilePage
 *  Version: V1
 *  Date: 2020.11.01
 *  Copyright: Yanlin Chen
 */
package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/*
 * ProfilePage class can let user to see their personal information
 */
public class ProfilePage extends AppCompatActivity {

    private TextView UserNameInProfile;
    private TextView EmailInProfile;
    private Button EditProfileButton;
    private DocumentReference userRef;
    
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        UserNameInProfile = findViewById(R.id.userName);
        EmailInProfile = findViewById(R.id.userEmail);
        EditProfileButton = findViewById(R.id.EditProfile);
        
        userRef = FirebaseFirestore.getInstance().collection("Users").document(getCurrentUsername());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserNameInProfile.setText("User Name: "+documentSnapshot.getString("username"));
                    EmailInProfile.setText("Contact Info: " + documentSnapshot.getString("email"));
                }
            }
        });

        EditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEdit = new Intent(ProfilePage.this,EditProfile.class);
                startActivityForResult(goToEdit,1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == 34){
                String edit_email = data.getStringExtra("result_email");
                EmailInProfile.setText("Contact Info: " + edit_email);
                Toast.makeText(ProfilePage.this, "Change Saved! ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
