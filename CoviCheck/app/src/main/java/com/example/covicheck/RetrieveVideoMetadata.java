package com.example.covicheck;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.util.ArrayList;

public class RetrieveVideoMetadata implements Runnable{

    private ArrayList<Bitmap> bitmapList = new ArrayList<>();

    @Override
    public void run() {

            String sdcardDir = Environment.getExternalStorageDirectory().getPath();
            //System.out.println(sdcardDir);
            String filePath = sdcardDir + "/Movies/IMG_6755.MOV";
            //System.out.println(filePath);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(filePath);

            String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            //System.out.println(duration);
            long durationInt = Integer.parseInt(duration);
            //System.out.println("Duration : " + durationInt);
            //45767000 micro secs/326660 = 140 frames = 3 FPS
            //30 FPS - crash with 100% res
            //15 FPS - crash with 100% worked with 10% bitmap resolution
            for (long frameTimeInMicroSeconds = 0; frameTimeInMicroSeconds<=durationInt*1000; frameTimeInMicroSeconds+=250000) {
                //long microSecond = frameTime*16666;
                //System.out.println("adding bitmap frame");
                bitmapList.add(Bitmap.createScaledBitmap(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds),1920,1080,false)); //unit in microsecond
                //bitmapList.add(mediaMetadataRetriever.getFrameAtTime(frameTimeInMicroSeconds)); //unit in microsecond
            }
        //System.out.println("Completed Bitmap retrieval");
        /*FileInputStream in = new FileInputStream(filePath);
        BufferedInputStream buf = new BufferedInputStream(in);
        byte[] bMapArray= new byte[buf.available()];
        buf.read(bMapArray);
        Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);*/

            //ImageView imageView = findViewById(R.id.imageView);

            //imageView.setImageDrawable(Drawable.createFromPath("/sdcard/Pictures/Screenshot_1629588769.png"));
        }
    public ArrayList<Bitmap> retrieveVideoMetadata() {
        //System.out.println("sending bitmap");
        return bitmapList;
    }
}
