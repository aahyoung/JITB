package com.user.main.purchase.ticket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NumButton extends JPanel{
	JLabel label;
	
	int index;
	int type_id;
	int price;
	
	boolean editable = true;
	boolean flag = false;
	
	public NumButton(int index, int type_id, int price) {
		this.index = index;
		this.type_id = type_id;
		this.price = price;
		
		label = new JLabel(Integer.toString(index));
		label.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
		if(index != 0){
			label.setForeground(Color.WHITE);
		}else{
			label.setForeground(Color.YELLOW);
		}
		
		add(label);
		setBackground(new Color(33,33,33));
		setPreferredSize(new Dimension(50, 50));

	}
}
