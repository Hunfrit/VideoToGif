package com.hunfrit.togif.main.BaseActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hunfrit.togif.R;
import com.hunfrit.togif.main.View.MainView;
import com.hunfrit.togif.main.presentation.VideoToGifPresenter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

import static com.hunfrit.togif.Constants.Constants.DIALOG;
import static com.hunfrit.togif.Constants.Constants.PATH_TO_THE_FILE;

public class MainActivity extends AppCompatActivity implements MainView {

    private Dialog dialog;

    private TextView tv;
    private ProgressBar progressBar;

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;

    private File mVideoFIle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoFIle = new File(PATH_TO_THE_FILE, "myvideo.avi");

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        tv = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    public void onClickToGif(View view) {
        VideoToGifPresenter videoToGif = new VideoToGifPresenter(MainActivity.this);
        videoToGif.execute(getBaseContext());

    }


    public void onClickStartRecord(View view) {
        if (prepareVideoRecorder()) {
            mMediaRecorder.start();
        } else {
            releaseMediaRecorder();
        }
    }

    public void onClickStopRecord(View view) {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
        }

    }


    private boolean prepareVideoRecorder() {

        mCamera.unlock();

        mMediaRecorder = new MediaRecorder();

        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(mVideoFIle.getAbsolutePath());
        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG){
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Video not found");
            adb.setMessage("Please, create the video");
            adb.setPositiveButton("OK", null);
            dialog = adb.create();
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
            }
        });

        return dialog;
    }

    @Override
    public void noSuchFile(boolean checkFile) {
        if(!checkFile){
            showDialog(DIALOG);
        }
    }

    @Override
    public void showProgress(String showProgress) {
        progressBar.setVisibility(View.VISIBLE);
        tv.setText(showProgress);
        if(showProgress == "END"){
            progressBar.setVisibility(View.GONE);
        }
    }
}
