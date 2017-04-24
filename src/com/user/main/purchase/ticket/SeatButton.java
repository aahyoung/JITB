package com.user.main.purchase.ticket;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SeatButton extends JPanel{
	JLabel la_name;
	
	String seat_name;
	int status;
	
	int index;
	boolean flag = false;
	
	public SeatButton(String seat_name, int status) {
		this.seat_name = seat_name;
		this.status = status;
		
		index = status;
		
		la_name = new JLabel();
		statusColor();
		add(la_name);
	}
	
	public void statusColor(){
		if(index==1){
			la_name.setText(seat_name);
			la_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
			la_name.setForeground(Color.LIGHT_GRAY);
			setBackground(Color.DARK_GRAY);
		}else if(index==0){
			la_name.setText(seat_name);
			la_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
			if(status == 0){
				la_name.setForeground(Color.WHITE);
				setBackground(Color.LIGHT_GRAY);
			}else if(status == 1){
				la_name.setForeground(new Color(255, 90, 90));
				setBackground(new Color(147, 0, 0));
			}
		}else if(index==-1){
			la_name.setText("");
			setBackground(new Color(33,33,33));
		}
	}
}
