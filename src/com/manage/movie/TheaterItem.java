package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * 각 영화관 패널
 * */

public class TheaterItem extends JPanel{
	JLabel lb_name, lb_count;
	
	String name;
	int theater_id;
	int count;
	
	public TheaterItem(String name, int count) {
		this.name=name;
		this.count=count;

		lb_name=new JLabel();
		lb_count=new JLabel();
		
		lb_name.setText(name+"관");
		lb_count.setText("총 좌석수 : "+count);
		//lb_count.setText("현재 영화관의 총 좌석수는 "+count+" 좌석 입니다.");	
		
		add(lb_name);
		add(lb_count);
		
		setPreferredSize(new Dimension(100, 50));
		setBackground(Color.orange);
	}

}
