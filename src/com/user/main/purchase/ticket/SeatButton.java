package com.user.main.purchase.ticket;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SeatButton extends JPanel{
	JLabel la_name;
	
	int seat_id;
	String seat_name;
	int status;
	
	boolean flag = false;
	
	public SeatButton(int seat_id, String seat_name, int status) {
		this.seat_id = seat_id;
		this.seat_name = seat_name;
		this.status = status;
		
		la_name = new JLabel();
		statusColor();
		add(la_name);
	}
	
	public void statusColor(){
		if(status==1){
			la_name.setText(seat_name);
			la_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
			la_name.setForeground(Color.LIGHT_GRAY);
			setBackground(Color.DARK_GRAY);
		}else if(status==0){
			la_name.setText(seat_name);
			la_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
			la_name.setForeground(Color.WHITE);
			setBackground(Color.LIGHT_GRAY);
		}else if(status==-1){
			la_name.setText("");
			setBackground(new Color(33,33,33));
		}
	}
}
