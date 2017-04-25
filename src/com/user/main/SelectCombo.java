package com.user.main;

import java.util.ArrayList;

public class SelectCombo {
	int combo_id;
	ArrayList<Integer> sub_opt_id;
	int payment_way_id;
	int discount_type_id;
	int price;
	
	public int getCombo_id() {
		return combo_id;
	}
	
	public void setCombo_id(int combo_id) {
		this.combo_id = combo_id;
	}
	
	public ArrayList<Integer> getSub_opt_id() {
		return sub_opt_id;
	}
	
	public void setSub_opt_id(ArrayList<Integer> sub_opt_id) {
		this.sub_opt_id = sub_opt_id;
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

	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
}
