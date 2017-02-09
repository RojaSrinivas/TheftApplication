package com.mobile.theftapp.services;

import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BetteryReceiver extends BroadcastReceiver {

	private Context context;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		context = arg0;
		int level = arg1.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		if (arg1.getAction() == Intent.ACTION_BATTERY_CHANGED
				&& arg1.getAction() == Intent.ACTION_BATTERY_LOW) {
			if (level == 15 || level == 10 || level == 5) {
				Object lObject = TheftAppCache.getData(
						Constants.CACHE_SIGN_UP_INFO, context);
				if (lObject != null) {
					Intent service1 = new Intent(context, SMSService.class);
					context.startService(service1);
				}
			}

		}
	}
}