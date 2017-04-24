package com.user.main.purchase;

import java.awt.Rectangle;

public class ChoiceTicket extends Rectangle{
	String movie;
	String branch;
	String theater;
	String time;
	String poster;
	String persons;
	
	public String getMovie() {
		return movie;
	}
	
	public void setMovie(String movie) {
		this.movie = movie;
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
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getPersons() {
		return persons;
	}

	public void setPersons(String persons) {
		this.persons = persons;
	}
	
}
