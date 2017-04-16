package com.jitb.db;

public class MovieRate {
	int movie_rate_id;
	int movie_id;
	double movie_rate;
	
	public int getMovie_rate_id() {
		return movie_rate_id;
	}

	public void setMovie_rate_id(int movie_rate_id) {
		this.movie_rate_id = movie_rate_id;
	}

	public int getMovie_id() {
		return movie_id;
	}
	
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public double getMovie_rate() {
		return movie_rate;
	}

	public void setMovie_rate(double movie_rate) {
		this.movie_rate = movie_rate;
	}
}
