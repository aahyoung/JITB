package com.user.main.purchase.ticket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.MoviePrice;
import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;
import com.user.main.OrderInfo;

public class PersonsChoiceScreen extends ScreenFrame{
	JPanel p_default;
	JPanel p_container;
	JPanel p_topInfo;
	JLabel la_topInfo;
	JPanel p_subInfo;
	JLabel la_subInfo;
	JPanel p_persons;
	JLabel la_persons;
	JPanel p_nomal;
	JLabel la_nomal;
	JPanel p_nomalNum;
	JPanel p_student;
	JLabel la_student;
	JPanel p_studentNum;
	JPanel p_bt;
	
	PersonsChoiceScreen pcs = this;
	
	ArrayList<NumButton> nomalBtn = new ArrayList<NumButton>();
	ArrayList<NumButton> studentBtn = new ArrayList<NumButton>();
	ArrayList<MoviePrice> moviePrice = new ArrayList<MoviePrice>();

	int nomalIndex;
	int studentIndex;
	boolean enable = false;
	
	public PersonsChoiceScreen(ClientMain main) {
		super(main);
		
		p_default = new JPanel();
		p_container = new JPanel();
		p_topInfo = new JPanel();
		la_topInfo = new JLabel("관람 인원수를 선택해주세요");
		p_subInfo = new JPanel();
		la_subInfo = new JLabel("인원수 선택은 일반, 청소년을 더해 최대 8매까지 가능해요");
		p_persons = new JPanel();
		la_persons = new JLabel("총 0명");
		p_nomal = new JPanel();
		la_nomal = new JLabel("일반");
		
		p_nomalNum = new JPanel();
		p_student = new JPanel();
		la_student = new JLabel("청소년");
		p_studentNum = new JPanel();
		p_bt = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/choice_end.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 50, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		p_default.setPreferredSize(new Dimension(600, 200));
		p_container.setPreferredSize(new Dimension(600, 600));
		p_topInfo.setPreferredSize(new Dimension(600, 50));
		p_subInfo.setPreferredSize(new Dimension(600, 40));
		p_persons.setPreferredSize(new Dimension(600, 100));
		p_nomal.setPreferredSize(new Dimension(80, 50));
		p_nomalNum.setPreferredSize(new Dimension(500, 50));
		p_student.setPreferredSize(new Dimension(80, 50));
		p_studentNum.setPreferredSize(new Dimension(500, 50));
		p_bt.setPreferredSize(new Dimension(200, 150));
		
		la_topInfo.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
		la_subInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_persons.setFont(new Font("Malgun Gothic", Font.PLAIN, 50));
		la_nomal.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
		la_student.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
		
		la_topInfo.setForeground(Color.WHITE);
		la_subInfo.setForeground(Color.WHITE);
		la_persons.setForeground(Color.WHITE);
		la_nomal.setForeground(Color.WHITE);
		la_student.setForeground(Color.WHITE);
		
		p_default.setBackground(new Color(33,33,33));
		p_container.setBackground(new Color(33,33,33));
		p_topInfo.setBackground(new Color(33,33,33));
		p_subInfo.setBackground(new Color(33,33,33));
		p_persons.setBackground(new Color(33,33,33));
		p_nomalNum.setBackground(new Color(33,33,33));
		p_studentNum.setBackground(new Color(33,33,33));
		p_nomal.setBackground(new Color(33,33,33));
		p_student.setBackground(new Color(33,33,33));
		
		selectType();
		
		for(int i=0; i<=8; i++){
			NumButton num = new NumButton(
						i, moviePrice.get(0).getType_id(), moviePrice.get(0).getPrice()
					);
			nomalBtn.add(num);
			p_nomalNum.add(num);
		}
		for(int i=0; i<=8; i++){
			NumButton num = new NumButton(
						i, moviePrice.get(1).getType_id(), moviePrice.get(1).getPrice()
					);
			studentBtn.add(num);
			p_studentNum.add(num);
		}
		for(int i=0; i<nomalBtn.size(); i++){
			nomalBtn.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NumButton nb = (NumButton)e.getSource();
					if(nb.editable == true){
						for(int j=0; j<nomalBtn.size(); j++){
							nomalBtn.get(j).label.setForeground(Color.WHITE);
							nomalBtn.get(j).label.updateUI();
						}
						nomalIndex = nb.index;
						for(int j=0; j<studentBtn.size(); j++){
							if(8-nomalIndex+1<=studentBtn.get(j).index){
								studentBtn.get(j).editable = false;
							}else{
								studentBtn.get(j).editable = true;
							}
						}
						int total = nomalIndex+studentIndex;
						la_persons.setText("총 "+total+"명");
						la_persons.updateUI();
						
						if(total != 0){
							enable = true;
						}else{
							enable = false;
						}
						
						nb.label.setForeground(Color.YELLOW);
						nb.label.updateUI();
						System.out.println(nb.price);
					}
				}
			});
		}
		for(int i=0; i<studentBtn.size(); i++){
			studentBtn.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NumButton nb = (NumButton)e.getSource();
					if(nb.editable == true){
						for(int j=0; j<studentBtn.size(); j++){
							studentBtn.get(j).label.setForeground(Color.WHITE);
							studentBtn.get(j).label.updateUI();
						}
						studentIndex = nb.index;
						for(int j=0; j<nomalBtn.size(); j++){
							if(8-studentIndex+1<=nomalBtn.get(j).index){
								nomalBtn.get(j).editable = false;
							}else{
								nomalBtn.get(j).editable = true;
							}
						}
						
						int total = nomalIndex+studentIndex;
						la_persons.setText("총 "+total+"명");
						la_persons.updateUI();
						
						if(total != 0){
							enable = true;
						}else{
							enable = false;
						}
						
						nb.label.setForeground(Color.YELLOW);
						nb.label.updateUI();
						System.out.println(nb.price);
					}
				}
			});
		}
		
		p_bt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(enable == true){
					//타입아이디, 인원수
					
					SeatsChoiceScreen nextScreen = ((SeatsChoiceScreen)main.screen.get(6));
					
					for(int i=0; i<moviePrice.size(); i++){
						OrderInfo orderInfo = new OrderInfo();
						orderInfo.setType_id(moviePrice.get(i).getType_id());
						orderInfo.setType(moviePrice.get(i).getType());
						orderInfo.setType_price(moviePrice.get(i).getPrice());
						nextScreen.orderInfos[i] = orderInfo;
					}
					
					nextScreen.nomal = new String[nomalIndex];
					nextScreen.student = new String[studentIndex];
					
					nextScreen.selectSeatOccupation(main.selectList.getProduct_id());
					nextScreen.createSeatBtn();
					
					main.setPage(6);
				}
			}
		});
		
		p_topInfo.add(la_topInfo);
		p_subInfo.add(la_subInfo);
		p_persons.add(la_persons);
		p_nomal.add(la_nomal);
		p_student.add(la_student);
		
		p_container.add(p_topInfo);
		p_container.add(p_subInfo);
		p_container.add(p_persons);
		p_container.add(p_nomal);
		p_container.add(p_nomalNum);
		p_container.add(p_student);
		p_container.add(p_studentNum);
		p_container.add(p_bt);
		
		add(p_default);
		add(p_container);
	}
	
	public void selectType(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		moviePrice.removeAll(moviePrice);
		
		String sql = "select * from movie_price";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				MoviePrice dto = new MoviePrice();
				dto.setType_id(rs.getInt("type_id"));
				dto.setType(rs.getString("type"));
				dto.setPrice(rs.getInt("ticket_price"));
				
				moviePrice.add(dto);
			}
			System.out.println(moviePrice.size());
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
