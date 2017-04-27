package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PointOutputMessage extends PurchasePanelFrame {
	JPanel content;
	JPanel can_default;
	Canvas bt_cancle;
	JTextField hidden;
	PointOutputMessage pointPanel = this;
	
	int discount_type_id;
	
	StringBuffer serialBuffr = new StringBuffer();
	
	long serial;
	boolean isSerial;
	UserPoint point;
	
	public PointOutputMessage(ClientMain main) {
		super(main);
		
		hidden = new JTextField(){
			@Override
			public void setBorder(Border border) {
			}
		};
		content = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/point_output_message.png");
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
		hidden.setBackground(new Color(33,33,33));
		
		can_default.setPreferredSize(new Dimension(550, 350));
		bt_cancle.setPreferredSize(new Dimension(200, 50));
		content.setPreferredSize(new Dimension(550, 450));
		hidden.setPreferredSize(new Dimension(500, 300));
		
		bt_cancle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				screen.setPanel(1);
			}
		});
		
		hidden.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER){
					serialBuffr.delete(serialBuffr.length()-1, serialBuffr.length());
					serial = Long.parseLong(serialBuffr.toString());
					serialBuffr.delete(0, serialBuffr.length());

					PaymentScreen screen = (PaymentScreen)main.screen.get(12);
					ApplyPointPanel nextPanel = (ApplyPointPanel)screen.content.get(7);
					
					selectSerialNum(serial);
					
					//시리얼 넘버가 있는 경우
					if(point != null){
						nextPanel.point = point;
						nextPanel.la_point.setText(Integer.toString(point.getPoint()));
						screen.setPanel(7);
					}else{
						screen.setPanel(6);
					}
				}else{
					serialBuffr.append(e.getKeyChar());
					System.out.println(serialBuffr.toString());
				}
				
			}
		});
		
//		addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusLost(FocusEvent e) {
//				pointPanel.requestFocus();
//			}
//		});
		
		content.add(can_default);
		content.add(bt_cancle);
		add(content);
		add(hidden);
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
				point = new UserPoint();
				point.setPoint_serial_id(rs.getInt("point_serial_id"));
				point.setSerial_number(rs.getLong("serial_number"));
				point.setPoint(rs.getInt("point"));
				point.setDiscount_type_id(discount_type_id);
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
}
