package com.example.covicheck;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ParseVideoThread implements Runnable{
    GraphView graph;


    public ParseVideoThread (GraphView graph)
    {
        this.graph = graph;
    }

    @Override
    public void run() {
        RetrieveVideoMetadata retrieveVideoMetadata = new RetrieveVideoMetadata();
        Thread t2 = new Thread(retrieveVideoMetadata);
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Bitmap> bitmapImageArray = retrieveVideoMetadata.retrieveVideoMetadata();
        //System.out.println("received bitmap");

        //long greenBucket = 0;
        //long blueBucket = 0;


        int k = 0;

        //ArrayList<Long> R_array = new ArrayList<>();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        while(k<bitmapImageArray.size()) {
            long redBucket = 0;
            long pixelCount = 0;
            //System.out.println("Height : " + bitmapImageArray.get(k).getHeight() + " Width : " + bitmapImageArray.get(k).getWidth());
            for (int y = bitmapImageArray.get(k).getHeight()/4; y < 3*bitmapImageArray.get(k).getHeight()/4; y++) {
                for (int x = bitmapImageArray.get(k).getWidth()/4; x < 3*bitmapImageArray.get(k).getWidth()/4; x++) {
                    //System.out.println("sIZE OF INT IS : " + Integer.BYTES);
                    int c = bitmapImageArray.get(k).getPixel(x, y);
                    //System.out.println(c);
                    //System.out.println(bitmapImageArray.get(k).getPixel(x,y));
                    pixelCount++;
                    //System.out.println("increment pixel count");
                    redBucket += Color.red(c);
                    //System.out.println();
                    //greenBucket += Color.green(c);
                    //blueBucket += Color.blue(c);
                }
            }
            float R_average = (float) redBucket / pixelCount;
            k++;
            //Filtering junk values
            if (R_average>220||R_average<190)
                continue;
            //R_array.add(redBucket);
            //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            series.appendData(new DataPoint(k,R_average),true,200);
            //System.out.println("test1");
            //System.out.println(k);
            //System.out.println("test2");
            //System.out.println("R : " + redBucket);
            graph.addSeries(series);
        }
        System.out.println("Plotted " + k + " points");


        //Printing RGB values of screenshots

        //TextView rgbResult = findViewById(R.id.textView3);
        //rgbResult.setText("R : " + redBucket + " G : " + greenBucket + " B : " + blueBucket);


        //Color averageColor = Color.rgb(redBucket / pixelCount,
        //        greenBucket / pixelCount,
        //       blueBucket / pixelCount);
    }


}
