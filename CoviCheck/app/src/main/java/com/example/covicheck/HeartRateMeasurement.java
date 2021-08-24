package com.example.covicheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class HeartRateMeasurement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_measurement);

        Button cameraButton = findViewById(R.id.cameraButton);
        Button parseVideoButton = findViewById(R.id.parseVideoButton);
        cameraButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {
                openCamera();
            }
        });
        parseVideoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {
                try {
                    final int STORAGE_PERMISSION_CODE = 101;
                    // Function to check and request permission
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

                    String filePath = "/sdcard/Movies/IMG_6750.MOV";
                    File tempFile = new File(filePath);
                    if (tempFile.exists()) {
                        parseVideoFromFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //Starting Camera Capture
    public void openCamera() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(cameraIntent);
    }

    public void parseVideoFromFile() throws IOException {


        ArrayList<Bitmap> bitmapImage = retrieveVideoMetadata();
        long redBucket = 0;
        //long greenBucket = 0;
        //long blueBucket = 0;
        long pixelCount = 0;

        int k = 0;
        while (k < bitmapImage.size()) {
            for (int y = 0; y < bitmapImage.get(k).getHeight(); y++)
            {
                for (int x = 0; x < bitmapImage.get(k).getWidth(); x++)
                {
                    int c = bitmapImage.get(k).getPixel(x,y);

                    pixelCount++;
                    redBucket += Color.red(c);
                    //greenBucket += Color.green(c);
                    //blueBucket += Color.blue(c);
                }
            }
            redBucket/=pixelCount;
            k++;
            System.out.println(redBucket);
            redBucket=0;
        }

        //Printing RGB values of screenshots

        //TextView rgbResult = findViewById(R.id.textView3);
        //rgbResult.setText("R : " + redBucket + " G : " + greenBucket + " B : " + blueBucket);



        //Color averageColor = Color.rgb(redBucket / pixelCount,
        //        greenBucket / pixelCount,
         //       blueBucket / pixelCount);
    }

    public ArrayList<Bitmap> retrieveVideoMetadata() throws IOException {

        String filePath = "/sdcard/Movies/IMG_6750.MOV";

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(filePath);
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        //System.out.println(duration);
        int durationInt = Integer.parseInt(duration);
        System.out.println(durationInt);
        for (int frameTimeInMicroSeconds = 0; frameTimeInMicroSeconds<=durationInt*1000; frameTimeInMicroSeconds+=326660) {
            //long microSecond = frameTime*16666;
            bitmapList.add(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds)); //unit in microsecond

        }
        /*FileInputStream in = new FileInputStream(filePath);
        BufferedInputStream buf = new BufferedInputStream(in);
        byte[] bMapArray= new byte[buf.available()];
        buf.read(bMapArray);
        Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);*/

        //ImageView imageView = findViewById(R.id.imageView);

        //imageView.setImageDrawable(Drawable.createFromPath("/sdcard/Pictures/Screenshot_1629588769.png"));
        return bitmapList;
    }


    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(HeartRateMeasurement.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(HeartRateMeasurement.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(HeartRateMeasurement.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}