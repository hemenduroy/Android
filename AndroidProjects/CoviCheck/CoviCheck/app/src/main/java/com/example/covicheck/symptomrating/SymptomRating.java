package com.example.covicheck.symptomrating;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicheck.R;
import com.example.covicheck.breathingmeasurement.BreathingMeasurement;
import com.example.covicheck.heartratemeasurement.HeartRateMeasurement;
import com.google.android.material.textfield.TextInputEditText;

public class SymptomRating extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_rating);
        RatingBar nauseaRating = findViewById(R.id.ratingNausea);
        RatingBar headacheRating = findViewById(R.id.ratingHeadache);
        RatingBar diarrheaRating = findViewById(R.id.ratingDiarrhea);
        RatingBar soreRating = findViewById(R.id.ratingSore);
        RatingBar feverRating = findViewById(R.id.ratingFever);
        RatingBar muscleRating = findViewById(R.id.ratingMuscle);
        RatingBar lossRating = findViewById(R.id.ratingLoss);
        RatingBar coughRating = findViewById(R.id.ratingCough);
        RatingBar shortnessRating = findViewById(R.id.ratingShortness);
        RatingBar tiredRating = findViewById(R.id.ratingTired);
        Button confirmButton = findViewById(R.id.button);
        TextInputEditText nameField = findViewById(R.id.textInputEditText2);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().equals(""))
                    nameField.setText("Unnamed");
                onConfirm(nameField.getText(),nauseaRating.getRating(),headacheRating.getRating(),diarrheaRating.getRating(),soreRating.getRating(),feverRating.getRating(),muscleRating.getRating(),lossRating.getRating(),coughRating.getRating(),shortnessRating.getRating(),tiredRating.getRating());
            }
        });

    }
    public void onConfirm(Editable name, float a, float b, float c, float d, float e, float f, float g, float h, float i, float j) {
        //WriteToDB writeToDB = new WriteToDB();
        System.out.println("patient name " + name + "\n"
                          +"nausea " + b + "\n"
                          +"headache " + b + "\n"
                          +"diarrhea " + c + "\n"
                          +"sore " + d + "\n"
                          +"fever " + e + "\n"
                          +"muscle " + f + "\n"
                          +"loss of smell or taste " + g + "\n"
                          +"cough " + h + "\n"
                          +"shortness of breath " + i + "\n"
                          +"feeling tired " + j + "\n"
                          +"respiratoryRate "+ BreathingMeasurement.getRPMvalue() + "\n"
                          +"heartRate "+ HeartRateMeasurement.getBPMvalue() );
        //String query = "INSERT INTO tblPat (patientID,name,nausea,headache,diarrhea,sore,fever,muscle,loss,cough,shortness,tired) VALUES ("+a+","+b+","+c+","+d+","+e+","+f+","+g+","+h+","+i+","+j+");";
        //System.out.println(query);
        //writeToDB.createDB();

        //=====
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext(),String.valueOf(name));
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("nausea", (int)a);
        values.put("headache", (int)b);
        values.put("diarrhea", (int)c);
        values.put("sore", (int)d);
        values.put("fever", (int)e);
        values.put("muscle", (int)f);
        values.put("loss", (int)g);
        values.put("cough", (int)h);
        values.put("shortness", (int)i);
        values.put("tired", (int)j);
        values.put("respiratoryRate", BreathingMeasurement.getRPMvalue());
        values.put("heartRate", HeartRateMeasurement.getBPMvalue());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(String.valueOf(name), null, values);
        System.out.println("Data for Patient "+newRowId+" has been recorded.");
        //=====
        /*ContentValues values = new ContentValues();
        values.put("nausea", a);
        values.put("headache", b);
        values.put("diarrhea", c);
        values.put("sore", d);
        values.put("fever", e);
        values.put("muscle", f);
        values.put("loss", g);
        values.put("cough", h);
        values.put("shortness", i);
        values.put("tired", j);
        writeToDB.writeDataToDB(values);*/

    }
}