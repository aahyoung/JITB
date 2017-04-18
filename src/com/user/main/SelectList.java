package com.user.main;

import java.util.HashMap;

public class SelectList {
	int movie_id;
	int screening_date_id;
	int start_time_id;
	int theater_operate_id;
	int theater_id;
	int seat_id;
	//타입 아이디, 인원수
	HashMap<Integer, Integer> type;
	//타입 아이디, 가격
	HashMap<Integer, Integer> price;
	
	public int getMovie_id() {
		return movie_id;
	}
	
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}
	
	public int getScreening_date_id() {
		return screening_date_id;
	}
	
	public void setScreening_date_id(int screening_date_id) {
		this.screening_date_id = screening_date_id;
	}
	
	public int getStart_time_id() {
		return start_time_id;
	}
	
	public void setStart_time_id(int start_time_id) {
		this.start_time_id = start_time_id;
	}
	
	public int getTheater_operate_id() {
		return theater_operate_id;
	}
	
	public void setTheater_operate_id(int theater_operate_id) {
		this.theater_operate_id = theater_operate_id;
	}
	
	public int getTheater_id() {
		return theater_id;
	}
	
	public void setTheater_id(int theater_id) {
		this.theater_id = theater_id;
	}
	
	public int getSeat_id() {
		return seat_id;
	}
	
	public void setSeat_id(int seat_id) {
		this.seat_id = seat_id;
	}

	public HashMap<Integer, Integer> getType() {
		return type;
	}

	public void setType(HashMap<Integer, Integer> type) {
		this.type = type;
	}

	public HashMap<Integer, Integer> getPrice() {
		return price;
	}

	public void setPrice(HashMap<Integer, Integer> price) {
		this.price = price;
	}
	
	
}
