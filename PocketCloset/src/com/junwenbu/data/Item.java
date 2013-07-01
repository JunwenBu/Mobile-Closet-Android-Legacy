/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.data;

// entity class of clothing items
public class Item {

	private Integer id;
	private String file; // directory of the item photo
	private Integer tag; // 0: not tagged 1: tagged
	private Integer type; // coat, shirt, trousers, ...
	private Integer weather; // hot, warm, cold, rain, ...
	private Integer calendar;
	private Integer style;

	// constructor
	public Item(Integer id, String file, Integer tag, Integer type,
			Integer weather, Integer calendar, Integer style) {
		this.id = id;
		this.file = file;
		this.tag = tag;
		this.type = type;
		this.weather = weather;
		this.calendar = calendar;
		this.style = style;
	}

	// getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getWeather() {
		return weather;
	}

	public void setWeather(Integer weather) {
		this.weather = weather;
	}

	public Integer getCalendar() {
		return calendar;
	}

	public void setCalendar(Integer calendar) {
		this.calendar = calendar;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

}
