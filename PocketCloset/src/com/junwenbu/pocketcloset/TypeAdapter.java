/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import java.util.List;

import com.junwenbu.data.Item;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

// Adapter for ListView displayed in calendar activity
public class TypeAdapter extends BaseAdapter {
	private List<Item> items;
	private int resource; // binded resource
	private LayoutInflater inflater;
	private String pathName;
	private int type;
	private ImageView typeIV;

	public TypeAdapter(Context context, List<Item> items, int resource,
			String pathName, int type) {
		this.items = items;
		this.resource = resource;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pathName = pathName;
		this.type = type;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// define inner class to get better performance
	private final class ViewCache {
		ImageView itemIV, weatherIV;
	}

	// to get better performance
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView itemIV = null, weatherIV = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			// get View
			itemIV = (ImageView) convertView.findViewById(R.id.calendarItem);
			weatherIV = (ImageView) convertView
					.findViewById(R.id.calendarWeather);
			typeIV = (ImageView) convertView.findViewById(R.id.calendarType);

			ViewCache cache = new ViewCache();
			cache.itemIV = itemIV;
			cache.weatherIV = weatherIV;

			switch (type) {
			case -1:
				typeIV.setBackgroundResource(R.drawable.empty);
				break;
			case 0:
				typeIV.setBackgroundResource(R.drawable.coat);
				break;
			case 1:
				typeIV.setBackgroundResource(R.drawable.shirt);
				break;
			case 2:
				typeIV.setBackgroundResource(R.drawable.trousers);
				break;
			case 3:
				typeIV.setBackgroundResource(R.drawable.accessory);
				break;
			default:
				typeIV.setBackgroundResource(R.drawable.empty);
				break;
			}

			// temporary store cache
			convertView.setTag(cache);
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			itemIV = cache.itemIV;
			weatherIV = cache.weatherIV;

		}

		// realize the data binding
		Item item = items.get(position);
		int wNum = item.getWeather();

		itemIV.setImageBitmap(BitmapFactory.decodeFile(pathName + "/"
				+ item.getFile()));

		switch (wNum) {
		case -1:
			weatherIV.setBackgroundResource(R.drawable.empty);
			break;
		case 0:
			weatherIV.setBackgroundResource(R.drawable.hot);
			break;
		case 1:
			weatherIV.setBackgroundResource(R.drawable.warm);
			break;
		case 2:
			weatherIV.setBackgroundResource(R.drawable.cold);
			break;
		case 3:
			weatherIV.setBackgroundResource(R.drawable.rain);
			break;
		default:
			weatherIV.setBackgroundResource(R.drawable.empty);
			break;
		}

		return convertView;
	}

}
