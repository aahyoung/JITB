package com.manage.movie;

/*
 * Theater 테이블 저장 DTO
 * */

public class Theater {
	private int theater_id;
	private int branch_id;
	private String name;
	private int row_line;
	private int column_line;
	
	public int getTheater_id() {
		return theater_id;
	}
	public void setTheater_id(int theater_id) {
		this.theater_id = theater_id;
	}
	public int getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRow_line() {
		return row_line;
	}
	public void setRow_line(int row_line) {
		this.row_line = row_line;
	}
	public int getColumn_line() {
		return column_line;
	}
	public void setColumn_line(int column_line) {
		this.column_line = column_line;
	}

}
