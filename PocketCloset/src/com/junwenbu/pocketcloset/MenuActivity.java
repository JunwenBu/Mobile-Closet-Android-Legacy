/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import com.junwenbu.data.DBOpenHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

// Main Menu of the application
public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// create database
		DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
		dbOpenHelper.getWritableDatabase();
	}

	// Camera
	public void clickBtnCamera(View view) {
		startActivity(new Intent(this, CameraActivity.class));
	}

	// Closet
	public void clickBtnCloset(View view) {
		startActivity(new Intent(this, ClosetActivity.class));
	}

	// Quit
	public void clickBtnQuit(View view) {
		finish();
	}
}