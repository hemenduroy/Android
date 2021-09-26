package com.example.covicheck.heartratemeasurement.parsingthreads;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.lang.Math;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ParseVideo implements Runnable{
    //GraphView graph;
    public int BPM;


    /*public ParseVideo (GraphView graph)
    {
        this.graph = graph;
    }*/
    public static int mode(ArrayList<Integer> a) {
        int maxValue = 0, maxCount = 0;

        for (int i = 0; i < a.size(); ++i) {
            int count = 0;
            for (int j = 0; j < a.size(); ++j) {
                if (a.get(j).equals(a.get(i))) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = a.get(i);
            }
        }

        return maxValue;
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
        //System.out.println("received bitmap");

        //long greenBucket = 0;
        //long blueBucket = 0;


        int k = 0;

        //ArrayList<Long> R_array = new ArrayList<>();
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        //System.out.println(bitmapImageArray.get(20).getPixel(60,60));;
        ArrayList<Float> redBucketMovingDifference = new ArrayList<>();
        //ArrayList<Float> redBucketArray = new ArrayList<>();
        ArrayList<Bitmap> bitmapImageArray = retrieveVideoMetadata.retrieveVideoMetadata();
        System.out.println("bitmap array size " + bitmapImageArray.size());
        while (k < bitmapImageArray.size()) {
        //while (k < 1868) {
            //float redBucket = 0;
            float redBucketDiff = 0;
            float blueBucketDiff = 0;
            float greenBucketDiff = 0;
            int pixelCount = 0;
            //System.out.println("Height : " + bitmapImageArray.get(k).getHeight() + " Width : " + bitmapImageArray.get(k).getWidth());
            for (int y = 0; y < bitmapImageArray.get(k).getHeight(); y++) {
                for (int x = 0; x < bitmapImageArray.get(k).getWidth(); x++) {
                    //Bitmap myImageBitmap = bitmapImageArray.get(k);
                    //myImageBitmap = myImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    //System.out.println("sIZE OF INT IS : " + Integer.BYTES);
                    int c = bitmapImageArray.get(k).getPixel(x, y);
                    //redBucket += Color.red(c);
                    int c_next;
                    if (k != bitmapImageArray.size() - 1) { //corner case for last element
                        c_next = bitmapImageArray.get(k + 1).getPixel(x, y);
                        redBucketDiff += Color.red(c_next) - Color.red(c);
                        blueBucketDiff += Color.blue(c_next) - Color.blue(c);
                        greenBucketDiff += Color.green(c_next) - Color.green(c);
                    }
                    //System.out.println(c);
                    //System.out.println(bitmapImageArray.get(k).getPixel(x,y));
                    pixelCount++;
                    //System.out.println("increment pixel count");
                    //greenBucket += Color.green(c);
                    //blueBucket += Color.blue(c);
                }
            }
            float colorDiff=(redBucketDiff+blueBucketDiff+greenBucketDiff)/pixelCount;
            //redBucketArray.add(k, redBucket/pixelCount);
            redBucketMovingDifference.add(k, colorDiff);
            k++;
        }

        System.out.println("got bucket array and moving diff array");
        int averageWindow = 15;
        ArrayList<Double> redBucketMovingDifferenceMovingAverage = new ArrayList<>();
        //Calculating moving average with group size of 5
        for (int i = 0; i < redBucketMovingDifference.size(); i++) {
            Double temp = 0.0;
            //System.out.println(index);
            for (int j=i; (j < i + averageWindow && j < redBucketMovingDifference.size()); j++) {
                temp += redBucketMovingDifference.get(j);
            }
            temp/=averageWindow;
            //System.out.println(temp);
            redBucketMovingDifferenceMovingAverage.add(i, temp);
        }
        System.out.println("redBucketMovingDifferenceMovingAverage size : " + redBucketMovingDifferenceMovingAverage.size());
        //Calculating moving average differences
        int oldSize = redBucketMovingDifferenceMovingAverage.size()-1;
        for (int i = 0; i < redBucketMovingDifferenceMovingAverage.size() - 1; i++)
            redBucketMovingDifferenceMovingAverage.set(i, redBucketMovingDifferenceMovingAverage.get(i) - redBucketMovingDifferenceMovingAverage.get(i + 1));
        redBucketMovingDifferenceMovingAverage.remove(oldSize);
        System.out.println("got moving average differences");
        //System.out.println(redBucket);
        //float R_average = (float) redBucket / pixelCount;
        //System.out.println(R_average);
        //Filtering junk values
        /*if (R_average>220)
            R_average=220;
        else if (R_average<190)
            R_average=190;*/
        //R_array.add(redBucket);
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        System.out.println("plotting mov avg diff");
        //System.out.println("test1");
        //System.out.println(k);
        //System.out.println("test2");
        //System.out.println("R : " + redBucket);

        int fps=30,timeW=10,sampleSize=fps*timeW,index=0,count=0,x=0;
        ArrayList<Double> peaks;
        //int Hr[] = new int[]{};
        ArrayList<Integer> Hr = new ArrayList<>();
        System.out.println("stats : " + index + "\n" + redBucketMovingDifferenceMovingAverage.size() + "\n" + sampleSize);
        while (index< redBucketMovingDifferenceMovingAverage.size()-sampleSize) {
            ArrayList<Double> sampleData = new ArrayList<>();
            for (int z=index;z<index+sampleSize-1;z++)
                sampleData.add(redBucketMovingDifferenceMovingAverage.get(z));
            /*while (x < redBucketMovingDifferenceMovingAverage.size()) {
                series.appendData(new DataPoint(x, redBucketMovingDifferenceMovingAverage.get(x)), true, 3000);
                x++;
            }*/

            PeakDetect peakDetect = new PeakDetect(sampleData);
            //thread
            Thread t3 = new Thread(peakDetect);
            t3.start();
            try {
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("size of sampledata " + sampleData.size());
            peaks=peakDetect.RPeakDetection();
            //thread
            System.out.println("size of peaks : " + peaks.size());
            Hr.add(count,60*peaks.size()/timeW); //60 * peaks per second = peaks per minute = heart rate
            index+=sampleSize;
            count++;
        }
        //graph.addSeries(series);
        System.out.println("Plotted " + x + " points");
        for(int tempcount=0; tempcount<Hr.size();tempcount++) {
            System.out.println("hr : " + Hr.get(tempcount));
            BPM+= Hr.get(tempcount);
        }
        BPM/= Hr.size();
        //BPM=mode(Hr);
        System.out.println("BPM : " + BPM);
    }


        //Printing RGB values of screenshots

        //TextView rgbResult = findViewById(R.id.textView3);
        //rgbResult.setText("R : " + redBucket + " G : " + greenBucket + " B : " + blueBucket);


        //Color averageColor = Color.rgb(redBucket / pixelCount,
        //        greenBucket / pixelCount,
        //       blueBucket / pixelCount);
    public int getBPM() {
        return BPM;
    }
}
