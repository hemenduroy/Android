package com.example.covicheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button heartButton = findViewById(R.id.buttonHeart);
        Button breatheButton = findViewById(R.id.buttonBreathe);
        //Open Heart measurement activity
        heartButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {
                openHeartActivity();
            }
        });
        //open breathing measurement activity
        breatheButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {
                openBreatheActivity();
            }
        });
    }
    public void openHeartActivity() {
        Intent heartActivityIntent = new Intent(this, HeartRateMeasurement.class);
        startActivity(heartActivityIntent);
    }

    public void openBreatheActivity() {
        Intent breathingActivityIntent = new Intent(this, BreathingMeasurement.class);
        startActivity(breathingActivityIntent);
    }



}