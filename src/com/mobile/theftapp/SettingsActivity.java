package com.mobile.theftapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TimePicker;

import com.mobile.theftapp.database.Account;
import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class SettingsActivity extends Activity implements OnClickListener {

	private EditText timeEditText, mobileNumberEditText, emailIdEditText,
			nameEditText;
	private Account mAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);
		if (getActionBar() != null) {
			getActionBar().setTitle("Settings");
		}
		timeEditText = (EditText) findViewById(R.id.settings_time);
		mobileNumberEditText = (EditText) findViewById(R.id.settings_mobile);
		emailIdEditText = (EditText) findViewById(R.id.settings_email);
		nameEditText = (EditText) findViewById(R.id.settings_name);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		fillData();
	}

	private void fillData() {
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			mAccount = (Account) lObject;
		}
		if (mAccount != null) {
			timeEditText.setText("" + mAccount.getTimeDelay());
			mobileNumberEditText.setText("" + mAccount.getPhoneNumber());
			emailIdEditText.setText("" + mAccount.getEmail());
			nameEditText.setText("" + mAccount.getName());
		}
	}

	private void showTimePicker() {
		Calendar cal = Calendar.getInstance();
		TimePickerDialog timePickerDialog = new TimePickerDialog(this,
				timePickerListener, cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE), false);
		timePickerDialog.show();
	}

	private OnTimeSetListener timePickerListener = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			saveSettings();
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	private void saveSettings() {
		String timString = timeEditText.getText().toString();
		String mobile = mobileNumberEditText.getText().toString();
		String emailString = emailIdEditText.getText().toString();
		String nameString = nameEditText.getText().toString();
		if (validateFields()) {
			mAccount.setEmail(emailString);
			mAccount.setPhoneNumber(mobile);
			mAccount.setTimeDelay(timString);
			mAccount.setName(nameString);
			int updated = new DatabaseHandler(this).Update_Contact(mAccount);

		}
	}

	private boolean validateFields() {
		if (TextUtils.isEmpty(timeEditText.getText().toString().trim())) {
			timeEditText.setError("Please enter Valid Time");
			return false;
		}
		if (TextUtils.isEmpty(mobileNumberEditText.getText().toString().trim())) {
			mobileNumberEditText.setError("Please enter Phone Number");
			return false;
		}
		if (TextUtils.isEmpty(emailIdEditText.getText().toString().trim())) {
			emailIdEditText.setError("Please enter Email ID");
			return false;
		}
		if (TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
			nameEditText.setError("Please enter Name");
			return false;
		}
		return true;
	}
}
