package com.mobile.theftapp.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mobile.theftapp.CameraView;
import com.mobile.theftapp.TheftApp;
import com.mobile.theftapp.database.Account;
import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.database.Message;
import com.mobile.theftapp.gps.GPSTracker;
import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class StopSmsService extends Service {

	Account mAccount = null;
	String TAG = "StopSmsService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	private boolean isLoggedIn() {
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			mAccount = (Account) lObject;
			return true;
		}
		return false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		// getBettryLevel();
		String smsString = "Service Stopped\n"
				+ "Name: "
				+ getName()
				+ "\n"
				+ (getMessage() == null ? "Switch on gps to get location"
						: getMessage())
				+ "\nWifi Details: "
				+ (getWifiInfo(StopSmsService.this) == null ? "No wifi"
						: getWifiInfo(StopSmsService.this));
		;
		if (smsString != null) {
			// sendLongSMS();
			sendSMS("" + smsString);
			Log.d("", "Final msg==>>>>>" + smsString);
		}
		this.stopSelf();
		return START_STICKY;
	}

	private String getName() {
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			mAccount = (Account) lObject;
		}
		if (mAccount != null) {
			return mAccount.getName();
		}
		return "No Name";
	}

	private String getMessage() {
		GPSTracker gps = new GPSTracker(this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// \n is for new line
			// Toast.makeText(
			// getApplicationContext(),
			// "Your Location is - \nLat: " + latitude + "\nLong: "
			// + longitude, Toast.LENGTH_LONG).show();
			return "http://maps.google.com/maps?q=" + latitude + ","
					+ longitude;
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			// gps.showSettingsAlert();
		}
		return null;
	}

	public static String getWifiInfo(Context context) {
		String wifiInfo = "";
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null) {
				/*
				 * if (!TextUtils.isEmpty(connectionInfo.getSSID())) { wifiInfo
				 * = "\nSSID: "; wifiInfo += connectionInfo.getSSID(); }
				 */
				if (!TextUtils.isEmpty(connectionInfo.getMacAddress())) {
					wifiInfo += "\nMac Addr: ";
					wifiInfo += connectionInfo.getMacAddress();
				}
				/*
				 * wifiInfo += "\nIP Addr: "; wifiInfo +=
				 * connectionInfo.getIpAddress();
				 */
			}
		}
		return wifiInfo;
	}

	private void sendSMS(final String messageString) {
		TheftApp app = (TheftApp) getApplication();

		final String mobileNumber = mAccount.getPhoneNumber();
		if (!(mobileNumber != null && mobileNumber.length() > 0)) {
			Toast.makeText(getBaseContext(), "Please updateMobile number",
					Toast.LENGTH_SHORT).show();
		}
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "==>>>>>Msg Sent");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "==>>>>>Generic failure");
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "==>>>>>No service");
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "==>>>>>Null PDU");
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "==>>>>>Radio off");
					break;
				}
			}

		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(mobileNumber, null, messageString, sentPI,
				deliveredPI);
		saveMessage(mobileNumber, messageString);
		// savePicture();
	}

	private void saveMessage(String number, String messageString) {
		Message message = new Message();
		message.setDate(getCurrentTimeStamp());
		message.setMsg(messageString);
		message.setNumber(number);
		new DatabaseHandler(this).Add_Message(message);
	}

	/**
	 * 
	 * @return yyyy-MM-dd HH:mm:ss formate date as string
	 */
	public static String getCurrentTimeStamp() {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String currentTimeStamp = dateFormat.format(new Date()); // Find
																		// todays
																		// date

			return currentTimeStamp;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public void sendLongSMS() {

		String phoneNumber = "9666081108";
		String message = "Hello World! Now we are going to demonstrate "
				+ "how to send a message with more than 160 characters from your Android application.";

		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> parts = smsManager.divideMessage(message);
		smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
				null);
	}

	private void savePicture() {

		TheftApp app = (TheftApp) getApplicationContext();
		if (app.isImageCapture()) {
			Intent i = new Intent(this, CameraView.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} else {
			return;
		}

	}

}
