/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.pocketcloset;

// hold information get from Yahoo_weather
public class Weather {
	private String city;
	private String region;
	private String country;
	private String conditiontext;
	private String conditiondate;
	private String temp;

	// toString()
	public String toString() {
		int ctemp = Integer.parseInt(temp);
		int ftemp = ctemp * 9 / 5 + 32;
		return "\nCity: " + city + "，" + region + "， " + country + "\n\n"
				+ "Temperature: " + "C" + temp + " /" + " F" + ftemp
				+ "\nWeather: " + conditiontext + "\n" + conditiondate + "\n";
	}

	// setters and getters
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getConditiontext() {
		return conditiontext;
	}

	public void setConditiontext(String conditiontext) {
		this.conditiontext = conditiontext;
	}

	public String getConditiondate() {
		return conditiondate;
	}

	public void setConditiondate(String conditiondate) {
		this.conditiondate = conditiondate;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

}
