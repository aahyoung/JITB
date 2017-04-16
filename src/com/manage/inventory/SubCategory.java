package com.manage.inventory;

public class SubCategory {
	private int sub_opt_id;
	private int top_opt_size_id;
	private String name;
	private int price;
	private String img;
	private String stock;
	
	public int getSub_opt_id() {
		return sub_opt_id;
	}
	public int getTop_opt_size_id() {
		return top_opt_size_id;
	}
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public String getImg() {
		return img;
	}
	public String getStock() {
		return stock;
	}
	public void setSub_opt_id(int sub_opt_id) {
		this.sub_opt_id = sub_opt_id;
	}
	public void setTop_opt_size_id(int Top_opt_size_id) {
		this.top_opt_size_id = Top_opt_size_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
}
