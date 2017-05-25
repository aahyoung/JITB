package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class CouponPanel extends PurchasePanelFrame{
	ArrayList<Gift> gifts = new ArrayList<Gift>();
	Canvas content;
	Canvas bt_go_purchase;
	Canvas bt_go_next;
	
	public CouponPanel(ClientMain main) {
		super(main);
		
		selectGift();
		
		int x = 0;
		int y = 0;
		for(int i=0; i<gifts.size(); i++){
			if(x == 3){
				x = 0;
				y++;
			}
			gifts.get(i).setBounds(0+(x*250), 0+(y*200), 250, 200); //230, 130
			x++;
		}
		
		content = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
				
				for(int i=0; i<gifts.size(); i++){
					URL url = null;
					try {
						url = new URL("http://localhost:8989/image/discount/"+gifts.get(i).getImg());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					//URL url = getClass().getResource("/"+gifts.get(i).getImg());
					try {
						g2.setColor(Color.WHITE);
						Image img = ImageIO.read(url);
						g2.drawImage(img, gifts.get(i).x+10, gifts.get(i).y+10, gifts.get(i).width-20, gifts.get(i).height-70, this);
						g2.drawString(gifts.get(i).getName(), gifts.get(i).x+30, gifts.get(i).y+180);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2.setColor(new Color(66,106,126));
					g2.draw(gifts.get(i));
				}
			}
		};
		
		bt_go_purchase = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_go_purchase.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 25, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		bt_go_next = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_go_next.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 25, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		content.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				
				for(int i=0; i<gifts.size(); i++){
					if(gifts.get(i).contains(point)){
						PaymentScreen screen = (PaymentScreen)main.screen.get(12);
						int price = Integer.parseInt(screen.la_total_price.getText());
						price = (int)(price * gifts.get(i).rate);
						screen.la_disc_price.setText(Integer.toString(price));
						
						int total = Integer.parseInt(screen.la_total_price.getText())-Integer.parseInt(screen.la_disc_price.getText());
						screen.la_remain_price.setText(Integer.toString(total));
						
						if(main.movie){
							main.selectList.setDiscount_type_id(gifts.get(i).getDiscount_type_id());
						}
						if(main.combo){
							main.selectCombo.setDiscount_type_id(gifts.get(i).getDiscount_type_id());
						}
						screen.setImg(2);
						screen.stepInfo.repaint();
						screen.setPanel(2);
					}
				}
			}
		});
		
		content.setPreferredSize(new Dimension(755, 550));
		bt_go_purchase.setPreferredSize(new Dimension(200, 100));
		bt_go_next.setPreferredSize(new Dimension(200, 100));
		
		bt_go_purchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				PurchasePanel nextPanel = (PurchasePanel)screen.content.get(2);
				screen.setImg(2);
				screen.stepInfo.repaint();
				nextPanel.isNoDiscount = true;
				screen.setPanel(2);
			}
		});
		
		bt_go_next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				screen.setImg(1);
				screen.stepInfo.repaint();
				screen.setPanel(1);
			}
		});
		
		add(content);
		add(bt_go_purchase);
		add(bt_go_next);
	}
	
	public void selectGift(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from gift";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Gift gift = new Gift();
				gift.setGift_id(rs.getInt("gift_id"));
				gift.setName(rs.getString("name"));
				gift.setRate(rs.getDouble("rate"));
				gift.setImg(rs.getString("img"));
				gift.setDiscount_type_id(rs.getInt("discount_type_id"));
				
				gifts.add(gift);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
