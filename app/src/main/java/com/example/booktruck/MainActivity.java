package com.example.booktruck;

import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void navigate (Class desitination_class) {
        Intent gotoDesitination = new Intent(this,
                desitination_class);
        startActivity(gotoDesitination);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnBorrow(View view) {
        navigate(BorrowManu.class);
    }
}