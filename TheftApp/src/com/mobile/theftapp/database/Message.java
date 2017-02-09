package com.mobile.theftapp.database;

public class Message {
	private String _number;
	private String _msg;
	private String _date;
	private int _id;

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public String getNumber() {
		return _number;
	}

	public void setNumber(String _number) {
		this._number = _number;
	}

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String _msg) {
		this._msg = _msg;
	}
	
	public String getDate() {
		return _date;
	}
	
	public void setDate(String _date) {
		this._date = _date;
	}

}
