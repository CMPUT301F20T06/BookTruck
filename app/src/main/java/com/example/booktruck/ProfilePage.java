package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booktruck.models.User;
import com.example.booktruck.services.UserService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.booktruck.services.UserService.getCurrentUsername;

public class ProfilePage extends AppCompatActivity {

    private UserService userService;
    private TextView UserNameInProfile;
    private TextView EmailInProfile;
    private TextView additionInProfile;
    private Button EditProfileButton;
    private DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(getCurrentUsername());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        UserService userService = new UserService();

        UserNameInProfile = findViewById(R.id.userName);
        EmailInProfile = findViewById(R.id.userEmail);
        EditProfileButton = findViewById(R.id.EditProfile);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserNameInProfile.setText(documentSnapshot.getString("username"));
                    EmailInProfile.setText(documentSnapshot.getString("email"));
                }
            }
        });

        UserNameInProfile.setText(userService.getCurrentUsername());
        EmailInProfile.setText(userService.getCurrentUserEmail());

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
                EmailInProfile.setText(edit_email);
                Toast.makeText(ProfilePage.this, "Change Saved! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
