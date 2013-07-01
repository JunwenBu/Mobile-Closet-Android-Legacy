/* CIS 600 Final Project - Pocket Closet
 * Version: 1.0
 * Author: Junwen Bu
 * April 2013 - May 2013
 * */
package com.junwenbu.data;

// entity class of table style
public class Style {
	private Integer id;
	private Integer type1; // coat
	private Integer type2; // shirt
	private Integer type3; // trousers
	private Integer type4; // accessory

	// constructor
	public Style(Integer id, Integer type1, Integer type2, Integer type3,
			Integer type4) {
		this.id = id;
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.type4 = type4;
	}

	// getters and setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType1() {
		return type1;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	}

	public Integer getType2() {
		return type2;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}

	public Integer getType3() {
		return type3;
	}

	public void setType3(Integer type3) {
		this.type3 = type3;
	}

	public Integer getType4() {
		return type4;
	}

	public void setType4(Integer type4) {
		this.type4 = type4;
	}
}
