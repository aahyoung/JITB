package com.manage.discount;

public class CustomerCategory {
	private int customer_id;
	private String serial_number;
	private String name;
	private int point;
	private int discount_type_id=1;
	
	public int getCustomer_id() {
		return customer_id;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public String getName() {
		return name;
	}
	public int getPoint() {
		return point;
	}
	public int getDiscount_type_id() {
		return discount_type_id;
	}
	
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}
