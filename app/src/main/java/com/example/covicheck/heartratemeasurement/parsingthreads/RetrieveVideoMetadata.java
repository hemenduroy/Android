package com.example.covicheck.heartratemeasurement.parsingthreads;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.util.ArrayList;

public class RetrieveVideoMetadata implements Runnable {

    private final ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private final ArrayList<double[]> bitmapToByteArray = new ArrayList<>();

    @Override
    public void run() {

        String sdcardDir = Environment.getExternalStorageDirectory().getPath();
        //System.out.println(sdcardDir);
        String filePath = sdcardDir + "/FingertipVideo.mp4";
        //System.out.println(filePath);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(filePath);
        long durationInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        int width = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        width /= 40;
        int height = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        height /= 40;
        //System.out.println(duration);
        System.out.println("Duration : " + durationInt);
        //45767000 micro secs/326660 = 140 frames = 3 FPS
        //30 FPS - crash with 100% res
        //15 FPS - crash with 100% worked with 10% bitmap resolution

        for (long frameTimeInMicroSeconds = 5000000; frameTimeInMicroSeconds<=durationInt*1000-5000000; frameTimeInMicroSeconds+=33333) {
            //long microSecond = frameTime*16666;
            //System.out.println("adding bitmap frame");
            //byte array bitmap
            //int[] bitmapFrame = new int[width*height];
            //Bitmap.createScaledBitmap(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds),width,height,false).getPixels(bitmapFrame, 0, width, 0, 0, width,height);
            //double[] bitmapFrameDouble = copyFromIntArray(bitmapFrame);
            //bitmapToByteArray.add(bitmapFrameDouble);
            //bytearray bitmap
            //System.out.println("added bitmap " + frameTimeInMicroSeconds/1000000);
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            //System.out.println("got frame");
            if (bitmap != null)
                bitmapList.add(Bitmap.createScaledBitmap(bitmap,width,height,false)); //unit in microsecond
            //bitmapList.add(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds)); //unit in microsecond
        }

    }

    public ArrayList<Bitmap> retrieveVideoMetadata() {
        //System.out.println("sending bitmap");
        return bitmapList;
    }

    public ArrayList<double[]> retrieveVideoMetadata2() {
        System.out.println("sending bitmap");
        return bitmapToByteArray;
    }

    public static double[] copyFromIntArray(int[] source) {
        double[] dest = new double[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }

}

