package com.user.main.purchase;

import java.awt.Rectangle;

public class NoDiscountPayment extends Rectangle{
	int payment_way_id;
	String payment_way;
	String img;
	
	public int getPayment_way_id() {
		return payment_way_id;
	}
	
	public void setPayment_way_id(int payment_way_id) {
		this.payment_way_id = payment_way_id;
	}
	
	public String getPayment_way() {
		return payment_way;
	}
	
	public void setPayment_way(String payment_way) {
		this.payment_way = payment_way;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
}
