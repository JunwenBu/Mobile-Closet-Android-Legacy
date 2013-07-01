/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import java.io.File;
import java.util.List;

import com.junwenbu.data.Item;
import com.junwenbu.data.ItemService;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class TagActivity extends Activity {
	private File directory;
	private String filePath;
	private final String ALBUM_NAME = "ClosetAlbum";
	private ListView listView;
	private ItemService itemService;
	private View selectV;
	private View listV;
	private RadioGroup weatherGroup, typeGroup;
	private int weather, type;
	private Item item;
	ItemAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);

		selectV = findViewById(R.id.selectLinearLayout);
		listV = findViewById(R.id.listViewLienarLayout);

		itemService = new ItemService(this);
		filePath = getAlbumStorageDir(getApplicationContext(), ALBUM_NAME)
				.getPath();
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new tagListener());
		weatherGroup = (RadioGroup) findViewById(R.id.radioWeather);
		typeGroup = (RadioGroup) findViewById(R.id.radioType);
		showList();
	}

	// get album directory
	private File getAlbumStorageDir(Context context, String albumName) {
		// Get the directory for the app's private pictures directory.
		if (directory == null) {
			directory = new File(
					context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					albumName);
		}
		return directory;
	}

	// use adapter defined by myself to show list
	private void showList() {
		List<Item> items = itemService.getAllData();
		adapter = new ItemAdapter(this, items, R.layout.item, filePath);
		listView.setAdapter(adapter);
	}

	private final class tagListener implements OnItemClickListener {
		// parent is ListView, view is list item, position is index
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			item = (Item) listView.getItemAtPosition(position);
			typeGroup.clearCheck();
			weatherGroup.clearCheck();
			listV.setVisibility(ViewGroup.INVISIBLE);
			selectV.setVisibility(ViewGroup.VISIBLE);
		}
	}

	/* <---- Buttons on tag screen ----> */

	public void clickCancel(View view) {
		selectV.setVisibility(ViewGroup.INVISIBLE);
		listV.setVisibility(ViewGroup.VISIBLE);
	}

	public void clickSave(View view) {
		weather = weatherGroup.getCheckedRadioButtonId();
		type = typeGroup.getCheckedRadioButtonId();
		if (weather != -1 && type != -1) {
			int wi = weatherGroup.indexOfChild(weatherGroup
					.findViewById(weather));
			int ti = typeGroup.indexOfChild(typeGroup.findViewById(type));
			Log.i("11", wi + "  " + ti);
			itemService.update(item.getId(), wi, ti);
			selectV.setVisibility(ViewGroup.INVISIBLE);
			listV.setVisibility(ViewGroup.VISIBLE);
			showList();

		} else {
			Toast.makeText(this, "Please select Weather and Type.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void clickDelete(View view) {
		itemService.delete(item.getId());
		File delFile = new File(filePath + "/" + item.getFile());
		if (delFile.exists()) {
			delFile.delete();
			showList();
		}
		selectV.setVisibility(ViewGroup.INVISIBLE);
		listV.setVisibility(ViewGroup.VISIBLE);
	}
}
