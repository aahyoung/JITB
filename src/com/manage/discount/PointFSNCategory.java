package com.manage.discount;

public class PointFSNCategory {
	private int point_serial_id;
	private long serial_number;
	private int point;
	private int point_id;
	
	public int getPoint_serial_id() {
		return point_serial_id;
	}
	public long getSerial_number() {
		return serial_number;
	}
	public int getPoint() {
		return point;
	}
	public int getPoint_id() {
		return point_id;
	}
	
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
	public void setSerial_number(long serial_number) {
		this.serial_number = serial_number;
	}
	public void setPoint_serial_id(int point_serial_id) {
		this.point_serial_id = point_serial_id;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}
