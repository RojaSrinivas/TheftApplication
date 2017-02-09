package com.mobile.theftapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mobile.theftapp.adapters.MessageAdapter;
import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.database.Message;

public class MessagesListActivity extends Activity {
	DatabaseHandler dbHandler;
	ArrayList<Message> messages;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_messages);
		dbHandler = DatabaseHandler.getOldInstance(this);
		listView = (ListView) findViewById(R.id.messageslist);
		updateList();
	}

	private void updateList() {
		messages = dbHandler.Get_Messages();
		listView.setAdapter(new MessageAdapter(this, messages));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_messageslist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_delete:
			clearMesages();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void clearMesages() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want clear all the messages");
		builder.setPositiveButton("Clear", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHandler.Delete_All_Messages();
				updateList();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
