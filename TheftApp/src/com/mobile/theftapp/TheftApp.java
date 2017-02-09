package com.mobile.theftapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.mobile.theftapp.services.EventReceiver;

public class TheftApp extends Application {
	private final String IS_IMAGE_CAPTURE = "IS_IMAGE_CAPTURE";
	private final String SECRET_CAMERA_PREFERENCES = "SECRET_CAMERA_PREFERENCES";

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(new EventReceiver(), intentFilter);
	}

	public boolean isImageCapture() {
		SharedPreferences prefs = getSharedPreferences(
				SECRET_CAMERA_PREFERENCES, Context.MODE_PRIVATE);
		return prefs.getBoolean(IS_IMAGE_CAPTURE, false);
	}

	public void setImageCapture(boolean isImageCapture) {
		SharedPreferences prefs = getSharedPreferences(
				SECRET_CAMERA_PREFERENCES, Context.MODE_PRIVATE);
		prefs.edit().putBoolean(IS_IMAGE_CAPTURE, isImageCapture).commit();
	}

//	public void registerDaySmsService() {
//		Intent daySmsService = new Intent(this, SendDayDataService.class);
//		int timeInIntger = 10;
////		try {
////			if (time != null && time.length() > 0) {
////				timeInIntger = Integer.parseInt(time);
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		Calendar calendar = Calendar.getInstance();
//
//		calendar.set(Calendar.SECOND, timeInIntger);
//
//		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
//				daySmsService, 0);
//
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
//				pendingIntent);
//	}
}