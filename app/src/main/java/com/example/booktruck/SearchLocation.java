package com.example.booktruck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SearchLocation extends AppCompatActivity {
    EditText editText;
    TextView textView1;
    Button confirmBtn;
    private FirebaseFirestore db;
    private String ISBN;
    private DocumentReference bookRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location);
        editText = findViewById(R.id.edit_text);
        textView1 = findViewById(R.id.text_view1);
        confirmBtn = findViewById(R.id.confirm_update);

        Intent gotoBook = getIntent();
        ISBN = gotoBook.getStringExtra("ISBN");

        db = FirebaseFirestore.getInstance();

        Places.initialize(getApplicationContext() , "AIzaSyAgSSA9ayAHcjU76Mf0CMz_uXj7pFK89as");

        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG, Place.Field.NAME);

                // Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(SearchLocation.this);

                // Start activity result
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            // when success
            // Initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            // set address on editText
            editText.setText(place.getAddress());
            // set locality name
            confirmBtn.setVisibility(View.VISIBLE);
            textView1.setText(String.format("Update pickup location to:\n %s", place.getName()));
            confirmBtn.setOnClickListener(v -> {
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;
                GeoPoint point = new GeoPoint(lat, lng);
                setStatusToHandOverAndUpdateLocation(point);
                Toast.makeText(getApplicationContext(), "Update succeed!", Toast.LENGTH_LONG).show();
                this.finish();
            });

        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * setStatusToHandOvered method can change the current book's status to "handovered"
     */
    public void setStatusToHandOverAndUpdateLocation(GeoPoint location){
        bookRef = db.collection("Books").document(ISBN);
        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GET_BOOK_BY_ISBN", "DocumentSnapshot data: " +
                                document.getData().get("title").toString());
                        Map<String, Object> data = document.getData();
                        // set status to handovered
                        data.put("status", "handovered");
                        // update the location
                        data.put("location", location);
                        bookRef.set(data);
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "No such document");
                        Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                }
            }
        });
    }
}


//setXYBtn.setOnClickListener(v -> {
//        String locationX = latitudeText.getText().toString();
//        Double valueX = Double.valueOf(locationX);
//        String locationY = longitudeText.getText().toString();
//        Double valueY = Double.valueOf(locationY);
//        Log.d("X,Y", valueX.toString() + valueY.toString());
//        if (valueY < -180 || valueY > 180 || valueX < -90 || valueX > 90) {
//        Toast.makeText(getApplicationContext(), "Please enter a valid location!", Toast.LENGTH_SHORT).show();
//        } else {
//        GeoPoint location = new GeoPoint(valueX, valueY);
//        setStatusToHandOverAndUpdateLocation(location);
//        Intent intent = new Intent(ShowBookDetail.this, BorrowMenu.class);
//        startActivity(intent);
//        }
//        });