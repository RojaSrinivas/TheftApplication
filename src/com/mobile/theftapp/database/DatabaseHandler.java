package com.mobile.theftapp.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobile.theftapp.utils.Constants;
import com.mobile.theftapp.utils.TheftAppCache;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_MESSAGES = "messages";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USERID = "userId";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_PH_NO = "phone_number";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_TIMEDELAY = "timeDelay";
	private final ArrayList<Account> contact_list = new ArrayList<Account>();

	// Contacts Table Columns names
	private static final String KEY_MSG_ID = "id";
	private static final String KEY_MSG = "msg";
	private static final String KEY_DATE = "date";
	private final ArrayList<Message> message_list = new ArrayList<Message>();

	private static DatabaseHandler mInstance = null;

	private Context mContext;

	public static synchronized DatabaseHandler getOldInstance(Context ctx) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (mInstance == null) {
			mInstance = new DatabaseHandler(ctx.getApplicationContext());
		}
		return mInstance;
	}

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERID + " TEXT,"
				+ KEY_PASSWORD + " TEXT," + KEY_PH_NO + " TEXT," + KEY_EMAIL
				+ " TEXT," + KEY_TIMEDELAY + " TEXT " + ")";
		String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_MSG + " TEXT,"
				+ KEY_PH_NO + " TEXT,"+ KEY_DATE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_MESSAGES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
		// Create tables again
		onCreate(db);
	}

	public boolean checkLoginDetails(String pUserId, String pPassword) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS
				+ " WHERE " + KEY_USERID + "=? AND " + KEY_PASSWORD + "=?",
				new String[] { pUserId, pPassword });
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				if (cursor != null)
					cursor.moveToFirst();

				Account contact = new Account();
				contact.setID(cursor.getInt(cursor
						.getColumnIndexOrThrow(KEY_USERID)));
				contact.setUserId(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_USERID)));
				contact.setPassword(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_PASSWORD)));
				contact.setPhoneNumber(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_PH_NO)));
				contact.setEmail(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_EMAIL)));
				contact.setTimeDelay(cursor.getString(cursor
						.getColumnIndexOrThrow(KEY_TIMEDELAY)));
				TheftAppCache.putData(Constants.CACHE_SIGN_UP_INFO, mContext,
						contact, TheftAppCache.CACHE_LOACATION_DISK);
				return true;
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return false;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void Add_Contact(Account contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USERID, contact.getUserId()); // Contact Name
		values.put(KEY_PASSWORD, contact.getPassword()); // Contact Name
		values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
		values.put(KEY_EMAIL, contact.getEmail()); // Contact Email
		values.put(KEY_TIMEDELAY, contact.getTimeDelay());
		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Account Get_Contact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_USERID, KEY_PASSWORD, KEY_PH_NO, KEY_EMAIL },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Account contact = new Account();
		contact.setID(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
		contact.setUserId(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_USERID)));
		contact.setPassword(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_PASSWORD)));
		contact.setPhoneNumber(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_PH_NO)));
		contact.setEmail(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_EMAIL)));
		contact.setTimeDelay(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_TIMEDELAY)));
		// return contact
		cursor.close();
		db.close();

		return contact;
	}

	// Getting All Contacts
	public ArrayList<Account> Get_Contacts() {
		try {
			contact_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Account contact = new Account();
					contact.setID(cursor.getInt(cursor
							.getColumnIndexOrThrow(KEY_ID)));
					contact.setUserId(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_USERID)));
					contact.setPassword(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_PASSWORD)));
					contact.setPhoneNumber(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_PH_NO)));
					contact.setEmail(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_EMAIL)));
					contact.setTimeDelay(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_TIMEDELAY)));
					// Adding contact to list
					contact_list.add(contact);
				} while (cursor.moveToNext());
			}

			// return contact list
			cursor.close();
			db.close();
			return contact_list;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_contact", "" + e);
		}

		return contact_list;
	}

	// Updating single contact
	public int Update_Contact(Account contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERID, contact.getUserId());
		values.put(KEY_PASSWORD, contact.getPassword());
		values.put(KEY_PH_NO, contact.getPhoneNumber());
		values.put(KEY_EMAIL, contact.getEmail());
		values.put(KEY_TIMEDELAY, contact.getTimeDelay());
		TheftAppCache.putData(Constants.CACHE_SIGN_UP_INFO, mContext, contact,
				TheftAppCache.CACHE_LOACATION_DISK);
		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void Delete_Contact(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Getting contacts Count
	public int Get_Total_Contacts() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new message
	public void Add_Message(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PH_NO, message.getNumber()); // Contact Email
		values.put(KEY_MSG, message.getMsg());
		values.put(KEY_DATE, message.getDate());
		// Inserting Row
		db.insert(TABLE_MESSAGES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single message
	public Message Get_Message(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_ID,
				KEY_PH_NO, KEY_MSG,KEY_DATE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Message message = new Message();
		message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
		message.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MSG)));
		message.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
		message.setNumber(cursor.getString(cursor
				.getColumnIndexOrThrow(KEY_PH_NO)));
		// return contact
		cursor.close();
		db.close();

		return message;
	}

	// Getting All Messages
	public ArrayList<Message> Get_Messages() {
		try {
			message_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Message message = new Message();
					message.setId(cursor.getInt(cursor
							.getColumnIndexOrThrow(KEY_ID)));
					message.setNumber(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_PH_NO)));
					message.setDate(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_DATE)));
					message.setMsg(cursor.getString(cursor
							.getColumnIndexOrThrow(KEY_MSG)));
					// Adding contact to list
					message_list.add(message);
				} while (cursor.moveToNext());
			}

			// return message list
			cursor.close();
			db.close();
			return message_list;
		} catch (Exception e) {
			Log.e("all_message", "" + e);
		}

		return message_list;
	}

	// Updating single Message
	public int Update_Message(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PH_NO, message.getNumber());
		values.put(KEY_MSG, message.getMsg());
		values.put(KEY_DATE, message.getDate());
		// updating row
		return db.update(TABLE_MESSAGES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(message.getId()) });
	}

	// Deleting single message
	public void Delete_Message(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MESSAGES, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Getting messages Count
	public int Get_Total_Messages() {
		String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Deleting all messages
	public void Delete_All_Messages() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MESSAGES, null, null);
		db.close();
	}

}
