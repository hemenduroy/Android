package com.example.covicheck.heartratemeasurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.example.covicheck.heartratemeasurement.parsingthreads.ParseByteArray;
import com.example.covicheck.heartratemeasurement.parsingthreads.ParseVideo;
import com.example.covicheck.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;

public class HeartRateMeasurement extends AppCompatActivity {
    //Video name properties
    public static final int MEDIA_TYPE_VIDEO = 3;
    public static final String videoFolderName = "CoviCheck";
    public static ParseVideo parseVideoThread = new ParseVideo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_measurement);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button parseVideoButton = findViewById(R.id.parseVideoButton);


        /*GraphView graph = findViewById(R.id.graph);
        //set graph limits
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(3000);
        graph.getViewport().setMinY(-25);
        graph.getViewport().setMaxY(25);
        //set title and axis labels
        graph.setTitle("Average R value per frame");
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Frame");
        gridLabel.setVerticalAxisTitle("R_average");
        //enable manual limit settings
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);*/

        //Camera Preview Window
        final boolean[] isVideoRecorded = {false};
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int CAMERA_PERMISSION_CODE = 102;
                final int WRITE_STORAGE_PERMISSION_CODE = 103;
                // Function to check and request camera access permission
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                // Function to check and request write storage permission
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE_PERMISSION_CODE);
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                //Limit capture to 46 seconds
                cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,46);
                //To save with Custom file name
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                isVideoRecorded[0] = true;
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent,1);
            }
        });

        parseVideoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final int READ_STORAGE_PERMISSION_CODE = 101;
                // Function to check and request read storage permission
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION_CODE);
                String sdcardDir = Environment.getExternalStorageDirectory().getPath();
                String filePath = null;
                if (isVideoRecorded[0])
                    filePath = fileUri.getPath();
                else
                    filePath = sdcardDir + "/FingertipVideo.mp4";
                //String filePath = sdcardDir + MediaStore.Video.Media.D;
                //System.out.println("File path : " + filePath);
                File tempFile = new File(filePath);

                //ParseVideo parseVideoThread = new ParseVideo(graph);
                Thread t1 = new Thread(parseVideoThread);
                if (tempFile.exists()) {
                    //System.out.println("Test");
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File does not exist!");
                }
            }

        });

    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(HeartRateMeasurement.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(HeartRateMeasurement.this, new String[]{permission}, requestCode);

        } /*else {
            Toast.makeText(HeartRateMeasurement.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }*/
    }


    //==================================Saving Video with a custom file name=============================================
    /* Creating file uri to store image/video */

    public Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));

    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                videoFolderName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            System.out.println("does not exist");
            if (!mediaStorageDir.mkdirs()) {
                Log.d(videoFolderName, "Oops! Failed create " + videoFolderName + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        /*if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else */
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            System.out.println("vid path " + mediaFile.getPath());
        } else
            return null;

        return mediaFile;
    }
    public static int getBPMvalue() {
        return parseVideoThread.getBPM();
    }
}