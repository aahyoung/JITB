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
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PurchasePanel extends PurchasePanelFrame{
	ArrayList<NoDiscountPayment> noDiscounts = new ArrayList<NoDiscountPayment>();
	ArrayList<DiscountPayment> discounts = new ArrayList<DiscountPayment>();
	Canvas content;
	
	UserPoint point;
	boolean isNoDiscount;
	
	public PurchasePanel(ClientMain main) {
		super(main);
		
		selectNoDiscount();
		selectDiscount();
		
		int x = 0;
		int y = 0;
		for(int i=0; i<noDiscounts.size(); i++){
			if(x == 3){
				x = 0;
				y++;
			}
			noDiscounts.get(i).setBounds(0+(x*250), 0+(y*200), 250, 200); //230, 130
			x++;
		}
		for(int i=0; i<discounts.size(); i++){
			if(x == 3){
				x = 0;
				y++;
			}
			discounts.get(i).setBounds(0+(x*250), 0+(y*200), 250, 200); //230, 130
			x++;
		}
		
		content = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
				
				for(int i=0; i<noDiscounts.size(); i++){
					URL url = getClass().getResource("/"+noDiscounts.get(i).getImg());
					try {
						g2.setColor(Color.WHITE);
						Image img = ImageIO.read(url);
						g2.drawImage(img, noDiscounts.get(i).x+10, noDiscounts.get(i).y+10, noDiscounts.get(i).width-20, noDiscounts.get(i).height-70, this);
						g2.drawString(noDiscounts.get(i).getPayment_way(), noDiscounts.get(i).x+30, noDiscounts.get(i).y+180);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2.setColor(new Color(66,106,126));
					g2.draw(noDiscounts.get(i));
				}
				
				if(isNoDiscount){
					for(int i=0; i<discounts.size(); i++){
						URL url = getClass().getResource("/"+discounts.get(i).getImg());
						try {
							g2.setColor(Color.WHITE);
							Image img = ImageIO.read(url);
							g2.drawImage(img, discounts.get(i).x+10, discounts.get(i).y+10, discounts.get(i).width-20, discounts.get(i).height-70, this);
							g2.drawString(discounts.get(i).getName(), discounts.get(i).x+30, discounts.get(i).y+180);
						} catch (IOException e) {
							e.printStackTrace();
						}
						g2.setColor(new Color(66,106,126));
						g2.draw(discounts.get(i));
					}
				}
			}
		};
		
		content.setPreferredSize(new Dimension(755, 550));
		
		content.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				
				if(isNoDiscount){ //할인을 받지 않은 경우
					for(int i=0; i<discounts.size(); i++){
						if(discounts.contains(point)){
							if(main.movie){ //영화를 구매했다면
								//1. 좌석이 있는지 확인 => 있다면 2 진행, 없다면 NoSeatMessage 출력
								//2. order_movie에 구매 내역 insert
								//3. buy_seat에 구매한 좌석 insert
							}else if(main.combo){ //콤보를 구매했다면
								//1. order_snack에 구매 내역 insert
								//2. buy_snack에 구매한 스낵 insert
							}
						}
					}
				}else{ //이미 할인 받은 경우
					for(int i=0; i<noDiscounts.size(); i++){
						if(noDiscounts.contains(point)){
							if(point!=null){ //포인트 할인을 받은 경우
								//포인트 차감
							}
							
							if(main.movie){
								//위와 같고
								//4. movie_discount_history에 insert
							}else if(main.combo){
								//위와 같고
								//3. snack_discount_history에 insert
							}
						}
					}
				}
			}
		});
		
		add(content);
	}
	
	public void selectNoDiscount(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from payment_way";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				NoDiscountPayment noDiscount = new NoDiscountPayment();
				noDiscount.setPayment_way_id(rs.getInt("payment_way_id"));
				noDiscount.setPayment_way(rs.getString("payment_way"));
				noDiscount.setImg(rs.getString("img"));
				
				noDiscounts.add(noDiscount);
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
	
	public void selectDiscount(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int payment_way_id = 0;
		
		String sql = "select payment_way_id from payment_way where payment_way = '카드'";
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				payment_way_id = rs.getInt("payment_way_id");
			}
			
			sql = "select * from card";
		
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				DiscountPayment discount = new DiscountPayment();
				
				discount.setCard_id(rs.getInt("card_id"));
				discount.setName(rs.getString("name"));
				discount.setRate(rs.getDouble("rate"));
				discount.setImg(rs.getString("img"));
				discount.setDiscount_type_id(rs.getInt("discount_type_id"));
				discount.setPayment_way_id(payment_way_id);
				
				discounts.add(discount);
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
