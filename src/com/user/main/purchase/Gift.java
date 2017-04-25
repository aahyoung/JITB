package com.user.main.purchase;

import java.awt.Rectangle;

public class Gift extends Rectangle{
	int gift_id;
	String name;
	double rate;
	String img;
	int discount_type_id;
	
	public int getGift_id() {
		return gift_id;
	}
	
	public void setGift_id(int gift_id) {
		this.gift_id = gift_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getRate() {
		return rate;
	}
	
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public int getDiscount_type_id() {
		return discount_type_id;
	}
	
	public void setDiscount_type_id(int discount_type_id) {
		this.discount_type_id = discount_type_id;
	}
	
}
