package com.manage.discountFinal;

public class PointFCategory {
	private int point_id;
	private String name;
	private String img;
	private int discount_type_id;
	
	public int getPoint_id() {
		return point_id;
	}
	public String getName() {
		return name;
	}
	public String getImg() {
		return img;
	}
	public int getDiscount_type_id() {
		return discount_type_id;
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
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
}
