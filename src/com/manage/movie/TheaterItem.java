package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * 각 영화관 패널
 * */

public class TheaterItem extends JPanel{
	JLabel lb_name, lb_number, lb_movie;
	int theater_id;
	
	String name;
	int row, col;
	
	public TheaterItem(String name, int row, int col) {
		this.name=name;
		this.row=row;
		this.col=col;

		lb_name=new JLabel();
		lb_number=new JLabel();
		lb_movie=new JLabel();
		
		lb_name.setText(name);
		lb_number.setText(Integer.toString(row)+" 행 "+Integer.toString(col)+" 열");	
		
		add(lb_name);
		add(lb_number);
		//add(lb_movie);
		
		setPreferredSize(new Dimension(50, 80));
		setBackground(Color.orange);
	}
}
