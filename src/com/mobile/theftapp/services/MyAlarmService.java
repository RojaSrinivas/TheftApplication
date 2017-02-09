package com.mobile.theftapp.services;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mobile.theftapp.database.Account;
import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class MyAlarmService extends Service

{
	private NotificationManager mManager;
	private Account mAccount;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("static-access")
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Intent smsService = new Intent(this, SMSService.class);
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			mAccount = (Account) lObject;
		}
		String time = mAccount.getTimeDelay();
		int timeInIntger = 30;
		try {
			if (time != null && time.length() > 0) {
				timeInIntger = Integer.parseInt(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MINUTE, timeInIntger);

		PendingIntent pendingIntent = PendingIntent.getService(this, 0,
				smsService, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
				pendingIntent);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}