package com.manage.discount;

public class gift_typeCategory {
	private int gift_type_id;
	private String name;
	private String img;
		public int getgift_type_id () {
			return gift_type_id ;
		}
		public String getName() {
			return name;
		}
		public String getImg() {
			return img;
		}
		
		public void setImg(String img) {
			this.img = img;
		}
		public void setgift_type_id (int gift_type_id ) {
			this.gift_type_id  = gift_type_id ;
		}
		public void setName(String name) {
			this.name = name;
		}
}
