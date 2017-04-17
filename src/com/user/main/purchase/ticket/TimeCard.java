package com.user.main.purchase.ticket;

import java.awt.Rectangle;

public class TimeCard extends Rectangle{
	int id;
	String time;
	String remaining_seat;
	String total_seat;
	
	public TimeCard(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemaining_seat() {
		return remaining_seat;
	}

	public void setRemaining_seat(String remaining_seat) {
		this.remaining_seat = remaining_seat;
	}

	public String getTotal_seat() {
		return total_seat;
	}

	public void setTotal_seat(String total_seat) {
		this.total_seat = total_seat;
	}
}
