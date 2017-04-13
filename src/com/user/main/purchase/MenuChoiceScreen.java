package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class MenuChoiceScreen extends ScreenFrame{
	JPanel p_date;
	JPanel p_time;
	JLabel la_date;
	JLabel la_time;
	JPanel ticket_print;
	JPanel ticket_purchase;
	JPanel combo_purchase;
	Canvas poster;
	
	public MenuChoiceScreen(ClientMain main) {
		super(main);
		
		p_date = new JPanel();
		p_time = new JPanel();
		la_date = new JLabel("날짜/요일");
		la_time = new JLabel("시간");
		ticket_print = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				URL url = getClass().getResource("/ticket_print_menu.png");
				Image img;
				try {
					img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 250, 350, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		};
		ticket_purchase = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				URL url = getClass().getResource("/ticket_purchase_menu.png");
				Image img;
				try {
					img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 250, 350, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		combo_purchase = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				URL url = getClass().getResource("/combo_menu.png");
				Image img;
				try {
					img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 250, 350, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		poster = new Canvas();
		
		p_date.setPreferredSize(new Dimension(750, 50));
		p_time.setPreferredSize(new Dimension(750, 150));
		ticket_print.setPreferredSize(new Dimension(250, 350));
		ticket_purchase.setPreferredSize(new Dimension(250, 350));
		combo_purchase.setPreferredSize(new Dimension(250, 350));
		poster.setPreferredSize(new Dimension(750, 400));
		
		la_date.setForeground(Color.WHITE);
		la_time.setForeground(Color.WHITE);
		la_date.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_time.setFont(new Font("Malgun Gothic", Font.PLAIN, 50));
		
		p_date.setBackground(Color.DARK_GRAY);
		p_time.setBackground(Color.DARK_GRAY);
		ticket_print.setBackground(Color.ORANGE);
		ticket_purchase.setBackground(Color.GRAY);
		combo_purchase.setBackground(Color.MAGENTA);
		poster.setBackground(Color.YELLOW);
		
		p_date.add(la_date);
		p_time.add(la_time);
		add(p_date);
		add(p_time);
		add(ticket_print);
		add(ticket_purchase);
		add(combo_purchase);
	}
}
