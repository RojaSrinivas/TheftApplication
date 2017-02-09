package com.mobile.theftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText mUserIdField;
	private EditText mPasswordField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);

		if (getActionBar() != null) {
			getActionBar().setTitle("Signin");
		}
		checkIsLoggedIn();
		loadUiComponents();
	}

	private void checkIsLoggedIn() {
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}		
	}

	private void loadUiComponents() {
		mUserIdField = (EditText) findViewById(R.id.login_user_id_field);
		mPasswordField = (EditText) findViewById(R.id.login_password_field);

		findViewById(R.id.signin_signin).setOnClickListener(this);
		findViewById(R.id.signin_signup).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signin_signin:
			validateLoginFields();
			break;
		case R.id.signin_signup:

			startActivity(new Intent(LoginActivity.this, SignupActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	private void validateLoginFields() {
		if (TextUtils.isEmpty(mUserIdField.getText().toString().trim())) {
			mUserIdField.setError("Please enter User ID");
			return;
		}
		if (TextUtils.isEmpty(mPasswordField.getText().toString().trim())) {
			mPasswordField.setError("Please enter Password");
			return;
		}

		authenticateLoginDetails(mUserIdField.getText().toString().trim(),
				mPasswordField.getText().toString().trim());
	}

	private void authenticateLoginDetails(String pUserId, String pPassword) {
		boolean isExists = DatabaseHandler.getOldInstance(this)
				.checkLoginDetails(pUserId, pPassword);
		if (isExists) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		} else {
			Toast.makeText(this, "Please enter valid credentials",
					Toast.LENGTH_SHORT).show();
		}
	}
}
