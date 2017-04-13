package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Theater extends JPanel{
	JLabel lb_name, lb_number, lb_movie;
	int theater_id;
	
	public Theater() {
		lb_name=new JLabel();
		lb_number=new JLabel();
		lb_movie=new JLabel();
		
		add(lb_name);
		add(lb_number);
		add(lb_movie);
		
		setPreferredSize(new Dimension(50, 80));
		setBackground(Color.pink);
	}
}
