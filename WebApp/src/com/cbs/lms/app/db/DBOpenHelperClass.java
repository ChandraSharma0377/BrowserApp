package com.cbs.lms.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelperClass extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "lms_database";
	private static final int DATABASE_VERSION = 1;
	private final String TEXT_TYPE = " TEXT";
	private final String COMMA_SEP = ", ";
	private static DBOpenHelperClass instance;
	public Context context;

	// Table
	public final String TABLE_URLS = "URL_DETAILS";

	// Column
	public final String COLUMN_UNIQUE_ID = "UNIQUE_ID"; // PRIMARY KEY
	public final String COLUMN_URL = "URLS ";

	public final String COLUMN_ADDITIONAL_1 = "ADDITIONAL_1";
	public final String COLUMN_ADDITIONAL_2 = "ADDITIONAL_2";
	public final String COLUMN_ADDITIONAL_3 = "ADDITIONAL_3";

	private final String CREATE_TABLE_SCAN_DETAIL = "CREATE TABLE " + TABLE_URLS + " ( " + COLUMN_UNIQUE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_URL + TEXT_TYPE + COMMA_SEP + COLUMN_ADDITIONAL_1
			+ TEXT_TYPE + COMMA_SEP + COLUMN_ADDITIONAL_2 + TEXT_TYPE + COMMA_SEP + COLUMN_ADDITIONAL_3 + TEXT_TYPE
			+ " );";

	public static DBOpenHelperClass getSharedObject(Context context) {
		if (instance == null) {
			instance = new DBOpenHelperClass(context);
		}
		instance.context = context;
		return instance;
	}

	public DBOpenHelperClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase sqdb = super.getWritableDatabase();
		return sqdb;
	}

	public SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TABLE_SCAN_DETAIL);
		} catch (Exception ex) {
			Log.d("DBException", ex.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
