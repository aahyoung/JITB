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
import java.net.URL;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class MenuChoiceScreen extends ScreenFrame implements Runnable{
	JPanel p_date;
	JPanel p_time;
	JLabel la_date;
	JLabel la_time;
	JPanel ticket_print;
	JPanel ticket_purchase;
	JPanel combo_purchase;
	Canvas poster;
	
	String[] days = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
	
	Calendar cal;
	int mm, dd, h, m, day;
	String ap;
	Thread thread;
	
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
		poster = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/kingsman.png");
				Image img;
				try {
					img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 800, 1200, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread = new Thread(this);
		thread.start();
		
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
	
	public void setTime(){
		cal = Calendar.getInstance();
		String str_h;
		String str_m;
		
		mm = cal.get(Calendar.MONTH);
		dd = cal.get(Calendar.DATE);
		h = cal.get(Calendar.HOUR);
		m = cal.get(Calendar.MINUTE);
		day = cal.get(Calendar.DAY_OF_WEEK);
		
		if(cal.get(Calendar.AM_PM)==Calendar.AM){
			ap = "am";
		}else{
			ap = "pm";
		}
		
		if(h<10){
			str_h = "0"+Integer.toString(h);
		}else{
			str_h = Integer.toString(h);
		}
		if(m<10){
			str_m = "0"+Integer.toString(m);
		}else{
			str_m = Integer.toString(m);
		}
		
		String date = mm+"월 "+dd+"일 "+days[day-1];
		String time = str_h+":"+str_m+ap;
		
		la_date.setText(date);
		la_time.setText(time);
	}
	
	@Override
	public void run() {
		while(true){
			setTime();
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
