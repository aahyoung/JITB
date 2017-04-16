package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Rectangle;

//예매된 정보들이 출력되는 객체
public class AdvancedTicket extends Rectangle{
	String branch = "지점";
	String theater = "관";
	String movie_name = "영화이름";
	String persons ="타입 및 인원수";
	String movie_time = "영화 날짜 및 시간";
	String poster = "포스터 이미지";
	
	public AdvancedTicket(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getTheater() {
		return theater;
	}

	public void setTheater(String theater) {
		this.theater = theater;
	}

	public String getMovie_name() {
		return movie_name;
	}

	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}

	public String getPersons() {
		return persons;
	}

	public void setPersons(String persons) {
		this.persons = persons;
	}

	public String getMovie_time() {
		return movie_time;
	}

	public void setMovie_time(String movie_time) {
		this.movie_time = movie_time;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

}
