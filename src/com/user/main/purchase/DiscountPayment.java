package com.user.main.purchase;

import java.awt.Rectangle;

public class DiscountPayment extends Rectangle{
	int card_id;
	String name;
	double rate;
	String img;
	int discount_type_id;
	int payment_way_id;
	
	public int getCard_id() {
		return card_id;
	}
	
	public void setCard_id(int card_id) {
		this.card_id = card_id;
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
	
	public int getPayment_way_id() {
		return payment_way_id;
	}
	
	public void setPayment_way_id(int payment_way_id) {
		this.payment_way_id = payment_way_id;
	}
}
