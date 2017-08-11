package com.hunfrit.togif.utils;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

import static com.hunfrit.togif.Constants.Constants.PATH_TO_THE_FILE;

/**
 * Created by pugman on 11.08.17.
 * Contact the developer - sckalper@gmail.com
 * company - A2Lab
 */


public class CameraHelper implements SurfaceHolder.Callback{

	private final SurfaceHolder mSurfaceHolder;
	private       Camera        mCamera;
	private       MediaRecorder mMediaRecorder;

	public CameraHelper(SurfaceHolder mSurfaceHolder){
		this.mSurfaceHolder = mSurfaceHolder;
		this.mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder){
		this.mCamera = Camera.open();
		try{
			mCamera.setPreviewDisplay(mSurfaceHolder);
			mCamera.startPreview();
			prepareVideoRecorder();
		} catch(IOException e){
			Log.e("CAMERA", "Surface creation exception: " + e.getMessage());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2){
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder){
	}

	public void closeCamera(){
		if(mCamera != null) {
			mCamera.lock();
			mCamera.release();
			releaseMediaRecorder();
			mCamera = null;
		}
	}

	public void startRecording(){
		mMediaRecorder.start();
	}

	public void stopRecording(){
		if(mMediaRecorder != null) {
			mMediaRecorder.stop();
			releaseMediaRecorder();
		}
	}

	private void prepareVideoRecorder(){
		mCamera.unlock();

		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setCamera(mCamera);
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mMediaRecorder.setOutputFile(new File(PATH_TO_THE_FILE, "myvideo.avi").getAbsolutePath());
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		try{
			mMediaRecorder.prepare();
		} catch(Exception e){
			Log.e("CAMERA", "Media recorder prepare exception: " + e.getMessage());
			releaseMediaRecorder();
		}
	}

	private void releaseMediaRecorder(){
		if(mMediaRecorder != null) {
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}

}
