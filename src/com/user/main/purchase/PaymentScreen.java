package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class PaymentScreen extends ScreenFrame{
	JPanel p_price;
	JPanel p_total_price;
	JLabel la_total_info;
	JLabel la_total_price;
	JPanel p_disc_price;
	JLabel la_disc_info;
	JLabel la_disc_price;
	JPanel p_remain_price;
	JLabel la_remain_info;
	JLabel la_remain_price;
	JLabel la_spar1;
	JLabel la_spar2;
	Canvas stepInfo;
	
	ArrayList<JPanel> content = new ArrayList<JPanel>();
	
	String[] path = {
			"white_coupon.png", "white_point.png", "white_purchase.png",
			"yellow_coupon.png", "yellow_point.png", "yellow_purchase.png"};
	URL[] url;
	Image[] img = new Image[3];
	
	public PaymentScreen(ClientMain main) {
		super(main);
		
		p_price = new JPanel();
		p_total_price = new JPanel();
		la_total_info = new JLabel("총 결제금액", JLabel.CENTER);
		la_total_price = new JLabel("0", JLabel.CENTER);
		p_disc_price = new JPanel();
		la_disc_info = new JLabel("할인 금액", JLabel.CENTER);
		la_disc_price = new JLabel("0", JLabel.CENTER);
		p_remain_price = new JPanel();
		la_remain_info = new JLabel("잔여 결제금액", JLabel.CENTER);
		la_remain_price = new JLabel("0", JLabel.CENTER);
		la_spar1 = new JLabel("-", JLabel.CENTER);
		la_spar2 = new JLabel("=", JLabel.CENTER);
		
		//======================쿠폰 쓰기
		//index=0
		content.add(new CouponPanel(main));
		
		//======================포인트 쓰기
		//index=1
		content.add(new PointPanel(main));
		
		//======================구매 + 포인트 적립
		//index=2
		content.add(new PurchasePanel(main));
		
		//======================메시지들
		//index=3
		content.add(new PointInputMessage(main));
		
		//index=4
		content.add(new PointOutputMessage(main));
		
		//index=5
		content.add(new PurchaseEndMessage(main));
		
		//index=6
		content.add(new NoPointMessage(main));
		
		//======================포인트 선택
		//index=7
		content.add(new ApplyPointPanel(main));
		
		setURL();
		setImg(0);
		
		stepInfo = new Canvas(){
			@Override
			public void paint(Graphics g) {
				for(int i=0; i<img.length; i++){
					g.drawImage(img[i], (i*200), 35, 200, 80, this);
				}
			}
		};
		
		la_spar1.setForeground(Color.WHITE);
		la_spar2.setForeground(Color.WHITE);
		la_total_info.setForeground(Color.WHITE);
		la_total_price.setForeground(Color.WHITE);
		la_disc_info.setForeground(Color.WHITE);
		la_disc_price.setForeground(Color.WHITE);
		la_remain_info.setForeground(Color.WHITE);
		la_remain_price.setForeground(Color.YELLOW);
		
		la_spar1.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_spar2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_total_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_total_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_disc_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_disc_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_remain_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_remain_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		
		p_price.setPreferredSize(new Dimension(800, 90));
		p_total_price.setPreferredSize(new Dimension(200, 90));
		la_spar1.setPreferredSize(new Dimension(25, 90));
		p_disc_price.setPreferredSize(new Dimension(200, 90));
		la_spar2.setPreferredSize(new Dimension(25, 90));
		p_remain_price.setPreferredSize(new Dimension(200, 90));
		la_total_info.setPreferredSize(new Dimension(200, 20));
		la_total_price.setPreferredSize(new Dimension(200, 70));
		la_disc_info.setPreferredSize(new Dimension(200, 20));
		la_disc_price.setPreferredSize(new Dimension(200, 70));
		la_remain_info.setPreferredSize(new Dimension(200, 20));
		la_remain_price.setPreferredSize(new Dimension(200, 70));
		stepInfo.setPreferredSize(new Dimension(600, 120));
		
		p_price.setBackground(new Color(33,33,33));
		p_total_price.setBackground(new Color(33,33,33));
		p_disc_price.setBackground(new Color(33,33,33));
		p_remain_price.setBackground(new Color(33,33,33));
		
		p_price.add(p_total_price);
		p_price.add(la_spar1);
		p_price.add(p_disc_price);
		p_price.add(la_spar2);
		p_price.add(p_remain_price);
		p_total_price.add(la_total_info);
		p_total_price.add(la_total_price);
		p_disc_price.add(la_disc_info);
		p_disc_price.add(la_disc_price);
		p_remain_price.add(la_remain_info);
		p_remain_price.add(la_remain_price);
		add(p_price);
		add(stepInfo);
		
		for(int i=0; i<content.size(); i++){
			add(content.get(i));
		}
		
		setPanel(0);
	}
	
	public void setPanel(int index){
		for(int i=0; i<content.size(); i++){
			if(i == index){
				content.get(i).setVisible(true);
			}else{
				content.get(i).setVisible(false);
			}
		}
	}
	
	public void setURL(){
		url = new URL[path.length];
		for(int i=0; i<url.length; i++){
			url[i] = getClass().getResource("/"+path[i]);
		}
	}
	
	public void setImg(int index){
		for(int i=0; i<img.length; i++){
			try {
				if(i == index){
					img[i] = ImageIO.read(url[i+3]);
				}else{
					img[i] = ImageIO.read(url[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
