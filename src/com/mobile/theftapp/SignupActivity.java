package com.mobile.theftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.mobile.theftapp.database.Account;
import com.mobile.theftapp.database.DatabaseHandler;
import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class SignupActivity extends Activity {

	private EditText mUserIdField, mPasswordField, mConfirmPasswordField,
			mEmailIdField, mPhoneNumberField, mNameField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_signup_new);

		if (getActionBar() != null) {
			getActionBar().setTitle("Signup");
		}
		checkIsLoggedIn();
		loadUiComponents();
	}

	private void checkIsLoggedIn() {
		Object lObject = TheftAppCache.getData(Constants.CACHE_SIGN_UP_INFO,
				this);
		if (lObject != null) {
			startActivity(new Intent(SignupActivity.this, MainActivity.class));
			finish();
		}		
	}
	
	private void loadUiComponents() {
		mUserIdField = (EditText) findViewById(R.id.sign_up_user_id_field);
		mPasswordField = (EditText) findViewById(R.id.sign_up_password_field);
		mConfirmPasswordField = (EditText) findViewById(R.id.sign_up_confirm_password_field);
		mEmailIdField = (EditText) findViewById(R.id.sign_up_email_id_field);
		mPhoneNumberField = (EditText) findViewById(R.id.sign_up_phone_number_field);
		mNameField = (EditText) findViewById(R.id.sign_up_name);
		findViewById(R.id.signup_signup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						validateFields();
					}
				});
		findViewById(R.id.signup_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
	}

	private void validateFields() {
		if (TextUtils.isEmpty(mNameField.getText().toString().trim())) {
			mNameField.setError("Please enter Name");
			return;
		}
		/*if (TextUtils.isEmpty(mUserIdField.getText().toString().trim())) {
			mUserIdField.setError("Please enter User ID");
			return;
		}
		if (TextUtils.isEmpty(mPasswordField.getText().toString().trim())) {
			mPasswordField.setError("Please enter Password");
			return;
		}
		if (TextUtils
				.isEmpty(mConfirmPasswordField.getText().toString().trim())) {
			mConfirmPasswordField.setError("Please enter Confirm Password");
			return;
		}
		if (!mPasswordField.getText().toString().trim()
				.equals(mConfirmPasswordField.getText().toString().trim())) {
			mConfirmPasswordField
					.setError("Password and Confirm Password not matched");
			return;
		}
		*/
		if (TextUtils.isEmpty(mPhoneNumberField.getText().toString().trim())) {
			mPhoneNumberField.setError("Please enter Phone Number");
			return;
		}
		if (TextUtils.isEmpty(mEmailIdField.getText().toString().trim())) {
			mEmailIdField.setError("Please enter Email ID");
			return;
		}
		

		signUpSelected();
	}

	private void signUpSelected() {
		Account lSignUpInfo = new Account();
		lSignUpInfo.setUserId(mUserIdField.getText().toString().trim());
		lSignUpInfo.setPassword(mPasswordField.getText().toString().trim());
		lSignUpInfo.setPhoneNumber(mPhoneNumberField.getText().toString()
				.trim());
		lSignUpInfo.setEmail(mEmailIdField.getText().toString().trim());
		lSignUpInfo.setTimeDelay("10");
		lSignUpInfo.setName(mNameField.getText().toString().trim());
		TheftAppCache.putData(Constants.CACHE_SIGN_UP_INFO, this, lSignUpInfo,
				TheftAppCache.CACHE_LOACATION_DISK);

		DatabaseHandler.getOldInstance(this).Add_Contact(lSignUpInfo);

		finish();
		startActivity(new Intent(SignupActivity.this, MainActivity.class));
	}
}
