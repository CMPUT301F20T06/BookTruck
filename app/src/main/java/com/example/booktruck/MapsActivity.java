package com.example.booktruck;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = FirebaseFirestore.getInstance();
        final double[] latitude = new double[1];
        final double[] longitude = new double[1];

        // Add a marker in Sydney and move the camera
        LatLng Edmonton = new LatLng(53.544388, -113.490929);

        String parentClass = String.valueOf(getIntent().getStringExtra("ParentClass"));

        if (parentClass.equalsIgnoreCase("HandOver")){
            String ISBN = String.valueOf(getIntent().getStringExtra("ISBN"));
            DocumentReference docRef = db.collection("Books").document(ISBN);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String title = document.getString("title");
                            Double latitude = document.getGeoPoint("location").getLatitude();
                            Double longitude = document.getGeoPoint("location").getLongitude();
                            LatLng Location = new LatLng(latitude, longitude);;
                            mMap.addMarker(new MarkerOptions().position(Location).title(title));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, 15));


                        } else {
                            Log.d("GET_BOOK_BY_ISBN", "No such document");
                            Toast.makeText(getApplicationContext(), "Book Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("GET_BOOK_BY_ISBN", "get failed with ", task.getException());
                    }
                }
            });

        } else if (parentClass.equalsIgnoreCase("ReceiveBook")){

            String ISBN = String.valueOf(getIntent().getStringExtra("ISBN"));
            DocumentReference docRef = db.collection("Books").document(ISBN);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String title = document.getString("title");
                            Double latitude = document.getGeoPoint("location").getLatitude();
                            Double longitude = document.getGeoPoint("location").getLongitude();
                            LatLng Location = new LatLng(latitude, longitude);;
                            mMap.addMarker(new MarkerOptions().position(Location).title(title));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, 15));
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
}