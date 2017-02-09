package com.mobile.theftapp.database;

import java.io.Serializable;

public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private variables
	public int _id;
	public String _userId;
	public String _phone_number;
	public String _email;
	private String _password;
	private String _timeDelay;
	private String _name;
	public Account() {
	}

	// constructor
	public Account(String userId, String password, String _phone_number,
			String _email, String _timeDelay) {
		this._userId = userId;
		this._password = password;
		this._phone_number = _phone_number;
		this._email = _email;
		this._timeDelay = _timeDelay;
	}

	public String getUserId() {
		return _userId;
	}

	public void setUserId(String userId) {
		this._userId = userId;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getPassword() {
		return this._password;
	}

	// setting name
	public void setPassword(String password) {
		this._password = password;
	}

	// getting phone number
	public String getPhoneNumber() {
		return this._phone_number;
	}

	// setting phone number
	public void setPhoneNumber(String phone_number) {
		this._phone_number = phone_number;
	}

	// getting email
	public String getEmail() {
		return this._email;
	}

	// setting email
	public void setEmail(String email) {
		this._email = email;
	}

	// getting timeDeay
	public String getTimeDelay() {
		return _timeDelay;
	}

	// Setting TimeDelay
	public void setTimeDelay(String timeDelay) {

		this._timeDelay = timeDelay;
	}

	// getting name
	public String getName() {
		return _name;
	}

	// Setting Name
	public void setName(String name) {

		this._name = name;
	}
}