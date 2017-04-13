package com.manage.discount;

public class Discount_infoCategory {
	private int discount_info_id;
	private int discount_type_id;
	private String name;
	private int rate;
	private String img;
	
	public int getSub_opt_id() {
		return discount_info_id;
	}
	public int getdiscount_type_id() {
		return discount_type_id;
	}
	public String getName() {
		return name;
	}
	public int getrate() {
		return rate;
	}
	public String getImg() {
		return img;
	}

	public void setdiscount_info_id(int discount_info_id) {
		this.discount_info_id = discount_info_id;
	}
	public void setdiscount_type_id(int discount_type_id) {
		this.discount_type_id = discount_type_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setrate(int rate) {
		this.rate = rate;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
