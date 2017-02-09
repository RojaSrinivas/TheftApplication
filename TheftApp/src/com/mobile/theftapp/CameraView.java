package com.mobile.theftapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class CameraView extends Activity implements SurfaceHolder.Callback,
		OnClickListener {
	private static final String TAG = "CameraTest";
	Camera mCamera;
	boolean mPreviewRunning = false;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.e(TAG, "onCreate");
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cameraview);
		ImageView img = (ImageView) findViewById(R.id.blankImage);

		// if (CaptureCameraImage.isBlack)
		// img.setBackgroundResource(android.R.color.black);
		// else
		img.setBackgroundResource(android.R.color.white);

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceView.setOnClickListener(this);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			if (data != null) {
				// Intent mIntent = new Intent();
				// mIntent.putExtra("image",imageData);

				mCamera.stopPreview();
				mPreviewRunning = false;
				mCamera.release();

				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length, opts);
					// bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300,
					// false);
					// int width = bitmap.getWidth();
					// int height = bitmap.getHeight();
					// int newWidth = 300;
					// int newHeight = 300;
					//
					// // calculate the scale - in this case = 0.4f
					// float scaleWidth = ((float) newWidth) / width;
					// float scaleHeight = ((float) newHeight) / height;
					//
					// // createa matrix for the manipulation
					// Matrix matrix = new Matrix();
					// // resize the bit map
					// matrix.postScale(scaleWidth, scaleHeight);
					// // rotate the Bitmap
					// matrix.postRotate(-90);
					// Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					// width, height, matrix, true);
					// CaptureCameraImage.image.setImageBitmap(bitmap);
					storeImage(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// StoreByteImage(mContext, imageData, 50,"ImageName");
				// setResult(FOTO_MODE, mIntent);
				finish();
			}
		}
	};

	private boolean storeImage(Bitmap imageData) {

		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		String format = s.format(new Date());

		// get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/TheftApp";
		File sdIconStorageDir = new File(iconsStoragePath);

		// create storage directories, if they don't exist
		if (!sdIconStorageDir.exists())
			sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() + "/" + format
					+ ".png";
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	protected void onResume() {
		Log.e(TAG, "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onStop() {
		Log.e(TAG, "onStop");
		super.onStop();
	}

	@TargetApi(9)
	public void surfaceCreated(SurfaceHolder holder) {
		int camCount = Camera.getNumberOfCameras();
		Log.e(TAG, "surfaceCreated with cameras===>>> " + camCount);
		if (camCount > 0){
			mCamera = Camera.open(1);
		}else{
			mCamera = Camera.open(0);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(TAG, "surfaceChanged");

		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		// p.setPreviewSize(300, 300);

		// if(CaptureCameraImage.cameraID == 0){
		// String stringFlashMode = p.getFlashMode();
		// if (stringFlashMode.equals("torch"))
		// p.setFlashMode("on"); // Light is set off, flash is set to normal
		// 'on' mode
		// else
		// p.setFlashMode("torch");
		// }
		p.setFlashMode("off");
		mCamera.setParameters(p);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true;
		mCamera.takePicture(null, mPictureCallback, mPictureCallback);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "surfaceDestroyed");
		// mCamera.stopPreview();
		// mPreviewRunning = false;
		// mCamera.release();
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	public void onClick(View v) {
		mCamera.takePicture(null, mPictureCallback, mPictureCallback);
	}

}