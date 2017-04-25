package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * �� ��ȭ�� �г�
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
		
		lb_name.setText(name+"��");
		lb_count.setText("�� �¼��� : "+count);
		//lb_count.setText("���� ��ȭ���� �� �¼����� "+count+" �¼� �Դϴ�.");	
		
		add(lb_name);
		add(lb_count);
		
		setPreferredSize(new Dimension(100, 50));
		setBackground(Color.orange);
	}

}
