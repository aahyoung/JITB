package com.user.main;

public class OrderInfo {
	int type_id;
	String type;
	int type_price;
	String[] seatName; //°¹¼ö°¡ °ð ÀÎ¿ø¼ö
	
	public int getType_id() {
		return type_id;
	}
	
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getType_price() {
		return type_price;
	}
	
	public void setType_price(int type_price) {
		this.type_price = type_price;
	}
	
	public String[] getSeatName() {
		return seatName;
	}
	
	public void setSeatName(String[] seatName) {
		this.seatName = seatName;
	}
}