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

public class TopOption extends JPanel{
	ComboList comboList;
	
	Canvas opt_img;
	ArrayList<JLabel> la_tags = new ArrayList<JLabel>();
	int[] selectedId;
	boolean[] isSelectBuffr;
	
	int plus_price;
	
	public TopOption(ComboList comboList) {
		this.comboList = comboList;
		
		opt_img = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = null;
				try {
					url = new URL("http://localhost:8989/image/snack/"+comboList.getTop_opt_img());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				//URL url = getClass().getResource("/"+comboList.getTop_opt_img());
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 150, 180, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		opt_img.setPreferredSize(new Dimension(150, 180));
		add(opt_img);
		
		for(int j=0; j<comboList.getAmount(); j++){
			JLabel opt = new JLabel(comboList.getTop_opt_name()+"("+comboList.getSize()+")", JLabel.CENTER);
			opt.setFont(new Font("Malgun Gothic", Font.PLAIN, 17));
			opt.setForeground(Color.WHITE);
			opt.setPreferredSize(new Dimension(150, 17));
			la_tags.add(opt);
			add(opt);
		}
		selectedId = new int[la_tags.size()];
		isSelectBuffr = new boolean[la_tags.size()];
		
		setPreferredSize(new Dimension(150, 250));
		setBackground(new Color(33,33,33));
	}
}
