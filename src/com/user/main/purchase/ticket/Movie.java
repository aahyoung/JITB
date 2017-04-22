package com.user.main.purchase.ticket;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Movie extends Rectangle{
	String movie_name;
	String poster;
	ArrayList<MovieTime> timeCard = new ArrayList<MovieTime>();
	
	public String getMovie_name() {
		return movie_name;
	}
	
	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public ArrayList<MovieTime> getTimeCard() {
		return timeCard;
	}
	
	public void setTimeCard(ArrayList<MovieTime> timeCard) {
		this.timeCard = timeCard;
	}
}
