package com.mobile.theftapp.services;

import java.util.ArrayList;

import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.database.Message;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SendDayDataService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		takeData();
		clearData();

		return START_STICKY;
	}

	private void takeData() {
		ArrayList<Message> messages = new DatabaseHandler(this).Get_Messages();
	}

	private void clearData() {
		new DatabaseHandler(this).Delete_All_Messages();
	}
}
