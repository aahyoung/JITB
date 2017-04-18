package com.jitb.db;

public class Seat {
	int seat_id;
	int theater_operate_id;
	String name;
	int status;
	
	public int getSeat_id() {
		return seat_id;
	}
	
	public void setSeat_id(int seat_id) {
		this.seat_id = seat_id;
	}
	
	public int getTheater_operate_id() {
		return theater_operate_id;
	}
	
	public void setTheater_operate_id(int theater_operate_id) {
		this.theater_operate_id = theater_operate_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
