package com.example.covicheck.breathingmeasurement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class sensorHandlerClass extends Service implements SensorEventListener {

    private SensorManager accelManage;
    private Sensor senseAccel;
    //float accelValuesX[] = new float[128];
    //float accelValuesY[] = new float[128];
    //float accelValuesZ[] = new float[];
    ArrayList<Float> accelValuesZ = new ArrayList<>();
    public static final String csvFolderName = "CoviCheck";
    int index = 0;
    public static File csvFile;
    //int k = 0;
    //Bundle b;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
        Sensor mySensor = sensorEvent.sensor;
        //System.out.println(mySensor.getPower());

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && accelValuesZ.size() < 451) {
            //accelValuesX[index] = sensorEvent.values[0];
            //accelValuesY[index] = sensorEvent.values[1];
            System.out.println("size csv : " + accelValuesZ.size());
            accelValuesZ.add(index, sensorEvent.values[2]);
            accelManage.unregisterListener(this);
            //writeZtocsv();
            /*try {
                //System.out.println("try block csv path : " + csvFile.getPath());
                //System.out.println("try block csv path2 : " + Environment.getExternalStorageDirectory());
                FileWriter writer = new FileWriter(csvFile.getPath());
                writer.append(accelValuesZ.get(index).toString());
                writer.append(',');
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            try (//PrintWriter writer = new PrintWriter(new File(csvFile.getPath()))
                 FileWriter fw = new FileWriter(csvFile.getPath(), true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                StringBuilder sb = new StringBuilder();
                sb.append(accelValuesZ.get(index).toString());
                //sb.append(',');
                //sb.append('\n');
                out.println(sb.toString());
                //System.out.println("done!");

            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
            accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            System.out.println("sensor unregistered");
            accelManage.unregisterListener(this);
        }
    }

    public File getCSVfile() {
        //System.out.println("Z : "+accelValuesZ.get(index));
        //Toast.makeText(this, "Z : "+accelValuesZ.get(index), Toast.LENGTH_SHORT).show();

        // External sdcard location
        //System.out.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        File mediaStorageDir = new File (Environment.getExternalStorageDirectory(), csvFolderName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            System.out.println("does not exist");
            if (!mediaStorageDir.mkdirs()) {
                Log.d(csvFolderName, "Oops! Failed create " + csvFolderName + " directory");
                //return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        /*if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else */
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "CSV_" + timeStamp + ".csv");
        //System.out.println("csv path " + mediaFile.getPath());
        return mediaFile;

    }
    public Uri getOutputMediaFileUri() {
        //return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", getCSVfile());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate() {
        //csvFile = new File(getOutputMediaFileUri().getPath());
        csvFile = getCSVfile();
        //System.out.println("path : " + getOutputMediaFileUri().getPath());
        //System.out.println("accel created");
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public String getCsvFolderPath() {
        return csvFile.getPath();
    }
}
