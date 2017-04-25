package com.user.main.purchase;

import java.awt.Rectangle;

public class PointCard extends Rectangle{
	int point_id;
	String name;
	String img;
	int discount_type_id;
	
	public int getPoint_id() {
		return point_id;
	}
	
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
