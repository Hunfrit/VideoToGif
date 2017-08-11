package com.hunfrit.togif.main.BaseActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hunfrit.togif.R;
import com.hunfrit.togif.main.View.MainView;
import com.hunfrit.togif.main.presentation.VideoToGifPresenter;
import com.hunfrit.togif.utils.CameraHelper;

import static com.hunfrit.togif.Constants.Constants.DIALOG;

public class MainActivity extends AppCompatActivity implements MainView{

	private Dialog       dialog;
	private TextView     mProgressTv;
	private ProgressBar  mProgressBar;
	private CameraHelper mCameraHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		mProgressTv = (TextView) findViewById(R.id.textView);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

		mCameraHelper = new CameraHelper(mSurfaceView.getHolder());
	}

	@Override
	protected void onPause(){
		mCameraHelper.closeCamera();
		super.onPause();
	}

	@Override
	protected Dialog onCreateDialog(int id){
		if(id == DIALOG) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Video not found");
			adb.setMessage("Please, create the video");
			adb.setPositiveButton("OK", null);
			dialog = adb.create();
		}
		dialog.setOnShowListener(new DialogInterface.OnShowListener(){
			@Override
			public void onShow(DialogInterface dialogInterface){
			}
		});
		return dialog;
	}

	@Override
	public void noSuchFile(boolean checkFile){
		if(!checkFile) {
			showDialog(DIALOG);
		}
	}

	@Override
	public void showProgress(String showProgress){
		if(showProgress.contentEquals("START")){
			mProgressBar.setVisibility(View.VISIBLE);
		}
		mProgressTv.setText(showProgress);
		if(showProgress.contentEquals("END") ) {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	public void onClickToGif(View view){
//		VideoToGifPresenter videoToGif = new VideoToGifPresenter(MainActivity.this);
//		videoToGif.execute(getBaseContext());
		VideoToGifPresenter toGif = new VideoToGifPresenter(this);
		toGif.convertToGif(getApplicationContext());
	}


	public void onClickStartRecord(View view){
		mCameraHelper.startRecording();
	}

	public void onClickStopRecord(View view){
		mCameraHelper.stopRecording();
	}

}
