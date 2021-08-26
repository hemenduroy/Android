package com.example.covicheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;

import com.jjoe64.graphview.GraphView;

public class HeartRateMeasurement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_measurement);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button parseVideoButton = findViewById(R.id.parseVideoButton);
        GraphView graph = findViewById(R.id.graph);
        //set graph limits
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(200);
        graph.getViewport().setMinY(190);
        graph.getViewport().setMaxY(220);
        //enable manual limit settings
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                openCamera();
            }
        });
        parseVideoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view) {

                    final int STORAGE_PERMISSION_CODE = 101;
                    // Function to check and request permission
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

                    String sdcardDir = Environment.getExternalStorageDirectory().getPath();
                    String filePath = sdcardDir + "/Movies/IMG_6755.MOV";
                    //System.out.println("File path : " + filePath);
                    File tempFile = new File(filePath);

                    ParseVideoThread parseVideoThread = new ParseVideoThread(graph);
                    Thread t1 = new Thread(parseVideoThread);

                    if (tempFile.exists()) {
                        //System.out.println("Test");
                        t1.start();
                        /*try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            t1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        System.out.println("File does not exist!");
                    }
            }
        });
    }
    //Starting Camera Capture
    public void openCamera() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(cameraIntent);
    }

    /*public void parseVideoFromFile() throws IOException {
        new Thread() {


        }.start*();
    }*/

    /*public ArrayList<Bitmap> retrieveVideoMetadata() {

        String sdcardDir = Environment.getExternalStorageDirectory().getPath();
        //System.out.println(sdcardDir);
        String filePath = sdcardDir + "/Movies/IMG_6755.MOV";
        //System.out.println(filePath);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(filePath);
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        //System.out.println(duration);
        long durationInt = Integer.parseInt(duration);
        System.out.println("Duration : " + durationInt);
        //45767000 micro secs/326660 = 140 frames = 3 FPS
        //30 FPS - crash with 100% res
        //15 FPS - crash worked with 10% bitmap resolution
        for (long frameTimeInMicroSeconds = 0; frameTimeInMicroSeconds<=durationInt*1000; frameTimeInMicroSeconds+=32666) {
            //long microSecond = frameTime*16666;
            bitmapList.add(Bitmap.createScaledBitmap(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds),48,27,false)); //unit in microsecond
            //bitmapList.add(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds)); //unit in microsecond
        }
        FileInputStream in = new FileInputStream(filePath);
        BufferedInputStream buf = new BufferedInputStream(in);
        byte[] bMapArray= new byte[buf.available()];
        buf.read(bMapArray);
        Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);

        //ImageView imageView = findViewById(R.id.imageView);

        //imageView.setImageDrawable(Drawable.createFromPath("/sdcard/Pictures/Screenshot_1629588769.png"));
        return bitmapList;
    }*/


    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        //Environment.isExternalStorageLegacy();
        if (ContextCompat.checkSelfPermission(HeartRateMeasurement.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(HeartRateMeasurement.this, new String[] { permission }, requestCode);

        }
        else {
            Toast.makeText(HeartRateMeasurement.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

}