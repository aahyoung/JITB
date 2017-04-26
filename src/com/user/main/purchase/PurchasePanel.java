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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PurchasePanel extends PurchasePanelFrame{
	ArrayList<NoDiscountPayment> noDiscounts = new ArrayList<NoDiscountPayment>();
	ArrayList<DiscountPayment> discounts = new ArrayList<DiscountPayment>();
	Canvas content;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	
	UserPoint point;
	boolean isNoDiscount;
	boolean isPoint;
	boolean isExistSeat = false;
	
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
				
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				
				if(isNoDiscount){ //할인을 받지 않은 경우
					for(int i=0; i<noDiscounts.size(); i++){
						if(noDiscounts.get(i).contains(point)){
							if(main.movie){ //영화를 구매했다면
								purchaseMovie(discounts.get(i).getPayment_way_id());
							}
							if(main.combo){ //콤보를 구매했다면
								purchaseSnack(discounts.get(i).getPayment_way_id());
							}
							PointInputMessage nextScreen = (PointInputMessage)screen.content.get(4);
							nextScreen.thread = new Thread(nextScreen);
							nextScreen.thread.start();
							screen.setPanel(4);
						}
					}
					for(int i=0; i<discounts.size(); i++){
						if(discounts.get(i).contains(point)){
							if(main.movie){ //영화를 구매했다면
								purchaseMovie(discounts.get(i).getPayment_way_id());
							}
							if(main.combo){ //콤보를 구매했다면
								purchaseSnack(discounts.get(i).getPayment_way_id());
							}
							PointInputMessage nextScreen = (PointInputMessage)screen.content.get(4);
							nextScreen.thread = new Thread(nextScreen);
							nextScreen.thread.start();
							screen.setPanel(4);
						}
					}
				}else{ //이미 할인 받은 경우
					for(int i=0; i<noDiscounts.size(); i++){
						if(noDiscounts.get(i).contains(point)){
							if(isPoint){ //포인트 할인을 받은 경우
								//포인트 차감
								subPoint();
							}
							
							if(main.movie){
								//위와 같고
								purchaseMovie(discounts.get(i).getPayment_way_id());
								//4. movie_discount_history에 insert
								if(!isExistSeat){
									movieDiscountHistory();
								}
							}
							if(main.combo){
								//위와 같고
								purchaseSnack(discounts.get(i).getPayment_way_id());
								//3. snack_discount_history에 insert
								if(!isExistSeat){
									snackDiscountHistory();
								}
							}
							screen.setPanel(5);
						}
					}
				}
			}
		});
		
		add(content);
	}
	
	public void purchaseMovie(int payment_way_id){
		PreparedStatement pstmt = null;
		
		//1. 좌석이 있는지 확인 => 있다면 2 진행, 없다면 NoSeatMessage 출력
		PaymentScreen screen = (PaymentScreen)main.screen.get(12);
		
		for(int j=0; j<main.selectList.getOrderInfos().size(); j++){
			String[] seats = main.selectList.getOrderInfos().get(j).getSeatName();
			for(int k=0; k<seats.length; k++){
				isExistSeat = selectExistSeat(main.selectList.getProduct_id(), seats[k]);
			}
		}
		
		if(isExistSeat){
			screen.setPanel(8);
		}else{
			//2. order_movie에 구매 내역 insert
			StringBuffer sql = new StringBuffer();
			sql.append("insert into order_movie (order_id, order_time, order_price, total_price, payment_way_id)");
			sql.append(" values(seq_order_movie.nextval, to_date(?, 'yyyy-MM-dd HH24:MI:SS'), ?, ?, ?)");
			
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			String now = sdf.format(cal.getTime());
			
			try {
				pstmt = main.con.prepareStatement(sql.toString());
				pstmt.setString(1, now);
				
				//결제 가격
				int price = Integer.parseInt(screen.la_total_price.getText());
				pstmt.setInt(2, price);

				//할인 뺀 가격
				pstmt.setInt(3, main.selectList.getPrice());
				pstmt.setInt(4, payment_way_id);
				
				int result = pstmt.executeUpdate();
				
				if(result != 0){
					System.out.println("영화주문정보등록!");
				}
				
				//3. buy_seat에 구매한 좌석 insert
				sql.delete(0, sql.length());
				sql.append("insert into buy_seat (buy_seat_id, name, product_id, type_id, order_id)");
				sql.append(" values (seq_buy_seat.nextval, ?, ?, ?, seq_order_movie.currval)");
				
				for(int i=0; i<main.selectList.getOrderInfos().size(); i++){
					for(int j=0; j<main.selectList.getOrderInfos().get(i).getSeatName().length; j++){
						pstmt = main.con.prepareStatement(sql.toString());
						pstmt.setString(1, main.selectList.getOrderInfos().get(i).getSeatName()[j]);
						pstmt.setInt(2, main.selectList.getProduct_id());
						pstmt.setInt(3, main.selectList.getOrderInfos().get(i).getType_id());
						
						int res = pstmt.executeUpdate();
						if(res!=0){
							System.out.println("좌석구매!");
						}
					}
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try {
					if(pstmt!=null){
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}

	}
	
	public void purchaseSnack(int payment_way_id){
		PreparedStatement pstmt = null;
		
		PaymentScreen screen = (PaymentScreen)main.screen.get(12);
		
		//1. order_snack에 구매 내역 insert
		StringBuffer sql = new StringBuffer();
		sql.append("insert into order_snack (order_snack_id, order_time, order_price, total_price, payment_way_id)");
		sql.append(" values(seq_order_snack.nextval, to_date(?, 'yyyy-MM-dd HH24:MI:SS'), ?, ?, ?)");
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String now = sdf.format(cal.getTime());
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setString(1, now);
			
			//결제 가격
			int price = Integer.parseInt(screen.la_total_price.getText());
			pstmt.setInt(2, price);
			
			//할인 뺀 가격
			pstmt.setInt(3, main.selectCombo.getPrice());
			pstmt.setInt(4, payment_way_id);
			
			int result = pstmt.executeUpdate();
			
			if(result != 0){
				System.out.println("스낵주문정보등록!");
			}
		
			//2. buy_snack에 구매한 스낵 insert
			sql.delete(0, sql.length());
			sql.append("insert into buy_snack (buy_snack_id, sub_opt_id, combo_id, order_snack_id)");
			sql.append(" values (seq_buy_snack.nextval, ?, ?, seq_order_snack.currval)");
			for(int i=0; i<main.selectCombo.getSub_opt_id().size(); i++){
				pstmt = main.con.prepareStatement(sql.toString());
				pstmt.setInt(1, main.selectCombo.getSub_opt_id().get(i));
				pstmt.setInt(2, main.selectCombo.getCombo_id());
				
				int res = pstmt.executeUpdate();
				if(res != 0){
					System.out.println("스낵구매!");
				}
			}
			
			//3. 구매한 상품 재고 차감
			sql.delete(0, sql.length());
			sql.append("update sub_opt set stock = (stock-1) where sub_opt_id = ?");
			
			for(int i=0; i<main.selectCombo.getSub_opt_id().size(); i++){
				pstmt = main.con.prepareStatement(sql.toString());
				pstmt.setInt(1, main.selectCombo.getSub_opt_id().get(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void movieDiscountHistory(){
		PreparedStatement pstmt = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into movie_discount_history(discount_history_id, order_id, discount_type_id)");
		sql.append(" values(seq_movie_discount_history.nextval, seq_order_movie.currval, ?)");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, main.selectList.getDiscount_type_id());
			
			int result = pstmt.executeUpdate();
			
			if(result != 0){
				System.out.println("영화할인정보추가");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
	}
	
	public void snackDiscountHistory(){
		PreparedStatement pstmt = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into snack_discount_history(discount_history_id, order_snack_id, discount_type_id)");
		sql.append(" values(seq_snack_discount_history.nextval, seq_order_snack.currval, ?)");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, main.selectCombo.getDiscount_type_id());
			
			int result = pstmt.executeUpdate();
			
			if(result != 0){
				System.out.println("스낵할인정보추가");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void subPoint(){
		PreparedStatement pstmt = null;
		
		String sql = "update point_serial set point = (point - ?) where point_serial_id = ?";
		try {
			pstmt = main.con.prepareStatement(sql);
			pstmt.setInt(1, point.getUse_point());
			pstmt.setInt(2, point.getPoint_serial_id());
			
			int result = pstmt.executeUpdate();
			
			if(result!=0){
				System.out.println("포인트삭감!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	
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
	
	public boolean selectExistSeat(int product_id, String seat_name){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean isExist = false;
		
		String sql = "select * from buy_seat where product_id = ? and name = ?";
		try {
			pstmt = main.con.prepareStatement(sql);
			pstmt.setInt(1, product_id);
			pstmt.setString(2, seat_name);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				isExist = true;
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
		
		return isExist;
	}
}
