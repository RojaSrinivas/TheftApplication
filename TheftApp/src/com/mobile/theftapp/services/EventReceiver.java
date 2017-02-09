package com.mobile.theftapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class EventReceiver extends BroadcastReceiver {
	private static final int TAKE_PHOTO_CODE = 021;
	private String TAG = "EventReceiver";
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			Log.d(TAG, "====>>>>" + Intent.ACTION_SCREEN_OFF);
			Object lObject = TheftAppCache.getData(
					Constants.CACHE_SIGN_UP_INFO, context);
			if (lObject != null) {
				Intent service1 = new Intent(context, MyAlarmService.class);
				context.startService(service1);
			}

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d(TAG, "====>>>>" + Intent.ACTION_SCREEN_ON);
		}
	}

}