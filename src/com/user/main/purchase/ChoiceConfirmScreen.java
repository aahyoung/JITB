package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class ChoiceConfirmScreen extends ScreenFrame{
	JPanel p_price;
	JPanel p_movie_price;
	JLabel la_movie_info;
	public JLabel la_movie_price;
	JPanel p_combo_price;
	JLabel la_combo_info;
	public JLabel la_combo_price;
	JPanel p_total_price;
	JLabel la_total_info;
	JLabel la_total_price;
	JLabel la_spar1;
	JLabel la_spar2;
	
	JLabel la_confirm_info;
	Canvas info;
	Canvas bt_purchase;
	
	ChoiceTicket ticket;
	ChoiceCombo combo;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd(E)");
	
	public ChoiceConfirmScreen(ClientMain main) {
		super(main);
		
		p_price = new JPanel();
		p_movie_price = new JPanel();
		la_movie_info = new JLabel("영화예매", JLabel.CENTER);
		la_movie_price = new JLabel("0", JLabel.CENTER);
		p_combo_price = new JPanel();
		la_combo_info = new JLabel("콤보구매", JLabel.CENTER);
		la_combo_price = new JLabel("0", JLabel.CENTER);
		p_total_price = new JPanel();
		la_total_info = new JLabel("총 결제금액", JLabel.CENTER);
		la_total_price = new JLabel("0", JLabel.CENTER);
		la_spar1 = new JLabel("-", JLabel.CENTER);
		la_spar2 = new JLabel("=", JLabel.CENTER);
		la_confirm_info = new JLabel("예매/주문 내역을 확인하세요", JLabel.CENTER);
		
		bt_purchase = new Canvas(){
			@Override
			public void paint(Graphics g) {//200 50
				URL url = getClass().getResource("/bt_purchase.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 50, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		la_spar1.setForeground(Color.WHITE);
		la_spar2.setForeground(Color.WHITE);
		la_movie_info.setForeground(Color.WHITE);
		la_movie_price.setForeground(Color.WHITE);
		la_combo_info.setForeground(Color.WHITE);
		la_combo_price.setForeground(Color.WHITE);
		la_total_info.setForeground(Color.WHITE);
		la_total_price.setForeground(Color.YELLOW);
		la_confirm_info.setForeground(Color.WHITE);
		
		la_spar1.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_spar2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_movie_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_movie_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_combo_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_combo_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_total_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_total_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_confirm_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		
		p_price.setPreferredSize(new Dimension(800, 90));
		p_movie_price.setPreferredSize(new Dimension(200, 90));
		la_spar1.setPreferredSize(new Dimension(25, 90));
		p_combo_price.setPreferredSize(new Dimension(200, 90));
		la_spar2.setPreferredSize(new Dimension(25, 90));
		p_total_price.setPreferredSize(new Dimension(200, 90));
		la_movie_info.setPreferredSize(new Dimension(200, 20));
		la_movie_price.setPreferredSize(new Dimension(200, 70));
		la_combo_info.setPreferredSize(new Dimension(200, 20));
		la_combo_price.setPreferredSize(new Dimension(200, 70));
		la_total_info.setPreferredSize(new Dimension(200, 20));
		la_total_price.setPreferredSize(new Dimension(200, 70));
		la_confirm_info.setPreferredSize(new Dimension(800, 80));
		bt_purchase.setPreferredSize(new Dimension(200, 100));
		
		p_price.setBackground(new Color(33,33,33));
		p_movie_price.setBackground(new Color(33,33,33));
		p_combo_price.setBackground(new Color(33,33,33));
		p_total_price.setBackground(new Color(33,33,33));
		
		bt_purchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaymentScreen nextScreen = (PaymentScreen)main.screen.get(12);
				nextScreen.la_total_price.setText(la_total_price.getText());
				main.setPage(12);
			}
		});
		
		p_price.add(p_movie_price);
		p_price.add(la_spar1);
		p_price.add(p_combo_price);
		p_price.add(la_spar2);
		p_price.add(p_total_price);
		p_movie_price.add(la_movie_info);
		p_movie_price.add(la_movie_price);
		p_combo_price.add(la_combo_info);
		p_combo_price.add(la_combo_price);
		p_total_price.add(la_total_info);
		p_total_price.add(la_total_price);
		add(p_price);
		add(la_confirm_info);
	}
	
	public void createInfo(){
		if(main.movie == true){
			ticket.setBounds(50, 150, 700, 300);
			if(main.combo == true){
				combo.setBounds(50, 150, 500, 300);
			}
		}else{
			if(main.combo == true){
				combo.setBounds(50, 500, 700, 300);
			}
		}
		
		info = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				
				if(main.movie == true){
					ticket.setBounds(25, 20, 650, 300);
					g2.draw(ticket);
					
					URL url = null;
					try {
						url = new URL("http://localhost:9090/image/movie/"+ticket.getPoster());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					//URL url = getClass().getResource("/"+ticket.getPoster());
					Image img;
					try {
						img = ImageIO.read(url);
						g2.drawImage(img, ticket.x+450, ticket.y+1, 200, 300-1, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2.setFont(new Font("Malgun Gothic", Font.BOLD, 35));
					g2.drawString(ticket.getMovie(), ticket.x+20, ticket.y+80);
					g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 28));
					g2.drawString(ticket.getBranch(), ticket.x+20, ticket.y+130);
					g2.drawString(ticket.getTheater(), ticket.x+20, ticket.y+165);
					g2.drawString(ticket.getTime(), ticket.x+20, ticket.y+200);
					g2.drawString(ticket.getPersons(), ticket.x+20, ticket.y+235);
					
					if(main.combo == true){
						combo.setBounds(25, 330, 650, 300);
						g2.draw(combo);
						
						URL comboUrl = null;
						try {
							comboUrl = new URL("http://localhost:9090/image/snack/"+combo.combo_img);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						//URL comboUrl = getClass().getResource("/"+combo.combo_img);
						Image comboImg = null;
						try {
							comboImg = ImageIO.read(comboUrl);
							g2.drawImage(comboImg, combo.x+430, combo.y+25, 200, 260, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
						g2.setFont(new Font("Malgun Gothic", Font.BOLD, 35));
						g2.drawString(combo.getCombo_name(), combo.x+20, combo.y+65);
						
						g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
						int count = 0;
						Iterator it = combo.getAmount().keySet().iterator();
						while(it.hasNext()){
							String opt = (String)it.next();
							g2.drawString(opt+"X"+combo.getAmount().get(opt), combo.x+20, combo.y+115+(count*35));
							count++;
						}
					}
				}else{
					if(main.combo == true){
						combo.setBounds(25, 20, 650, 300);
						g2.draw(combo);
						
						URL comboUrl = null;
						try {
							comboUrl = new URL("http://localhost:9090/image/snack/"+combo.combo_img);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						//URL comboUrl = getClass().getResource("/"+combo.combo_img);
						Image comboImg = null;
						try {
							comboImg = ImageIO.read(comboUrl);
							g2.drawImage(comboImg, combo.x+430, combo.y+25, 200, 260, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
						g2.setFont(new Font("Malgun Gothic", Font.BOLD, 35));
						g2.drawString(combo.getCombo_name(), combo.x+20, combo.y+65);
						
						g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
						int count = 0;
						Iterator it = combo.getAmount().keySet().iterator();
						while(it.hasNext()){
							String opt = (String)it.next();
							g2.drawString(opt+" X"+combo.getAmount().get(opt), combo.x+20, combo.y+115+(count*35));
							count++;
						}
					}
				}
			}
		};
		
		if(main.movie == false || main.combo == false){
			info.setPreferredSize(new Dimension(700, 350));
		}else{
			info.setPreferredSize(new Dimension(700, 650));
		}
		add(info);
		add(bt_purchase);
	}
	
	public void setTotalPrice(){
		int movie_price = Integer.parseInt(la_movie_price.getText());
		int combo_price = Integer.parseInt(la_combo_price.getText());
		la_total_price.setText(Integer.toString(movie_price+combo_price));
	}
	
	public void selectChoiceProduct(int product_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select m.name as 영화이름, b.name as 지점이름, t.name as 관이름,");
		sql.append(" to_char(p.screening_date, 'YYYY.MM.DD') as 날짜,");
		sql.append(" (select to_char(sysdate, 'DY', 'NLS_DATE_LANGUAGE=KOREAN') from dual) as 요일,");
		sql.append(" p.start_time as 시작시간, m.poster as 포스터");
		sql.append(" from product p");
		sql.append(" inner join movie m on p.movie_id = m.movie_id ");
		sql.append(" inner join theater t on p.theater_id = t.theater_id");
		sql.append(" inner join branch b on t.branch_id = b.branch_id");
		sql.append(" where product_id = ?");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, product_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				ticket = new ChoiceTicket();
				ticket.setMovie(rs.getString("영화이름"));
				ticket.setBranch(rs.getString("지점이름"));
				ticket.setTheater(rs.getString("관이름"));
				ticket.setTime(rs.getString("날짜")+"("+rs.getString("요일")+") "+rs.getString("시작시간"));
				ticket.setPoster(rs.getString("포스터"));
			}
			
			StringBuffer persons = new StringBuffer();
			
			for(int i=0; i<main.selectList.getOrderInfos().size(); i++){
				if(main.selectList.getOrderInfos().get(i).getSeatName().length != 0){
					persons.append(main.selectList.getOrderInfos().get(i).getType()+" ");
					persons.append(main.selectList.getOrderInfos().get(i).getSeatName().length+"매 ");
				}
			}
			ticket.setPersons(persons.toString());
			
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
	
	public void extractCombo(ArrayList<Integer> sub_opt_id){
		Collections.sort(sub_opt_id);
		
		HashMap<String, Integer> amount = new HashMap<String, Integer>();
		int count = 1;
		int prev_id;
		String subOpt = null;
		
		for(int i=1; i<sub_opt_id.size(); i++){
			if(sub_opt_id.get(i-1) == sub_opt_id.get(i)){
				count++;
			}else{
				subOpt = selectChoiceSubOpt(sub_opt_id.get(i-1));
				amount.put(subOpt, count);
				count = 1;
			}
		}
		
		subOpt = selectChoiceSubOpt(sub_opt_id.get(sub_opt_id.size()-1));
		amount.put(subOpt, count);
		
		selectChoiceCombo(main.selectCombo.getCombo_id());
		combo.setAmount(amount);
		
		Iterator it = amount.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			System.out.println("id"+key+","+amount.get(key));
		}
	}
	
	public String selectChoiceSubOpt(int sub_opt_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer subOpt = new StringBuffer();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select s.name as 이름, tos.opt_size as 사이즈 from top_opt_size tos");
		sql.append(" inner join sub_opt s on tos.top_opt_size_id = s.top_opt_size_id");
		sql.append(" where s.sub_opt_id = ?");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, sub_opt_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				subOpt.append(rs.getString("이름")+"("+rs.getString("사이즈")+")");
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
		
		return subOpt.toString();
	}
	
	public void selectChoiceCombo(int combo_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select name as 콤보이름, img as 콤보이미지 from combo where combo_id = ?";
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, combo_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				combo = new ChoiceCombo();
				combo.setCombo_name(rs.getString("콤보이름"));
				combo.setCombo_img(rs.getString("콤보이미지"));
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
