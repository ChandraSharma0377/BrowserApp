package com.cbs.lms.app.db;

import java.util.ArrayList;

import com.cbs.lms.app.pojos.UrlsDto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelperClass {
	private Context mContext;

	public DataHelperClass(Context con) {
		mContext = con;
	}

	public void addUrlData(UrlsDto urlDto) {
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase SQDB = DBOHC.getWritableDatabase();
		ContentValues values = new ContentValues();

		SQDB.beginTransaction();
		try {

			values.put(DBOHC.COLUMN_URL, urlDto.getUrl());
			values.put(DBOHC.COLUMN_ADDITIONAL_1, "");
			values.put(DBOHC.COLUMN_ADDITIONAL_2, "");
			values.put(DBOHC.COLUMN_ADDITIONAL_3, "");
			SQDB.insert(DBOHC.TABLE_URLS, null, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		SQDB.setTransactionSuccessful();
		SQDB.endTransaction();

	}

	public ArrayList<UrlsDto> getUrlData() {
		{
			DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
			SQLiteDatabase SQDB = DBOHC.getWritableDatabase();
			ArrayList<UrlsDto> urlList = new ArrayList<UrlsDto>();
			String myQuery = "SELECT  * FROM " + DBOHC.TABLE_URLS;
			try {
				Cursor cursor = SQDB.rawQuery(myQuery, null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						do {
							try {
								UrlsDto ulrItemDto = new UrlsDto();
								ulrItemDto.setUniqueId(cursor.getInt(0));
								ulrItemDto.setUrl(cursor.getString(1));

								urlList.add(ulrItemDto);
							} catch (Exception e) {
								Log.d("DB_EXCEPTION" + "OBJ_NOT : ", e.getMessage());
							}
						} while (cursor.moveToNext());
					}
				}
				cursor.close();
				// SQDB.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return urlList;
		}

	}
	
	public ArrayList<String> getUrlList() {
		{
			DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
			SQLiteDatabase SQDB = DBOHC.getWritableDatabase();
			ArrayList<String> urlList = new ArrayList<String>();
			String myQuery = "SELECT  * FROM " + DBOHC.TABLE_URLS;
			try {
				Cursor cursor = SQDB.rawQuery(myQuery, null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						do {
							try {
								urlList.add(cursor.getString(1));
							} catch (Exception e) {
								Log.d("DB_EXCEPTION" + "OBJ_NOT : ", e.getMessage());
							}
						} while (cursor.moveToNext());
					}
				}
				cursor.close();
				// SQDB.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return urlList;
		}

	}

	public boolean isRecordExist() {
		boolean isexit = false;
		String selectQuery = "SELECT * FROM URL_DETAILS ";
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		isexit = cursor.getCount() > 0 ? true : false;
		if (null != cursor)
			cursor.close();

		return isexit;

	}

	public boolean isValidUrl(String url) {
		boolean isexit = false;
		String selectQuery = "SELECT * FROM URL_DETAILS WHERE lower(URLS) LIKE '"+url+"%';";
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		isexit = cursor.getCount() > 0 ? true : false;
		if (null != cursor)
			cursor.close();
		return isexit;

	}
	public boolean isRecordExist(int unique_id) {
		boolean isexit = false;
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();

		Cursor cursor = null;
		String sql = "SELECT * FROM URL_DETAILS WHERE UNIQUE_ID=" + unique_id;
		cursor = db.rawQuery(sql, null);

		isexit = cursor.getCount() > 0 ? true : false;
		if (null != cursor)
			cursor.close();

		return isexit;

	}

	public boolean deleteRecord(int name) {
		DBOpenHelperClass DBOHC = DBOpenHelperClass.getSharedObject(mContext);
		SQLiteDatabase db = DBOHC.getWritableDatabase();
		return db.delete(DBOHC.TABLE_URLS, DBOHC.COLUMN_UNIQUE_ID + "=" + name, null) > 0;
	}

}
