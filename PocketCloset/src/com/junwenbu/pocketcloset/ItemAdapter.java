/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

import com.junwenbu.data.*;
import java.util.List;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// Adapter for item ListView
public class ItemAdapter extends BaseAdapter {

	private List<Item> items;
	private int resource; // binded resource
	private LayoutInflater inflater;
	private String pathName;

	public ItemAdapter(Context context, List<Item> items, int resource,
			String pathName) {
		this.items = items;
		this.resource = resource;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pathName = pathName;
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
		ImageView itemIV, weatherIV, typeIV;
		TextView tagTV;
	}

	// to get better performance
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView itemIV = null, weatherIV = null, typeIV = null;
		TextView tagTV = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			// get View
			itemIV = (ImageView) convertView.findViewById(R.id.item);
			weatherIV = (ImageView) convertView.findViewById(R.id.weather);
			typeIV = (ImageView) convertView.findViewById(R.id.type);
			tagTV = (TextView) convertView.findViewById(R.id.tag);

			ViewCache cache = new ViewCache();
			cache.itemIV = itemIV;
			cache.weatherIV = weatherIV;
			cache.typeIV = typeIV;
			cache.tagTV = tagTV;
			// temporary store cache
			convertView.setTag(cache);
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			itemIV = cache.itemIV;
			weatherIV = cache.weatherIV;
			typeIV = cache.typeIV;
			tagTV = cache.tagTV;
		}

		// realize the data binding
		Item item = items.get(position);
		int wNum = item.getWeather();
		int typeNum = item.getType();
		int tagNum = item.getTag();

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

		switch (typeNum) {
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

		tagTV.setText(tagNum == 1 ? "Tagged" : "Not Tagged");
		return convertView;
	}

	// just list this method with worse performance here
	public View getView2(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
		}
		// get View
		ImageView itemIV = (ImageView) convertView.findViewById(R.id.item);
		ImageView weatherIV = (ImageView) convertView
				.findViewById(R.id.weather);
		ImageView typeIV = (ImageView) convertView.findViewById(R.id.type);
		TextView tagTV = (TextView) convertView.findViewById(R.id.tag);

		// realize the data binding
		Item item = items.get(position);
		int wNum = item.getWeather();
		int typeNum = item.getType();
		int tagNum = item.getTag();

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

		switch (typeNum) {
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
			weatherIV.setBackgroundResource(R.drawable.empty);
			break;
		}

		tagTV.setText(tagNum == 1 ? "Tagged" : "Not Tagged");
		return convertView;
	}

}
