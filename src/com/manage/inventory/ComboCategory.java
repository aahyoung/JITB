package com.manage.inventory;

public class ComboCategory {
	private int combo_id;
	private int top_opt_id;
	private String name;
	private String img;
	private int price;

		public int getCombo_id() {
			return combo_id;
		}
		public int getTop_opt_id() {
			return top_opt_id;
		}
		public String getName() {
			return name;
		}
		public String getImg() {
			return img;
		}
		public int getPrice() {
			return price;
		}
		
		public void setCombo_id(int combo_id) {
			this.combo_id = combo_id;
		}
		public void setTop_opt_id(int top_opt_id) {
			this.top_opt_id = top_opt_id;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public void setPrice(int price) {
			this.price = price;
		}
}
