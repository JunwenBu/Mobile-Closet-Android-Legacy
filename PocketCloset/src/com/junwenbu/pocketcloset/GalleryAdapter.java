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

// Adapter for Gallery 
public class GalleryAdapter extends BaseAdapter {

	private List<Item> items;
	private int resource; // binded resource
	private LayoutInflater inflater;
	private String pathName;

	public GalleryAdapter(Context context, List<Item> items, int resource,
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

	private final class ViewCache {
		ImageView itemIV;
	}

	// to get better performance
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView itemIV = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
			// get View
			itemIV = (ImageView) convertView.findViewById(R.id.galleryItem);

			ViewCache cache = new ViewCache();
			cache.itemIV = itemIV;
			// temporary store cache
			convertView.setTag(cache);
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			itemIV = cache.itemIV;
		}

		// realize the data binding
		Item item = items.get(position);
		itemIV.setImageBitmap(BitmapFactory.decodeFile(pathName + "/"
				+ item.getFile()));

		return convertView;
	}

}
