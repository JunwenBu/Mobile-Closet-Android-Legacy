/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.junwenbu.data.Item;
import com.junwenbu.data.Style;
import com.junwenbu.data.ItemService;
import com.junwenbu.data.StyleService;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressWarnings("deprecation")
public class StyleActivity extends Activity {
	private File directory;
	private String filePath;
	private final String ALBUM_NAME = "ClosetAlbum";
	private Gallery gallery1, gallery2, gallery3, gallery4;
	private ListView styleListView;
	private GalleryAdapter adapter;
	private ItemService itemService;
	private StyleService styleService;
	private int styleId;
	private int coat, shirt, trousers, accessory;
	private ImageView coatV, shirtV, trouV, accessV;
	private Bitmap coatBp, shirtBp, trouBp, accessBp;
	private View stylelistLayout, mainLayout, weatherLayout;

	private HashMap<String, Integer> styleMap;

	private Item item;
	private Style style;

	// used for weather
	private int temp;
	private Button switchBtn;
	private TextView weatherText;
	private int zipcode;
	private Weather myWeather;
	private boolean isWeatherOpen;
	private int weatherValue; // used to query

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_style);

		coat = shirt = trousers = accessory = -1;
		coatV = (ImageView) findViewById(R.id.stylecoat);
		shirtV = (ImageView) findViewById(R.id.styleshirt);
		trouV = (ImageView) findViewById(R.id.styletrousers);
		accessV = (ImageView) findViewById(R.id.styleaccessory);
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

		stylelistLayout = findViewById(R.id.stylelistlayout);
		mainLayout = findViewById(R.id.mainlayout);
		weatherLayout = findViewById(R.id.weatherLayout);

		// used for weather
		isWeatherOpen = false;
		myWeather = new Weather();
		switchBtn = (Button) findViewById(R.id.switchBtn);
		weatherText = (TextView) findViewById(R.id.weatherCondition);
		temp = -9999;
		weatherValue = -1;

		// browser for coat, shirt, trousers and accessory
		gallery1 = (Gallery) findViewById(R.id.gallery1);
		gallery2 = (Gallery) findViewById(R.id.gallery2);
		gallery3 = (Gallery) findViewById(R.id.gallery3);
		gallery4 = (Gallery) findViewById(R.id.gallery4);

		gallery1.setOnItemClickListener(new myListener(gallery1));
		gallery2.setOnItemClickListener(new myListener(gallery2));
		gallery3.setOnItemClickListener(new myListener(gallery3));
		gallery4.setOnItemClickListener(new myListener(gallery4));

		styleListView = (ListView) findViewById(R.id.stylelistView);
		styleListView.setOnItemClickListener(new StyleListListener());

		itemService = new ItemService(this);
		styleService = new StyleService(this);
		filePath = getAlbumStorageDir(getApplicationContext(), ALBUM_NAME)
				.getPath();

		showGalleryOne();
		showGalleryTwo();
		showGalleryThree();
		showGalleryFour();

	}

	// used to display ListView of styles
	// use the SimpleAdapter
	private void showStyleList() {
		styleMap = new HashMap<String, Integer>();

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

	private final class myListener implements OnItemClickListener {
		private Gallery gallery;

		public myListener(Gallery g) {
			gallery = g;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			item = (Item) gallery.getItemAtPosition(position);
			int t = item.getType();
			switch (t) {
			case 0:
				coat = item.getId();
				coatV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ item.getFile()));
				break;
			case 1:
				shirt = item.getId();
				shirtV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ item.getFile()));
				break;
			case 2:
				trousers = item.getId();
				trouV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ item.getFile()));
				break;
			case 3:
				accessory = item.getId();
				accessV.setImageBitmap(BitmapFactory.decodeFile(filePath + "/"
						+ item.getFile()));
				break;
			default:
				break;
			}
		}
	}

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
			// Log.i("Test", " "+coat+" "+shirt);
			boolean exist = false;
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
			mainLayout.setVisibility(ViewGroup.VISIBLE);
			stylelistLayout.setVisibility(ViewGroup.INVISIBLE);
		}
	}

	public void clickView(View view) {
		mainLayout.setVisibility(ViewGroup.INVISIBLE);
		stylelistLayout.setVisibility(ViewGroup.VISIBLE);
		showStyleList();
	}

	// generate style table id according items
	private int generateId() {
		// A better generation algorithm will be given in later versions,
		// I suppose a people can save at most 99 items in a single type.
		int id = 0;
		id += (coat == -1 ? 0 : coat * 1000000);
		id += (shirt == -1 ? 0 : shirt * 10000);
		id += (trousers == -1 ? 0 : trousers * 100);
		id += (accessory == -1 ? 0 : accessory);
		return id;
	}

	// called when click "cancel and go back" button on the style ListView
	public void cancelStyleList(View view) {
		mainLayout.setVisibility(ViewGroup.VISIBLE);
		stylelistLayout.setVisibility(ViewGroup.INVISIBLE);
	}

	// called when click the "refresh" button
	public void refreshStyle(View view) {
		coat = shirt = trousers = accessory = -1;
		showGalleryOne();
		showGalleryTwo();
		showGalleryThree();
		showGalleryFour();
		coatV.setImageBitmap(coatBp);
		shirtV.setImageBitmap(shirtBp);
		trouV.setImageBitmap(trouBp);
		accessV.setImageBitmap(accessBp);
	}

	// called when click the save button
	public void saveStyle(View view) {
		if (coat != -1 || shirt != -1 || trousers != -1 || accessory != -1) {
			styleId = generateId();
			Style style = new Style(styleId, coat, shirt, trousers, accessory);
			if (styleService.getCount(styleId) == 0) {
				styleService.insert(style);
			} else { // update
				styleService.update(style, styleId);
			}
		} else {
			Toast.makeText(this, "No items to save!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// called when click the clear button
	public void clearStyle(View view) {
		coat = shirt = trousers = accessory = -1;
		coatV.setImageBitmap(coatBp);
		shirtV.setImageBitmap(shirtBp);
		trouV.setImageBitmap(trouBp);
		accessV.setImageBitmap(accessBp);
	}

	// called when click the weather button
	public void clickWeather(View view) {
		mainLayout.setVisibility(ViewGroup.INVISIBLE);
		weatherLayout.setVisibility(ViewGroup.VISIBLE);
	}

	// called when click the delete button
	public void deleteStyle(View view) {
		coatV.setImageBitmap(coatBp);
		shirtV.setImageBitmap(shirtBp);
		trouV.setImageBitmap(trouBp);
		accessV.setImageBitmap(accessBp);
		styleService.delete(generateId());
		coat = shirt = trousers = accessory = -1;
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

	// show Galleries
	private void showGalleryOne() {
		List<Item> items;
		if (!isWeatherOpen) {
			items = itemService.getDataByType(0);
		} else {
			items = itemService.getDataByTypeAndWeather(0, weatherValue);
		}
		adapter = new GalleryAdapter(this, items, R.layout.gallery_item,
				filePath);
		gallery1.setAdapter(adapter);
	}

	private void showGalleryTwo() {
		List<Item> items;
		if (!isWeatherOpen) {
			items = itemService.getDataByType(1);
		} else {
			items = itemService.getDataByTypeAndWeather(1, weatherValue);
		}
		adapter = new GalleryAdapter(this, items, R.layout.gallery_item,
				filePath);
		gallery2.setAdapter(adapter);
	}

	private void showGalleryThree() {
		List<Item> items;
		if (!isWeatherOpen) {
			items = itemService.getDataByType(2);
		} else {
			items = itemService.getDataByTypeAndWeather(2, weatherValue);
		}
		adapter = new GalleryAdapter(this, items, R.layout.gallery_item,
				filePath);
		gallery3.setAdapter(adapter);
	}

	private void showGalleryFour() {
		List<Item> items;
		if (!isWeatherOpen) {
			items = itemService.getDataByType(3);
		} else {
			items = itemService.getDataByTypeAndWeather(3, weatherValue);
		}
		adapter = new GalleryAdapter(this, items, R.layout.gallery_item,
				filePath);
		gallery4.setAdapter(adapter);
	}

	// <-------------- used for weather --------------------->
	/*
	 * I reference the source code from:
	 * http://android-er.blogspot.com/2012/03/get
	 * -weather-info-from-yahoo-weather-rss.html
	 */
	private class WeatherTask implements Runnable {
		@Override
		public void run() {
			try {
				String weatherString = QueryYahooWeather(zipcode);
				Document weatherDoc = convertStringToDocument(weatherString);

				parseWeather(weatherDoc);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						weatherText.setText(myWeather.toString());
						temp = Integer.parseInt(myWeather.getTemp());
						switchBtn.setVisibility(ViewGroup.VISIBLE);
					}
				});
			} catch (Exception e) {
				temp = -9999;
				e.printStackTrace();
			}
		}
	}

	// parse weather from srcDoc
	private void parseWeather(Document srcDoc) {
		// <yweather:location.../>
		Node locationNode = srcDoc.getElementsByTagName("yweather:location")
				.item(0);
		myWeather.setCity(locationNode.getAttributes().getNamedItem("city")
				.getNodeValue().toString());
		myWeather.setRegion(locationNode.getAttributes().getNamedItem("region")
				.getNodeValue().toString());
		myWeather.setCountry(locationNode.getAttributes()
				.getNamedItem("country").getNodeValue().toString());

		// <yweather:condition.../>
		Node conditionNode = srcDoc.getElementsByTagName("yweather:condition")
				.item(0);
		myWeather.setConditiontext(conditionNode.getAttributes()
				.getNamedItem("text").getNodeValue().toString());
		myWeather.setConditiondate(conditionNode.getAttributes()
				.getNamedItem("date").getNodeValue().toString());

		myWeather.setTemp(conditionNode.getAttributes().getNamedItem("temp")
				.getNodeValue().toString());
	}

	private Document convertStringToDocument(String src) {
		Document dest = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			Toast.makeText(this, e1.toString(), Toast.LENGTH_LONG).show();
		} catch (SAXException e) {
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		return dest;
	}

	// query yahoo weather by zip
	private String QueryYahooWeather(int zip) {

		String qResult = "";
		String queryString = "";
		if (zip < 10000)
			queryString = "http://weather.yahooapis.com/forecastrss?p=0" + zip
					+ "&u=c";
		else
			queryString = "http://weather.yahooapis.com/forecastrss?p=" + zip
					+ "&u=c";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(queryString);
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
		return qResult;
	}

	// called when click the "Go" button
	public void clickGetWeather(View view) {
		EditText ziptext = (EditText) findViewById(R.id.zipcodeText);
		// Hide soft keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ziptext.getWindowToken(), 0);
		String zipstr = ziptext.getText().toString();
		try {
			if (zipstr.charAt(0) == '0')
				zipstr = zipstr.substring(1, zipstr.length());
			zipcode = Integer.parseInt(zipstr);
			Thread weatherThread = new Thread(new WeatherTask());
			weatherThread.start();
		} catch (Exception e) {
			Toast.makeText(this,
					"Invalid zip code or other issues, please try again.",
					Toast.LENGTH_SHORT).show();
		}
	}

	// called when click "ON/OFF" button
	public void clickOpen(View view) {
		isWeatherOpen = !isWeatherOpen;
		if (isWeatherOpen) {
			switchBtn.setText("OFF");
			if (temp != -9999) {
				if (temp >= 20)
					weatherValue = 0; // hot
				else if (temp >= 10)
					weatherValue = 1; // warm
				else
					// temperature < 10C
					weatherValue = 2; // cold
				Toast.makeText(this, "Start weather function",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Something wrong with weather",
						Toast.LENGTH_SHORT).show();
				isWeatherOpen = false;
				switchBtn.setText("ON");
				switchBtn.setVisibility(ViewGroup.INVISIBLE);
			}
		} else { // turn off weather
			switchBtn.setText("ON");
			Toast.makeText(this, "Switch off weather function",
					Toast.LENGTH_SHORT).show();
		}
	}

	// called when click the "Go Back" button in weather layout
	public void goBackFromWeather(View view) {
		mainLayout.setVisibility(ViewGroup.VISIBLE);
		weatherLayout.setVisibility(ViewGroup.INVISIBLE);
		showGalleryOne();
		showGalleryTwo();
		showGalleryThree();
		showGalleryFour();
	}
}
