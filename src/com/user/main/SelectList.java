package com.user.main;

import java.util.HashMap;

public class SelectList {
	int product_id;
	int type_id;
	String seat_name;
	HashMap type_price;
	int payment_way_id;
	int discount_type_id;
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getSeat_name() {
		return seat_name;
	}
	public void setSeat_name(String seat_name) {
		this.seat_name = seat_name;
	}
	public HashMap getType_price() {
		return type_price;
	}
	public void setType_price(HashMap type_price) {
		this.type_price = type_price;
	}
	public int getPayment_way_id() {
		return payment_way_id;
	}
	public void setPayment_way_id(int payment_way_id) {
		this.payment_way_id = payment_way_id;
	}
	public int getDiscount_type_id() {
		return discount_type_id;
	}
	public void setDiscount_type_id(int discount_type_id) {
		this.discount_type_id = discount_type_id;
	}
	
}
