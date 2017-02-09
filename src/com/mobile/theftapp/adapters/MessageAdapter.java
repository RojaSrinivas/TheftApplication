package com.mobile.theftapp.adapters;

import java.util.ArrayList;

import com.mobile.theftapp.R;
import com.mobile.theftapp.database.Message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAdapter extends ArrayAdapter<Message> {
	ArrayList<Message> messages = null;
	Context context;

	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super(context, -1, messages);
		this.messages = messages;
		this.context = context;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Message getItem(int position) {
		return messages.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder;
		final Message message = messages.get(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.listitem_message, null);
			viewHolder = new ViewHolder();
			view.findViewById(R.id.showmap).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							showMap(message);
						}
					});
			viewHolder.numberTextView = (TextView) view
					.findViewById(R.id.listitem_number);
			viewHolder.dateTextView = (TextView) view
					.findViewById(R.id.listitem_date);
			viewHolder.mesageTextView = (TextView) view
					.findViewById(R.id.listitem_message);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.numberTextView.setText(message.getNumber());
		viewHolder.dateTextView.setText(message.getDate());
		viewHolder.mesageTextView.setText(message.getMsg());
		return view;
	}

	private void showMap(Message message) {
		try {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(message.getMsg()));
			intent.setPackage("com.google.android.apps.maps");
			context.startActivity(intent);
		} catch (Exception e) {
				Toast.makeText(context, "Location not available..!", Toast.LENGTH_SHORT).show();
		}
	}

	private class ViewHolder {
		TextView numberTextView, dateTextView, mesageTextView;
	}

}
