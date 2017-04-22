package com.user.main.purchase.ticket;

import java.awt.Rectangle;

public class MovieTime extends Rectangle{
	int product_id;
	int theater_id;
	String start_time;
	String theater_name;
	int remaining_seat;
	int total_seat;
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getTheater_id() {
		return theater_id;
	}
	public void setTheater_id(int theater_id) {
		this.theater_id = theater_id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getTheater_name() {
		return theater_name;
	}
	public void setTheater_name(String theater_name) {
		this.theater_name = theater_name;
	}
	public int getRemaining_seat() {
		return remaining_seat;
	}
	public void setRemaining_seat(int remaining_seat) {
		this.remaining_seat = remaining_seat;
	}
	public int getTotal_seat() {
		return total_seat;
	}
	public void setTotal_seat(int total_seat) {
		this.total_seat = total_seat;
	}
	
	
}
