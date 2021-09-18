package com.example.covicheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covicheck.breathingmeasurement.BreathingMeasurement;
import com.example.covicheck.heartratemeasurement.HeartRateMeasurement;
import com.example.covicheck.symptomrating.SymptomRating;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button heartButton = findViewById(R.id.buttonHeart);
        Button breatheButton = findViewById(R.id.buttonBreathe);
        Button symptomsButton = findViewById(R.id.buttonSymptoms);
        //Open Heart measurement activity
        heartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openHeartActivity();
            }
        });
        //open breathing measurement activity
        breatheButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openBreatheActivity();
            }
        });
        //open symptom rating activity
        symptomsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openSymptomActivity();
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

    public void openSymptomActivity() {
        Intent symptomActivityIntent = new Intent(this, SymptomRating.class);
        startActivity(symptomActivityIntent);
    }
}