/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// DBOpenHelper is used to create tables of database
public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		// context, name, factory, version
		super(context, "closet.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// called the first time the database is created
		// will be stored in folder /<package name>/database
		db.execSQL("CREATE TABLE item ("
				+ "_id integer primary key autoincrement,"
				+ "file varchar(30)," + "tag integer NULL,"
				+ "type integer NULL," + "weather integer NULL,"
				+ "calendar integer NULL," + "style integer NULL" + ");");

		db.execSQL("CREATE TABLE calendar (" + "_id integer primary key,"
				+ "type1 integer NULL," + "type2 integer NULL,"
				+ "type3 integer NULL," + "type4 integer NULL" + ");");

		db.execSQL("CREATE TABLE style (" + "_id integer primary key,"
				+ "type1 integer NULL," + "type2 integer NULL,"
				+ "type3 integer NULL," + "type4 integer NULL" + ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// called when the version number changed
		// for example if the version number changed to 2
	}

}
