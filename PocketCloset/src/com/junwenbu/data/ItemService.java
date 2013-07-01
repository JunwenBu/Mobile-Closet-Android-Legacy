/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// item service, provides database operations
public class ItemService {
	private DBOpenHelper dbOpenHelper;

	public ItemService(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}

	// insertion
	public void insert(Item item) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"insert into item(file, tag, type, weather, calendar, style) values (?, ?, ?, ?, ?, ?)",
				new Object[] { item.getFile(), item.getTag(), item.getType(),
						item.getWeather(), item.getCalendar(), item.getStyle() });
		db.close();
	}

	// deletion
	public void delete(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from item where _id=?", new Object[] { id });
		db.close();
	}

	// update
	public void update(Item item, Integer id) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update item set file=?, tag=?, type=?, weather=?, calendar=?, style=? where _id=?",
				new Object[] { item.getFile(), item.getTag(), item.getType(),
						item.getWeather(), item.getCalendar(), item.getStyle(),
						id });
		db.close();
	}

	public void update(Integer id, Integer weather, Integer type) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update item set tag=1, type=?, weather=? where _id=?",
				new Object[] { type, weather, id });
	}

	// search
	public List<Item> getScrollData(int offset, int maxResult) {
		List<Item> items = new ArrayList<Item>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from item order by _id desc limit ?,?", new String[] {
						String.valueOf(offset), String.valueOf(maxResult) });
		while (cursor.moveToNext()) { // similar as ResultSet.next() in JDBC
			Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
			String file = cursor.getString(cursor.getColumnIndex("file"));
			Integer tag = cursor.getInt(cursor.getColumnIndex("tag"));
			Integer type = cursor.getInt(cursor.getColumnIndex("type"));
			Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));
			Integer calendar = cursor.getInt(cursor.getColumnIndex("calendar"));
			Integer style = cursor.getInt(cursor.getColumnIndex("style"));
			items.add(new Item(id, file, tag, type, weather, calendar, style));
		}
		cursor.close();
		db.close();
		return items;
	}

	// select * from database
	public List<Item> getAllData() {
		List<Item> items = new ArrayList<Item>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery("select * from item order by _id desc", null);
		while (cursor.moveToNext()) { // similar as ResultSet.next() in JDBC
			Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
			String file = cursor.getString(cursor.getColumnIndex("file"));
			Integer tag = cursor.getInt(cursor.getColumnIndex("tag"));
			Integer type = cursor.getInt(cursor.getColumnIndex("type"));
			Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));
			Integer calendar = cursor.getInt(cursor.getColumnIndex("calendar"));
			Integer style = cursor.getInt(cursor.getColumnIndex("style"));
			items.add(new Item(id, file, tag, type, weather, calendar, style));
		}
		cursor.close();
		db.close();
		return items;
	}

	// get data according to type
	public List<Item> getDataByType(Integer t) {
		List<Item> items = new ArrayList<Item>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from item where type =? order by _id asc",
				new String[] { String.valueOf(t) });
		while (cursor.moveToNext()) { // similar as ResultSet.next() in JDBC
			Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
			String file = cursor.getString(cursor.getColumnIndex("file"));
			Integer tag = cursor.getInt(cursor.getColumnIndex("tag"));
			Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));
			Integer calendar = cursor.getInt(cursor.getColumnIndex("calendar"));
			Integer style = cursor.getInt(cursor.getColumnIndex("style"));
			items.add(new Item(id, file, tag, t, weather, calendar, style));
		}
		cursor.close();
		db.close();
		return items;
	}

	public List<Item> getDataByTypeAndWeather(Integer t, Integer w) {
		List<Item> items = new ArrayList<Item>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from item where type =? and weather=? order by _id asc",
						new String[] { String.valueOf(t), String.valueOf(w) });
		while (cursor.moveToNext()) { // similar as ResultSet.next() in JDBC
			Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
			String file = cursor.getString(cursor.getColumnIndex("file"));
			Integer tag = cursor.getInt(cursor.getColumnIndex("tag"));
			Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));
			Integer calendar = cursor.getInt(cursor.getColumnIndex("calendar"));
			Integer style = cursor.getInt(cursor.getColumnIndex("style"));
			items.add(new Item(id, file, tag, t, weather, calendar, style));
		}
		cursor.close();
		db.close();
		return items;
	}

	public Item getDataById(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from item where _id = ?",
				new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) { // similar as ResultSet.next() in JDBC
			String file = cursor.getString(cursor.getColumnIndex("file"));
			Integer tag = cursor.getInt(cursor.getColumnIndex("tag"));
			Integer type = cursor.getInt(cursor.getColumnIndex("type"));
			Integer weather = cursor.getInt(cursor.getColumnIndex("weather"));
			Integer calendar = cursor.getInt(cursor.getColumnIndex("calendar"));
			Integer style = cursor.getInt(cursor.getColumnIndex("style"));
			return new Item(id, file, tag, type, weather, calendar, style);
		}
		cursor.close();
		db.close();
		return null;
	}

	public String getFileName(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select file from item where _id=?",
				new String[] { String.valueOf(id) });
		cursor.moveToFirst();
		String result = cursor.getString(0);
		cursor.close();
		return result;
	}

	public long getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from item", null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}

	public int getCountByID(Integer id) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from item where _id=?",
				new String[] { String.valueOf(id) });
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
}
