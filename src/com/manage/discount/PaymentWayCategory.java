package com.manage.discount;

public class PaymentWayCategory {
	private int payment_way_id;
	private String payment_way;
	private String img;
	
	public String getPayment_way() {
		return payment_way;
	}
	public int getPayment_way_id() {
		return payment_way_id;
	}
	public String getImg() {
		return img;
	}
	public void setPayment_way_id(int payment_way_id) {
		this.payment_way_id = payment_way_id;
	}
	public void setPayment_way(String payment_way) {
		this.payment_way = payment_way;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
