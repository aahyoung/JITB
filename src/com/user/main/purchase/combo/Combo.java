package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Combo extends JPanel {
	int combo_id;
	String name;
	int price;
	String img;
	ArrayList<ComboList> comboLists;
	
	JLabel combo_name;
	JLabel combo_price;
	Canvas combo_img;
	JLabel combo_list;
	
	public Combo(int combo_id, String name, int price, String img) {
		this.combo_id = combo_id;
		this.name = name;
		this.price = price;
		this.img = img;

		combo_name = new JLabel(name, JLabel.CENTER);
		combo_price = new JLabel(Integer.toString(price), JLabel.CENTER);
		combo_img = new Canvas() {
			@Override
			public void paint(Graphics g) {
				try {
					URL url = null;
					try {
						url = new URL("http://localhost:9090/image/snack/"+img);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					//URL url = this.getClass().getResource("/" + img);
					Image image = ImageIO.read(url);
					g.drawImage(image, 0, 0, 200, 250, this);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		combo_list = new JLabel("", JLabel.CENTER);

		combo_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
		combo_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		combo_list.setFont(new Font("Malgun Gothic", Font.PLAIN, 17));
		combo_name.setForeground(Color.WHITE);
		combo_price.setForeground(Color.WHITE);
		combo_list.setForeground(Color.WHITE);

		combo_name.setPreferredSize(new Dimension(240, 35));
		combo_price.setPreferredSize(new Dimension(240, 35));
		combo_img.setPreferredSize(new Dimension(200, 250));
		combo_list.setPreferredSize(new Dimension(240, 25));
		
		combo_name.setText(name);
		combo_price.setText(Integer.toString(price)+"¿ø");
		
		setPreferredSize(new Dimension(250, 400));
		setBackground(new Color(33, 33, 33));
		
		add(combo_name);
		add(combo_price);
		add(combo_img);
		add(combo_list);
	}
	
	public void setComboList(ArrayList<ComboList> comboLists){
		this.comboLists = comboLists;
		StringBuffer text = new StringBuffer();
		
		for(int i=0; i<comboLists.size(); i++){
			ComboList comboList = comboLists.get(i);
			text.append(comboList.getTop_opt_name()+"("+comboList.getSize()+")"+comboList.getAmount());
			
			if(i < comboLists.size()-1){
				text.append("+");
			}
			
			combo_list.setText(text.toString());
		}
	}
}
