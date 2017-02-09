package com.mobile.theftapp;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.theftapp.gps.GPSTracker;
import com.mobile.theftapp.services.SMSService;
import com.mobile.theftapp.services.StopSmsService;

public class MainActivity extends Activity {

	public static int cameraID = 0;
	public static boolean isBlack = true;
	public static ImageView imageViewSwitch;
	public static TextView textViewSwitch, gpsAlert;
	public static LinearLayout linearLayout;

	String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_switch);
		if (getActionBar() != null) {
			getActionBar().setTitle("Whip");
		}
		linearLayout = (LinearLayout) findViewById(R.id.layout);
		textViewSwitch = (TextView) findViewById(R.id.textcapture_switch);
		gpsAlert = (TextView) findViewById(R.id.gpsalert);
		imageViewSwitch = (ImageView) findViewById(R.id.imagecapture_switch);
		imageViewSwitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switchClicked();
			}

		});
		updateView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			break;
		case R.id.menu_messages:
			startActivity(new Intent(MainActivity.this,
					MessagesListActivity.class));
			break;
		case R.id.menu_gallery:

			File folder = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/TheftApp/");
			File[] allFiles = folder.listFiles();
			if (allFiles != null) {
				new SingleMediaScanner(MainActivity.this, allFiles[0]);
			} else {
				Toast.makeText(MainActivity.this, "No files available",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void switchClicked() {
		TheftApp app = (TheftApp) getApplication();
		boolean iscapture = app.isImageCapture();
		app.setImageCapture(!iscapture);
		updateView();
		if (iscapture) {
			Intent service1 = new Intent(MainActivity.this,
					StopSmsService.class);
			MainActivity.this.startService(service1);
		}
		if (iscapture && canToggleGPS()) {
			turnGPSOn();
		}
	}

	private void updateView() {
		GPSTracker gpsTracker = new GPSTracker(this);
		if (gpsTracker.canGetLocation()) {
			gpsAlert.setVisibility(View.GONE);
		} else {
			gpsAlert.setVisibility(View.VISIBLE);
		}

		TheftApp app = (TheftApp) getApplication();
		boolean iscapture = app.isImageCapture();
		Log.v(TAG, "=====>>>>> " + iscapture);
		if (iscapture) {

			linearLayout.setBackgroundColor(getResources().getColor(
					R.color.green));
			textViewSwitch.setText("Theft app is ON");
			imageViewSwitch.setImageResource(R.drawable.switch_on);
		} else {
			linearLayout.setBackgroundColor(getResources()
					.getColor(R.color.red));
			textViewSwitch.setText("Theft app is OFF");
			imageViewSwitch.setImageResource(R.drawable.switch_off);

		}

	}

	// public void onFrontClick(View v) {
	// RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
	// if (rdbBlack.isChecked()) {
	// isBlack = true;
	// } else {
	// isBlack = false;
	// }
	// cameraID = 1;
	// Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
	// startActivityForResult(i, 999);
	// }

	// public void onBackClick(View v) {
	// RadioButton rdbBlack = (RadioButton) findViewById(R.id.rdb_black);
	// if (rdbBlack.isChecked()) {
	// isBlack = true;
	// } else {
	// isBlack = false;
	// }
	// cameraID = 0;
	// Intent i = new Intent(CaptureCameraImage.this, CameraView.class);
	// startActivityForResult(i, 999);
	// }
	private void turnGPSOn() {
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

	private void turnGPSOff() {
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps")) { // if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

	private boolean canToggleGPS() {
		PackageManager pacman = getPackageManager();
		PackageInfo pacInfo = null;

		try {
			pacInfo = pacman.getPackageInfo("com.android.settings",
					PackageManager.GET_RECEIVERS);
		} catch (NameNotFoundException e) {
			return false; // package not found
		}

		if (pacInfo != null) {
			for (ActivityInfo actInfo : pacInfo.receivers) {
				// test if recevier is exported. if so, we can toggle GPS.
				if (actInfo.name
						.equals("com.android.settings.widget.SettingsAppWidgetProvider")
						&& actInfo.exported) {
					return true;
				}
			}
		}

		return false; // default
	}

	public class SingleMediaScanner implements MediaScannerConnectionClient {

		private MediaScannerConnection mMs;
		private File mFile;

		public SingleMediaScanner(Context context, File f) {
			mFile = f;
			mMs = new MediaScannerConnection(context, this);
			mMs.connect();
		}

		public void onMediaScannerConnected() {
			mMs.scanFile(mFile.getAbsolutePath(), null);
		}

		public void onScanCompleted(String path, Uri uri) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(uri);
			startActivity(intent);
			mMs.disconnect();
		}

	}
}
