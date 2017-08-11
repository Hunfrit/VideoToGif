package com.hunfrit.togif.main.presentation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.hunfrit.togif.Constants.Constants;
import com.hunfrit.togif.GifEncoderLIB.AnimatedGifEncoder;
import com.hunfrit.togif.main.View.MainView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import static com.hunfrit.togif.Constants.Constants.PathToTheFile;

/**
 * Created by Artem Shapovalov on 10.08.2017.
 */

public class VideoToGifPresenter extends AsyncTask<Context, Void, Boolean> {

    private MainView view;

    public VideoToGifPresenter(MainView view) {this.view = view; }

    @Override
    protected Boolean doInBackground(Context... contexts) {
        File videoFile = new File(PathToTheFile, "myvideo.avi");

        if (!checkOnExistingFile(videoFile)){
            return false;
        }

        Uri videoFileUri = Uri.parse(videoFile.toString());

        Log.d("myLogs", String.valueOf(videoFileUri));

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile.getAbsolutePath());
        ArrayList<Bitmap> rev = new ArrayList<Bitmap>();

        //Create a new Media Player
        MediaPlayer mp = MediaPlayer.create(contexts[0], videoFileUri);

        int millis = mp.getDuration();

        Log.d("TAGA", "millis - " + String.valueOf(millis));

        for(int i=0;i<millis*1000;i+=1000000)
        {
            Log.d("TAGA", "millis - " + millis*1000 + " i - " + i);
            Bitmap bitmap = retriever.getFrameAtTime(i);
            rev.add(bitmap);
            Log.d("TAGA", "addFrame - " + String.valueOf(rev));
        }

        FileOutputStream outStream = null;
        try{
            outStream = new FileOutputStream(PathToTheFile + "/test.gif");
            outStream.write(generateGIF(rev));
            outStream.close();
            deleteVideo();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean check) {
        super.onPostExecute(check);

        if (!check){
            view.NoSuchFile(check);
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public byte[] generateGIF(ArrayList<Bitmap> bitmaps){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        for (Bitmap bitmap : bitmaps){
            Log.d("TAGA", String.valueOf(bitmap));
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        return bos.toByteArray();
    }

    private void deleteVideo(){
        File deleteFile = new File(PathToTheFile, "myvideo.avi");
        boolean deleted = deleteFile.delete();
        Log.d("TAGA", String.valueOf(deleted));
    }

    private boolean checkOnExistingFile(File fileCheck){
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileCheck));
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Log.d("TAGA", String.valueOf(e));
            return false;
        }
        return true;
    }
}
