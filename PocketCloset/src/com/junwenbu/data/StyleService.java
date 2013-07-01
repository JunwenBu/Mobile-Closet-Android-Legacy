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

// service class of style table, provides database operations
public class StyleService {
	private DBOpenHelper dbOpenHelper;

	public StyleService(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}
	
	// insertion
	public void insert(Style style){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into style (_id, type1, type2, type3, type4) values (?, ?, ?, ?, ?)",
				new Object[]{style.getId(), style.getType1(), style.getType2(), style.getType3(), style.getType4()});
		db.close(); 
	}
	
	// deletion
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from style where _id=?", new Object[]{id});
		db.close(); 
	}
	
	// update
	public void update(Style style, Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update style set type1=?, type2=?, type3=?, type4=? where _id=?", 
				new Object[]{style.getType1(), style.getType2(), style.getType3(), style.getType4(), id});
		db.close(); 
	}
	
	// see if has data
	public int getCount(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from style where _id=?", 
				new String[]{String.valueOf(id)}); 
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public List<Style> getAllData(){
		List<Style> styles = new ArrayList<Style>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from style order by _id asc", null); 
		while(cursor.moveToNext()){ // similar as ResultSet.next() in JDBC
			Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
			Integer type1 = cursor.getInt(cursor.getColumnIndex("type1"));
			Integer type2 = cursor.getInt(cursor.getColumnIndex("type2"));
			Integer type3 = cursor.getInt(cursor.getColumnIndex("type3"));
			Integer type4 = cursor.getInt(cursor.getColumnIndex("type4"));
			styles.add(new Style(id, type1, type2, type3, type4));
		}
		cursor.close();
		db.close();
		return styles;
	}
	
	public Style getByID(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from style where _id=?", 
				new String[]{String.valueOf(id)});
		while(cursor.moveToNext()){
			Integer styleid = cursor.getInt(cursor.getColumnIndex("_id"));
			Integer type1 = cursor.getInt(cursor.getColumnIndex("type1"));
			Integer type2 = cursor.getInt(cursor.getColumnIndex("type2"));
			Integer type3 = cursor.getInt(cursor.getColumnIndex("type3"));
			Integer type4 = cursor.getInt(cursor.getColumnIndex("type4"));
			return new Style(styleid, type1, type2, type3, type4);
		}
		cursor.close();
		db.close();
		return null;
	}
	
}
