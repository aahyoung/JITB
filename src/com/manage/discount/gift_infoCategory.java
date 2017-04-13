package com.manage.discount;

public class gift_infoCategory {
	private int gift_info;
	private int gift_type_id;
	private String no;
	private int price;
	private int status;
	private String img;
	
	public int getgift_info() {
		return gift_info;
	}
	public int gift_type_id() {
		return gift_type_id;
	}
	public String getno() {
		return no;
	}
	public int getPrice() {
		return price;
	}
	public String getImg() {
		return img;
	}
	public int getstatus() {
		return status;
	}
	
	public void setgift_info(int gift_info) {
		this.gift_info = gift_info;
	}
	public void setgift_type_id(int gift_type_id) {
		this.gift_type_id = gift_type_id;
	}
	public void setno(String no) {
		this.no = no;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setstatus(int status) {
		this.status = status;
	}
}
