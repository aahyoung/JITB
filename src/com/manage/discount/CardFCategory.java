package com.manage.discount;

public class CardFCategory {
	private int card_id;
	private String name;
	private double rate;
	private String img;
	private int discount_type_id;
	
	public double getRate() {
		return rate;
	}
	public int getCard_id() {
		return card_id;
	}
	public String getImg() {
		return img;
	}
	public String getName() {
		return name;
	}
	public int getDiscount_type_id() {
		return discount_type_id;
	}
	
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public void setDiscount_type_id(int discount_type_id) {
		this.discount_type_id = discount_type_id;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
}
