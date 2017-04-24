package com.user.main.purchase;

import java.awt.Rectangle;
import java.util.HashMap;

public class ChoiceCombo extends Rectangle{
	String combo_name;
	String combo_img;
	HashMap<String, Integer> amount;
	
	public String getCombo_name() {
		return combo_name;
	}
	
	public void setCombo_name(String combo_name) {
		this.combo_name = combo_name;
	}
	
	public String getCombo_img() {
		return combo_img;
	}
	
	public void setCombo_img(String combo_img) {
		this.combo_img = combo_img;
	}
	
	public HashMap<String, Integer> getAmount() {
		return amount;
	}
	
	public void setAmount(HashMap<String, Integer> amount) {
		this.amount = amount;
	}
	
}
