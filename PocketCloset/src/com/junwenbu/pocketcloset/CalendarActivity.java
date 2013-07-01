/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */

package com.junwenbu.pocketcloset;

// The methods of Calendar View are provided by Chris Gao
// Copyright (C) 2011 Chris Gao <chris@exina.net>

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.junwenbu.data.Calendar;
import com.junwenbu.data.CalendarService;
import com.junwenbu.data.Item;
import com.junwenbu.data.ItemService;
import com.junwenbu.data.Style;
import com.junwenbu.data.StyleService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// arrange clothing items based on the calendar
@SuppressWarnings({ "deprecation", "unused" })
public class CalendarActivity extends Activity implements
		CalendarView.OnCellTouchListener {
	// used for calendar view
	private CalendarView mView = null;
	private Handler mHandler = new Handler();
	private Button btCenter;
	private Rect ecBounds;

	private View listV, calendarV, buttonV, styleV;
	private ImageView currentViewButton, coatV, shirtV, trouV, accessV;
	private int calendarId;
	private Item item;
	private ListView listView; // list of items
	private ListView styleListView; // list of styles
	private String filePath;
	private File directory;
	private final String ALBUM_NAME = "ClosetAlbum";

	// database service
	private ItemService itemService;
	private CalendarService calendarService;
	private StyleService styleService;

	private int coat, shirt, trousers, accessory;
	private Bitmap coatBp, shirtBp, trouBp, accessBp;
	private int currentType;

	private TypeAdapter adapter;
	private Style style;

	private HashMap<String, Integer> styleMap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		currentViewButton = null;
		calendarId = -1;
		coat = shirt = trousers = accessory = -1;
		currentType = -1;
		mView = (CalendarView) findViewById(R.id.calendar);
		mView.setOnCellTouchListener(this);
		coatV = (ImageView) findViewById(R.id.coatBtn);
		shirtV = (ImageView) findViewById(R.id.shirtBtn);
		trouV = (ImageView) findViewById(R.id.trousersBtn);
		accessV = (ImageView) findViewById(R.id.accessoryBtn);
		coatBp = BitmapFactory.decodeResource(getResources(), R.drawable.coat);
		shirtBp = BitmapFactory
				.decodeResource(getResources(), R.drawable.shirt);
		trouBp = BitmapFactory.decodeResource(getResources(), R.drawable.pants);
		accessBp = BitmapFactory.decodeResource(getResources(),
				R.drawable.scarf);
		coatV.setImageBitmap(coatBp);
		shirtV.setImageBitmap(shirtBp);
		trouV.setImageBitmap(trouBp);
		accessV.setImageBitmap(accessBp);

		itemService = new ItemService(this);
		calendarService = new CalendarService(this);
		styleService = new StyleService(this);
		filePath = getAlbumStorageDir(getApplicationContext(), ALBUM_NAME)
				.getPath();

		listV = findViewById(R.id.calendarListViewLayout);
		calendarV = findViewById(R.id.calendarLinearLayout);
		buttonV = findViewById(R.id.buttonLayout);
		styleV = findViewById(R.id.calendarStylelistlayout);

		listView = (ListView) findViewById(R.id.calendarlistView);
		listView.setOnItemClickListener(new myListener());

		styleListView = (ListView) findViewById(R.id.calendarStylelistView);
		styleListView.setOnItemClickListener(new StyleListListener());

		btCenter = (Button) findViewById(R.id.centerBtn);
		btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
	}

	// used to display list view of items
	// use adapter defined by myself
	private void showList(int type) {
		List<Item> items = itemService.getDataByType(type);
		adapter = new TypeAdapter(this, items, R.layout.calendar_item,
				filePath, type);
		listView.setAdapter(adapter);
	}

	// used to display list view of styles
	// use the SimpleAdapter
	private void showStyleList() {
		styleMap = new HashMap<String, Integer>(); // style name, id in style
													// table
		List<Style> styles = styleService.getAllData();
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		int i = 1;
		for (Style style : styles) {
			String name = "My style " + i;
			styleMap.put(name, style.getId());
			i++;
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", name);
			data.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.style_item, new String[] { "name" },
				new int[] { R.id.styleiteminlist });
		styleListView.setAdapter(adapter);
	}

	// called when users click the item on the item listview
	private final class myListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			item = (Item) listView.getItemAtPosition(position);
			currentViewButton.setImageBitmap(BitmapFactory.decodeFile(filePath
					+ "/" + item.getFile()));
			switch (currentType) {
			case 0:
				coat = item.getId();
				break;
			case 1:
				shirt = item.getId();
				break;
			case 2:
				trousers = item.getId();
				break;
			case 3:
				accessory = item.getId();
				break;
			default:
				break;
			}

			listV.setVisibility(ViewGroup.INVISIBLE);
			calendarV.setVisibility(ViewGroup.VISIBLE);
			buttonV.setVisibility(ViewGroup.VISIBLE);
		}
	}

	// called when users click elements of the style listview
	private final class StyleListListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			coat = shirt = trousers = accessory = -1;
			String name = (String) ((HashMap<String, Object>) styleListView
					.getItemAtPosition(position)).get("name");
			style = styleService.getByID(styleMap.get(name));
			coat = style.getType1();
			shirt = style.getType2();
			trousers = style.getType3();
			accessory = style.getType4();

			boolean exist = false;
			// 0:coat 1:shirt 2:trousers 3:accessory
			if (itemService.getDataById(coat) != null
					&& itemService.getDataById(coat).getType() == 0) {
				exist = true;
				coatV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ itemService.getFileName(coat)));
			} else
				coatV.setImageBitmap(coatBp);
			if (itemService.getDataById(shirt) != null
					&& itemService.getDataById(shirt).getType() == 1) {
				exist = true;
				shirtV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ itemService.getFileName(shirt)));
			} else
				shirtV.setImageBitmap(shirtBp);
			if (itemService.getDataById(trousers) != null
					&& itemService.getDataById(trousers).getType() == 2) {
				exist = true;
				trouV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ itemService.getFileName(trousers)));
			} else
				trouV.setImageBitmap(trouBp);
			if (itemService.getDataById(accessory) != null
					&& itemService.getDataById(accessory).getType() == 3) {
				exist = true;
				accessV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ itemService.getFileName(accessory)));
			} else
				accessV.setImageBitmap(accessBp);
			if (!exist) {
				styleService.delete(generateId());
				coat = shirt = trousers = accessory = -1;
			}
			styleV.setVisibility(ViewGroup.INVISIBLE);
			calendarV.setVisibility(ViewGroup.VISIBLE);
			buttonV.setVisibility(ViewGroup.VISIBLE);
		}
	}

	// generate style table id according items
	private int generateId() {
		// A better generate algorithm will be given in later versions,
		// I suppose a people can save at most 99 items in a single type.
		int id = 0;
		id += (coat == -1 ? 0 : coat * 1000000);
		id += (shirt == -1 ? 0 : shirt * 10000);
		id += (trousers == -1 ? 0 : trousers * 100);
		id += (accessory == -1 ? 0 : accessory);
		return id;
	}

	// called when user click the "Cancel and Go back" button
	public void calendarCancelStyleList(View view) {
		styleV.setVisibility(ViewGroup.INVISIBLE);
		calendarV.setVisibility(ViewGroup.VISIBLE);
		buttonV.setVisibility(ViewGroup.VISIBLE);
	}

	// called save button clicked
	public void clickSave(View view) {
		if (coat != -1 || shirt != -1 || trousers != -1 || accessory != -1) {
			if (calendarId == -1) {
				Toast.makeText(this, "Please select a date.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Calendar calendar = new Calendar(calendarId, coat, shirt, trousers,
					accessory);
			if (calendarService.getCount(calendarId) == 0) {
				calendarService.insert(calendar);
			} else { // update
				calendarService.update(calendar, calendarId);
			}
		} else {
			Toast.makeText(this, "No items to save!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// called when style button clicked
	public void clickStyle(View view) {
		styleV.setVisibility(ViewGroup.VISIBLE);
		calendarV.setVisibility(ViewGroup.INVISIBLE);
		buttonV.setVisibility(ViewGroup.INVISIBLE);
		showStyleList();
	}

	// called when clear button clicked
	public void clickClear(View view) {
		coatV.setImageBitmap(coatBp);
		shirtV.setImageBitmap(shirtBp);
		trouV.setImageBitmap(trouBp);
		accessV.setImageBitmap(accessBp);
		coat = shirt = trousers = accessory = -1;
		calendarService.delete(calendarId);
	}

	// called when user click the cancel button in the item listview
	public void clickCancel(View view) {
		listV.setVisibility(ViewGroup.INVISIBLE);
		calendarV.setVisibility(ViewGroup.VISIBLE);
		buttonV.setVisibility(ViewGroup.VISIBLE);
	}

	// called when the user click the empty coat imageview
	public void clickCoat(View view) {
		currentViewButton = coatV;
		currentType = 0;
		listV.setVisibility(ViewGroup.VISIBLE);
		calendarV.setVisibility(ViewGroup.INVISIBLE);
		buttonV.setVisibility(ViewGroup.INVISIBLE);
		showList(0);
	}

	// called when the user click the empty shirt imageview
	public void clickShirt(View view) {
		currentViewButton = shirtV;
		currentType = 1;
		listV.setVisibility(ViewGroup.VISIBLE);
		calendarV.setVisibility(ViewGroup.INVISIBLE);
		buttonV.setVisibility(ViewGroup.INVISIBLE);
		showList(1);
	}

	// called when the user click the empty trousers imageview
	public void clickTrousers(View view) {
		currentViewButton = trouV;
		currentType = 2;
		listV.setVisibility(ViewGroup.VISIBLE);
		calendarV.setVisibility(ViewGroup.INVISIBLE);
		buttonV.setVisibility(ViewGroup.INVISIBLE);
		showList(2);
	}

	// called when the user click the empty accessory imageview
	public void clickAccessory(View view) {
		currentViewButton = accessV;
		currentType = 3;
		listV.setVisibility(ViewGroup.VISIBLE);
		calendarV.setVisibility(ViewGroup.INVISIBLE);
		buttonV.setVisibility(ViewGroup.INVISIBLE);
		showList(3);
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

	// called when the user click the left arrow of the calendar
	public void clickLeft(View view) {
		mView.previousMonth();
		btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
	}

	// called when the user click the right arrow of the calendar
	public void clickRight(View view) {
		mView.nextMonth();
		btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
	}

	// called when click the calendar
	public void onTouch(Cell cell) {
		if (cell.mPaint.getColor() == Color.GRAY) {
			// switch to previous month for GRAY represents last month
			mView.previousMonth();
			btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else if (cell.mPaint.getColor() == Color.LTGRAY) {
			// switch to next month for LTGRAY represents next month
			mView.nextMonth();
			btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else { // click on days in this month
			coat = shirt = trousers = accessory = -1;
			calendarId = cell.getDayOfMonth() + mView.getMonth() * 100
					+ mView.getYear() * 10000;

			ecBounds = cell.getBound();
			mView.getDate();
			mView.mDecoraClick.setBounds(ecBounds);
			mView.invalidate();

			Calendar temp = calendarService.getByID(calendarId);
			if (temp != null) { // calendar has saved clothing item group
				coat = temp.getType1();
				shirt = temp.getType2();
				trousers = temp.getType3();
				accessory = temp.getType4();
				boolean exist = false;
				if (coat >= 0 && itemService.getCountByID(coat) > 0) {
					exist = true;
					coatV.setImageBitmap(BitmapFactory.decodeFile(filePath
							+ "/" + itemService.getFileName(coat)));
				} else
					coatV.setImageBitmap(coatBp);
				if (shirt >= 0 && itemService.getCountByID(shirt) > 0) {
					exist = true;
					shirtV.setImageBitmap(BitmapFactory.decodeFile(filePath
							+ "/" + itemService.getFileName(shirt)));
				} else
					shirtV.setImageBitmap(shirtBp);
				if (trousers >= 0 && itemService.getCountByID(trousers) > 0) {
					exist = true;
					trouV.setImageBitmap(BitmapFactory.decodeFile(filePath
							+ "/" + itemService.getFileName(trousers)));
				} else
					trouV.setImageBitmap(trouBp);
				if (accessory >= 0 && itemService.getCountByID(accessory) > 0) {
					exist = true;
					accessV.setImageBitmap(BitmapFactory.decodeFile(filePath
							+ "/" + itemService.getFileName(accessory)));
				} else
					accessV.setImageBitmap(accessBp);

				if (!exist) {
					calendarService.delete(calendarId);
				}
			} else {
				coat = shirt = trousers = accessory = -1;
				currentType = -1;
				coatV.setImageBitmap(coatBp);
				shirtV.setImageBitmap(shirtBp);
				trouV.setImageBitmap(trouBp);
				accessV.setImageBitmap(accessBp);
			}

			return;
		}
		
		// update the center button above the calendar
		mHandler.post(new Runnable() {
			public void run() {
				btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
			}
		});
	}

}