package com.user.main.purchase.ticket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;
import com.user.main.OrderInfo;

public class SeatsChoiceScreen extends ScreenFrame{
	JLabel la_priceInfo;
	JLabel la_price;
	JPanel p_container;
	JPanel p_screenInfo;
	JPanel p_bt;
	
	ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
	String nomal[];
	String student[];
	
	ArrayList<SeatButton> selectSeat = new ArrayList<SeatButton>();
	ArrayList<String> occupiedSeat = new ArrayList<String>();
	ArrayList<String> existSeat = new ArrayList<String>();
	String theaterName;
	
	String[] locationImg = {
			"A.png", "B.png", "C.png", "D.png", "E.png", "F.png", "G.png", "H.png", "I.png", "J.png"
	};
	
	public SeatsChoiceScreen(ClientMain main) {
		super(main);
		
		la_priceInfo = new JLabel("총 결제금액");
		la_price = new JLabel("0");
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
		
		p_bt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				purchase();
			}
		});
		
		p_container.add(p_screenInfo);
		add(la_priceInfo);
		add(la_price);
		add(p_container);
		add(p_bt);
		
	}
	
	public void createSeatBtn(){
		char seatName = 'A';
		exelSeatSetting();
		
		for(int i=0; i<10; i++){
			JPanel col_panel = new JPanel();
			col_panel.setBackground(new Color(33,33,33));
			col_panel.setPreferredSize(new Dimension(700, 50));
			p_container.add(col_panel);
			
			final int index = i;
			
			for(int j=0; j<10+2; j++){
				if(j==0 || j==10+2-1){
					JPanel row_panel = new JPanel(){
						@Override
						public void paint(Graphics g) {
							URL url = getClass().getResource("/"+locationImg[index]);
							try {
								Image img = ImageIO.read(url);
								g.drawImage(img, 0, 0, 50, 50, this);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					row_panel.setPreferredSize(new Dimension(50, 50));
					col_panel.add(row_panel);
				}else{
					String seatNaming = ""+seatName+(j);
					int status = 1;
					
					if(existSeat.get(j).equalsIgnoreCase("X")){
						status = -1;
					}
					
					for(int k=0; k<occupiedSeat.size(); k++){
						if(occupiedSeat.get(k).equals(seatNaming)){
							status = 0;
						}
					}
					
					SeatButton row_panel = new SeatButton(seatNaming, status);
					
					row_panel.setPreferredSize(new Dimension(50, 50));
					col_panel.add(row_panel);
					
					//좌석을 인원수 만큼 선택할 수 있다.
					//좌석을 한 번 누르면 해당 좌석의 status가 0이 된다.
					row_panel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if(row_panel.status == 1){
								if(selectSeat.size() < nomal.length+student.length){
									if(row_panel.index == 0){ //자리선택해제
										release(row_panel);
									}else if(row_panel.index == 1){ //자리선택
										select(row_panel);
									}
								}else if(selectSeat.size() == nomal.length+student.length){
									if(row_panel.index == 0){ //자리선택해제
										release(row_panel);
									}
								}
								row_panel.statusColor();
							}
						}
					});
				}
			}
			seatName++;
		}
	}
	
	public void release(SeatButton btn){
		for(int i=0; i<nomal.length; i++){
			if(nomal[i] != null){
				if(nomal[i].equals(btn.seat_name)){
					nomal[i] = null;
					btn.index = 1;
					selectSeat.remove(btn);
					
					int price = Integer.parseInt(la_price.getText());
					la_price.setText(Integer.toString(price - orderInfos.get(0).getType_price()));
					
					return;
				}
			}
		}
		for(int i=0; i<student.length; i++){
			if(student[i] != null){
				if(student[i].equals(btn.seat_name)){
					student[i] = null;
					btn.index = 1;
					selectSeat.remove(btn);
					
					int price = Integer.parseInt(la_price.getText());
					la_price.setText(Integer.toString(price - orderInfos.get(1).getType_price()));
					
					return;
				}
			}
		}
	}
	
	public void select(SeatButton btn){
		for(int i=0; i<nomal.length; i++){
			if(nomal[i] == null){
				nomal[i] = btn.seat_name;
				btn.index = 0;
				selectSeat.add(btn);
				
				int price = Integer.parseInt(la_price.getText());
				la_price.setText(Integer.toString(price + orderInfos.get(0).getType_price()));
				
				return;
			}
		}
		for(int i=0; i<student.length; i++){
			if(student[i] == null){
				student[i] = btn.seat_name;
				btn.index = 0;
				selectSeat.add(btn);
				System.out.println(orderInfos.get(1).getType_price());
				int price = Integer.parseInt(la_price.getText());
				la_price.setText(Integer.toString(price + orderInfos.get(1).getType_price()));
				
				return;
			}
		}
	}
	
	public void purchase(){
		for(int i=0; i<nomal.length; i++){
			if(nomal[i] == null){
				return;
			}
		}
		
		for(int i=0; i<student.length; i++){
			if(student[i] == null){
				return;
			}
		}
		
		orderInfos.get(0).setSeatName(nomal);
		orderInfos.get(1).setSeatName(student);
		main.selectList.setOrderInfos(orderInfos);
		main.selectList.setPrice(Integer.parseInt(la_price.getText()));
		main.movie = true;
		main.setPage(7);
	}
	
	public void exelSeatSetting(){
		File file = new File("C:/JITB Java Project/JITB/res_manager/영화관 좌석표.xls");
		FileInputStream fis;
		try {
			DataFormatter df = new DataFormatter();
			fis = new FileInputStream(file);
			
			HSSFWorkbook book = new HSSFWorkbook(fis);
			selectTheaterName();
			HSSFSheet sheet = book.getSheet(theaterName);
			
			for(int i=0; i<10; i++){
				HSSFRow row = sheet.getRow(i);
				for(int j=0; j<10; j++){
					HSSFCell cell = row.getCell(j);
					String value = df.formatCellValue(cell);
					existSeat.add(value);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void selectSeatOccupation(int product_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select name from buy_seat where product_id=?";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			pstmt.setInt(1, product_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				occupiedSeat.add(rs.getString("name"));
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
	
	public void selectTheaterName(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.name as 관이름 from product p");
		sql.append(" inner join theater t on p.theater_id = t.theater_id");
		sql.append(" where product_id = ?");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, main.selectList.getProduct_id());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				theaterName = rs.getString("관이름");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
