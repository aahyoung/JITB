/*
buy_movie 데이터 베이스의 레코드 1건을 담기위한 객체 : DTO 설계
*/

package com.manage.sales;

public class BuyMovie {
	
	private int buy_movie_id;
	private int price;
	private int type_id;
	private String type;
	
	private int movie_id;
	private String name;
	private String poster;

	private String screening_date;
	
	private String order_time;
	

	private String start_date;
	private String end_date;


	private int countBuy;


	public int getBuy_movie_id() {
		return buy_movie_id;
	}

	public void setBuy_movie_id(int buy_movie_id) {
		this.buy_movie_id = buy_movie_id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getScreening_date() {
		return screening_date;
	}

	public void setScreening_date(String screening_date) {
		this.screening_date = screening_date;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	
	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	public int getCountBuy() {
		return countBuy;
	}

	public void setCountBuy(int countBuy) {
		this.countBuy = countBuy;
	}


}
