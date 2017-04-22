package com.user.main.purchase.ticket;

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

public class SeatsChoiceScreen extends ScreenFrame{
	JLabel la_priceInfo;
	JLabel la_price;
	JPanel p_container;
	JPanel p_screenInfo;
	JPanel p_bt;
	
	int rowLine;
	int colLine;
	
	int totalPersons;
	
	ArrayList<SeatButton> selectSeat = new ArrayList<SeatButton>();
	
	String[] locationImg = {
			"A.png", "B.png", "C.png", "D.png", "E.png", "F.png", "G.png", "H.png", "I.png", "J.png"
	};
	
	public SeatsChoiceScreen(ClientMain main) {
		super(main);
		
		la_priceInfo = new JLabel("총 결제금액");
		la_price = new JLabel("0원");
		p_container = new JPanel();
		p_screenInfo = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/screen.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 600, 100, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		p_bt = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_purchase.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		la_priceInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		
		la_priceInfo.setForeground(Color.WHITE);
		la_price.setForeground(Color.WHITE);
		
		p_container.setBackground(new Color(33,33,33));
		
		la_priceInfo.setPreferredSize(new Dimension(700, 25));
		la_price.setPreferredSize(new Dimension(700, 50));
		p_container.setPreferredSize(new Dimension(700, 700));
		p_screenInfo.setPreferredSize(new Dimension(600, 100));
		p_bt.setPreferredSize(new Dimension(200, 50));
		
		p_container.add(p_screenInfo);
		add(la_priceInfo);
		add(la_price);
		add(p_container);
		add(p_bt);
	}
	
//	public void createSeatBtn(){
//		for(int i=0; i<colLine; i++){
//			JPanel col_panel = new JPanel();
//			col_panel.setBackground(new Color(33,33,33));
//			col_panel.setPreferredSize(new Dimension(700, 50));
//			p_container.add(col_panel);
//			
//			final int index = i;
//			
//			for(int j=0; j<rowLine+2; j++){
//				if(j==0 || j==rowLine+2-1){
//					JPanel row_panel = new JPanel(){
//						@Override
//						public void paint(Graphics g) {
//							URL url = getClass().getResource("/"+locationImg[index]);
//							try {
//								Image img = ImageIO.read(url);
//								g.drawImage(img, 0, 0, 50, 50, this);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					};
//					row_panel.setPreferredSize(new Dimension(50, 50));
//					col_panel.add(row_panel);
//				}else{
//					Seat seat = seatInfo.get(i*rowLine+j-1);
//					SeatButton row_panel = new SeatButton(seat.getSeat_id(), seat.getName(), seat.getStatus());
//					row_panel.setPreferredSize(new Dimension(50, 50));
//					col_panel.add(row_panel);
//					
//					//좌석을 인원수 만큼 선택할 수 있다.
//					//좌석을 한 번 누르면 해당 좌석의 status가 0이 된다.
//					row_panel.addMouseListener(new MouseAdapter() {
//						@Override
//						public void mouseClicked(MouseEvent e) {
//							if(row_panel.status == 1){
//								//5    5
//								if(selectSeat.size() < totalPersons){
//									if(row_panel.index == 0){
//										row_panel.index = 1;
//										selectSeat.remove(row_panel);
//									}else if(row_panel.index == 1){
//										row_panel.index = 0;
//										selectSeat.add(row_panel);
//									}
//								}else if(selectSeat.size() == totalPersons){
//									if(row_panel.index == 0){
//										row_panel.index = 1;
//										selectSeat.remove(row_panel);
//									}
//								}
//								row_panel.statusColor();
//							}
//						}
//					});
//				}
//			}
//		}
//	}
}
