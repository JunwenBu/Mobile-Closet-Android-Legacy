/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */

package com.junwenbu.pocketcloset;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

// implement tab view
@SuppressWarnings("deprecation")
public class ClosetActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set application to full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_closet);

		TabHost tabHost = getTabHost();

		// Tab for Items
		TabSpec tagspec = tabHost.newTabSpec("Items");
		tagspec.setIndicator("Items", null);
		Intent tagIntent = new Intent(this, TagActivity.class);
		tagspec.setContent(tagIntent);

		// Tab for Calendar
		TabSpec calendarspec = tabHost.newTabSpec("Calendar");
		// setting Title and Icon for the Tab
		calendarspec.setIndicator("Calendar", null);
		Intent calendarIntent = new Intent(this, CalendarActivity.class);
		calendarspec.setContent(calendarIntent);

		// Tab for Style
		TabSpec stylespec = tabHost.newTabSpec("Style");
		stylespec.setIndicator("Style", null);
		Intent styleIntent = new Intent(this, StyleActivity.class);
		stylespec.setContent(styleIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(tagspec); // Adding tag tab
		tabHost.addTab(calendarspec); // Adding calendar tab
		tabHost.addTab(stylespec); // Adding style tab
	}
}