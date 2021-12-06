package com.example.covicheck.heartratemeasurement.parsingthreads;

import java.util.ArrayList;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ParseByteArray implements Runnable {
    GraphView graph;


    public ParseByteArray(GraphView graph) {
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
        ArrayList<double[]> byteImageArray = retrieveVideoMetadata.retrieveVideoMetadata2();
        System.out.println("retrieved int[] list");
        //System.out.println("received bitmap");

        //long greenBucket = 0;
        //long blueBucket = 0;


        //ArrayList<Long> R_array = new ArrayList<>();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        //System.out.println(byteImageArray.size());
        ArrayList<double[]> byteImageArrayFiltered = new ArrayList<>();
        int k = 0;
        //Passing signal through a bandpass filter to remove noise

        while (k < byteImageArray.size()) {
            //************Calling Butterworth
            ButterworthBandPassFilter butterworthBandPassFilter = new ButterworthBandPassFilter(byteImageArray.get(k));
            Thread t3 = new Thread(butterworthBandPassFilter);
            t3.start();
            try {
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byteImageArrayFiltered.add(butterworthBandPassFilter.returnSignalResult());
            //byteImageArrayFiltered.add(byteImageArray.get(k));
            k++;
            //************Ending Butterworth
        }

        k = 0;
        ArrayList<Integer> ecgGraph = new ArrayList<>();
        while (k < byteImageArrayFiltered.size()) {
            int redBucket = 0;
            int pixelCount = 32 * 18;
            //System.out.println("Height : " + bitmapImageArray.get(k).getHeight() + " Width : " + bitmapImageArray.get(k).getWidth());
            for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                redBucket += ((int) byteImageArrayFiltered.get(k)[pixelIndex] >> 16) & 0xFF;
            }
            ecgGraph.add(redBucket / pixelCount);
            //float R_average = (float) redBucket / pixelCount;
            //System.out.println("float" + R_average);
            series.appendData(new DataPoint(k, ecgGraph.get(k)), true, 3000); //comment this line and undoc to use filtering + peak calc
            k++;
        }
        /*
        //Setting all >125 to 130 and all <125 to 120
        for (int i = 0; i < ecgGraph.size(); i++) {
            int temp = ecgGraph.get(i);
            if (temp > 125)
                ecgGraph.set(i, 130);
            else
                ecgGraph.set(i, 120);
        }
        //eliminating continuous peaks and dips
        for (int i = 1; i < ecgGraph.size(); i++) {
            int temp = ecgGraph.get(i);
            int temp_old = ecgGraph.get(i - 1);
            if (temp == temp_old) {
                ecgGraph.remove(i-1);
                i--;
            }
        }
        //appending new data
        for (int i = 0; i < ecgGraph.size(); i++) {
            int temp = ecgGraph.get(i);
            if (temp !=0)
                series.appendData(new DataPoint(i, temp), true, 1500);
        }
        */


        graph.addSeries(series);
        System.out.println("Plotted " + k + " points");

    }
            //Printing RGB values of screenshots

            //TextView rgbResult = findViewById(R.id.textView3);
            //rgbResult.setText("R : " + redBucket + " G : " + greenBucket + " B : " + blueBucket);


            //Color averageColor = Color.rgb(redBucket / pixelCount,
            //        greenBucket / pixelCount,
            //       blueBucket / pixelCount);
}


