package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PointInputMessage extends PurchasePanelFrame implements Runnable{
	JPanel content;
	JPanel can_default;
	Canvas bt_cancle;
	
	Thread thread;
	
	long serial;
	boolean isSerial;
	UserPoint point;
	
	public PointInputMessage(ClientMain main) {
		super(main);
		
		content = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/point_input_message.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 50, 527, 402, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		can_default = new JPanel();
		bt_cancle = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_cancle.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, -10, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		bt_cancle.setBackground(Color.WHITE);
		content.setBackground(new Color(33,33,33));
		
		can_default.setPreferredSize(new Dimension(550, 350));
		bt_cancle.setPreferredSize(new Dimension(200, 50));
		content.setPreferredSize(new Dimension(550, 700));
		
		bt_cancle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				thread.interrupt();
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				screen.setPanel(1);
			}
		});
		
		content.add(can_default);
		content.add(bt_cancle);
		add(content);
	}
	
	public void inputPointCard(){
		Scanner scan = new Scanner(System.in);
		serial = scan.nextLong();
		
		System.out.println(serial);
		PaymentScreen screen = (PaymentScreen)main.screen.get(12);
		
		System.out.println("시리얼확인전");
		selectSerialNum(serial);
		System.out.println("시리얼확인후");
		
		//시리얼 넘버가 있는 경우
		if(isSerial){
			System.out.println("포인트적립전");
			//포인트 적립 후 결제완료 메시지
			pointInput();
			System.out.println("포인트적립후");
			screen.setPanel(5);
		}else{
			System.out.println("카드잘못됨");
			screen.setPanel(9);
		}
	}
	
	public void selectSerialNum(long serial_number){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from point_serial where serial_number = ?";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			pstmt.setLong(1, serial_number);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				isSerial = true;
				point = new UserPoint();
				point.setPoint_serial_id(rs.getInt("point_serial_id"));
				point.setSerial_number(rs.getLong("serial_number"));
				point.setPoint(rs.getInt("point"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pointInput(){
		System.out.println("포인트인풋");
		PreparedStatement pstmt = null;
		
		PaymentScreen screen = (PaymentScreen)main.screen.get(12);
		int plusPoint = (int)(Integer.parseInt(screen.la_total_price.getText()) * 0.1);
		
		String sql = "update point_serial set point = (point + ?) where point_serial_id = ?";
		try {
			pstmt = main.con.prepareStatement(sql);
			pstmt.setInt(1, plusPoint);
			pstmt.setInt(2, point.getPoint_serial_id());
			
			int result = pstmt.executeUpdate();
			System.out.println(result);
			if(result!=0){
				System.out.println("포인트적립!!");
			}else{
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void run() {
		inputPointCard();
	}
}
