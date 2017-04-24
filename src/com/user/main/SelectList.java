package com.user.main;

public class SelectList {
	int product_id;
	OrderInfo[] orderInfos;
	int payment_way_id;
	int discount_type_id;
	int price;
	
	public int getProduct_id() {
		return product_id;
	}
	
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	public OrderInfo[] getOrderInfos() {
		return orderInfos;
	}

	public void setOrderInfos(OrderInfo[] orderInfos) {
		this.orderInfos = orderInfos;
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
