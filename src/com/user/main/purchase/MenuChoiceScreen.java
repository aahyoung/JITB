package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class MenuChoiceScreen extends ScreenFrame {
	JPanel p_date;
	JPanel p_time;
	public JLabel la_date;
	public JLabel la_time;
	JPanel ticket_print;
	JPanel ticket_purchase;
	JPanel combo_purchase;
	public Canvas poster;
	
	public URL poster_url;
	
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
		
		try {
			poster_url = new URL("http://localhost:8989/image/movie/kingsman.png");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//poster_url = getClass().getResource("/kingsman.png");
		
		poster = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Image img;
				try {
					img = ImageIO.read(poster_url);
					g.drawImage(img, 0, 0, 800, 1200, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		p_date.setPreferredSize(new Dimension(750, 65));
		p_time.setPreferredSize(new Dimension(750, 135));
		ticket_print.setPreferredSize(new Dimension(250, 350));
		ticket_purchase.setPreferredSize(new Dimension(250, 350));
		combo_purchase.setPreferredSize(new Dimension(250, 350));
		poster.setPreferredSize(new Dimension(750, 400));
		
		la_date.setForeground(Color.WHITE);
		la_time.setForeground(Color.WHITE);
		la_date.setFont(new Font("Malgun Gothic", Font.PLAIN, 50));
		la_time.setFont(new Font("Malgun Gothic", Font.PLAIN, 80));
		
		p_date.setBackground(new Color(33,33,33));
		p_time.setBackground(new Color(33,33,33));
		
		p_date.add(la_date);
		p_time.add(la_time);
		add(p_date);
		add(p_time);
		add(ticket_print);
		add(ticket_purchase);
		add(combo_purchase);
		add(poster);
		
		ticket_print.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.setPage(2);
			}
		});
		
		ticket_purchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.setPage(4);
			}
		});
		
		combo_purchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.setPage(7);
			}
		});
	}
}
