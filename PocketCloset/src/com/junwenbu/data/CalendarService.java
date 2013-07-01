/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */

package com.junwenbu.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// service class of calendar, provides database operations on calendar
public class CalendarService {
	private DBOpenHelper dbOpenHelper;

	public CalendarService(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}

	// insertion
	public void insert(Calendar calendar) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into calendar (_id, type1, type2, type3, type4) values (?, ?, ?, ?, ?)",
				new Object[] { calendar.getId(), calendar.getType1(),
						calendar.getType2(), calendar.getType3(),
						calendar.getType4() });
		db.close();
	}

	// deletion
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from calendar where _id=?", new Object[] { id });
		db.close();
	}

	// update
	public void update(Calendar calendar, Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update calendar set type1=?, type2=?, type3=?, type4=? where _id=?",
				new Object[] { calendar.getType1(), calendar.getType2(),
						calendar.getType3(), calendar.getType4(), id });
		db.close();
	}

	// see if data is exist
	public int getCount(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select count(*) from calendar where _id=?",
				new String[] { String.valueOf(id) });
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}

	// get calendar by id
	public Calendar getByID(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from calendar where _id=?",
				new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) {
			Integer calendarid = cursor.getInt(cursor.getColumnIndex("_id"));
			Integer type1 = cursor.getInt(cursor.getColumnIndex("type1"));
			Integer type2 = cursor.getInt(cursor.getColumnIndex("type2"));
			Integer type3 = cursor.getInt(cursor.getColumnIndex("type3"));
			Integer type4 = cursor.getInt(cursor.getColumnIndex("type4"));
			return new Calendar(calendarid, type1, type2, type3, type4);
		}
		cursor.close();
		db.close();
		return null;
	}

}
