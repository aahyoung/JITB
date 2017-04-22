package com.user.main.purchase.advance;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.user.main.purchase.ticket.MovieTime;
import com.user.main.purchase.ticket.MovieTime;

public class MovieCard extends Rectangle{
	int movie_id;
	String poster;
	String movie_name;
	ArrayList<MovieTime> cars;
	
	public MovieCard(int movie_id, String poster, String movie_name, ArrayList<MovieTime> cars) {
		this.movie_id = movie_id;
		this.poster = poster;
		this.movie_name = movie_name;
		this.cars = cars;
	}
	
	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getMovie_name() {
		return movie_name;
	}

	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}

	public ArrayList<MovieTime> getCars() {
		return cars;
	}

	public void setCars(ArrayList<MovieTime> cars) {
		this.cars = cars;
	}
	
}
