package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jitb.db.DBManager;

/*
 * 1. 레이아웃 구성
 * p_north		영화관 추가 버튼 구현
 * p_content	- 영화관 목록이 존재하면 			p_list 추가된 영화관 목록 출력
 * 				- 영화관 목록이 존재하지 않으면 		p_warning 영화관 추가 요청 문구 출력
 * 2. 기능 구현
 * 1) 영화관 추가 버튼은 JDialog으로 구현
 * 2) p_list의 영화관 리스트는 영화관 이름/행열 정보 구현
 * 3) 각 영화관 패널을 누르면 JDialog으로 수정/삭제/영화 선택 구현
 * */

public class TheaterMain extends JPanel implements ActionListener{
	JPanel p_north, p_content;
	JPanel p_list, p_warning;
	JButton bt_add;
	
	JLabel lb_title, lb_warning;
	
	JScrollPane scroll;
	
	// 영화관 추가 Dialog
	AddTheater addTheater;
	
	// 영화관 수정/삭제/영화 선택 Dialog
	EditTheater editTheater;
	
	// 각 영화관 패널
	TheaterItem theaterItem;
	
	// 현재 영화관 데이터가 존재하는지 여부 확인
	boolean exist=false;
	
	// DB연동에 필요
	DBManager manager;
	Connection con;
	
	// DB로부터 현재 존재하는 영화관 정보를 담아놓을 collection framework
	ArrayList<Theater> theaterList=new ArrayList<Theater>();
	
	// 영화관 패널을 담아놓을 collection framework
	ArrayList<TheaterItem> theaters=new ArrayList<TheaterItem>();
	
	// 선택된 영화관의 id
	int id;
	
	public TheaterMain() {
		// DB 연동
		connect();
		
		// 현재 존재하는 영화관 갯수 계산 및 출력
		getTheaterList();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_list=new JPanel();
		p_warning=new JPanel();
		
		lb_title=new JLabel("영화관 목록");
		lb_warning=new JLabel("현재 등록된 영화관이 없습니다. 영화관을 등록해주세요.");
		
		bt_add=new JButton("영화관 추가");
		
		// 영화관 목록 패널에 scroll 붙이기
		//scroll=new JScrollPane(p_list);
		showTheaterList();
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		bt_add.addActionListener(this);
		
		//p_warning.add(lb_warning);
		//p_warning.setBackground(Color.red);
		
		p_content.setBackground(Color.white);
		p_content.setLayout(new BorderLayout());
		p_content.add(p_list);
		//p_content.add(p_warning);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
		//addFrame();

	}
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 현재 존재하는 영화관을 DB에서 가져오기
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		theaterList.removeAll(theaterList);
		
		String sql="select * from theater order by theater_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
	
			while(rs.next()){			
				Theater dto=new Theater();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setName(rs.getString("name"));
				dto.setRow_line(rs.getInt("row_line"));
				dto.setColumn_line(rs.getInt("column_line"));
				
				theaterList.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 영화관 패널 설정
	public void setTheaterList(){
		theaters.removeAll(theaters);
		p_list.removeAll();
		
		// 현재 존재하는 영화관만큼 생성하고 설정 및 출력
		for(int i=0; i<theaterList.size(); i++){
			String name=theaterList.get(i).getName()+" 관";
			int row=theaterList.get(i).getRow_line();
			int col=theaterList.get(i).getColumn_line();
			
			theaterItem=new TheaterItem(name, row, col);
			p_list.add(theaterItem);
			
			theaters.add(theaterItem);
		}
		
		for(int j=0; j<theaters.size(); j++){
			theaters.get(j).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object obj=e.getSource();
					
					for(int k=0; k<theaters.size(); k++){
						if(obj==theaters.get(k)){
							System.out.println(theaters.get(k).theater_id+"관 선택");
							id=theaters.get(k).theater_id;
							editTheater=new EditTheater(TheaterMain.this);
						}
					}
				}
			});
		}
		
		System.out.println("영화관 세팅");
	}
	
	// 영화관 목록 출력
	public void showTheaterList(){
		if(theaterList.size()==0){
			p_warning.setVisible(true);
			p_list.setVisible(false);
		}
		else{
			setTheaterList();
			p_warning.setVisible(false);
			p_list.setVisible(true);
			System.out.println("영화관 출력");
		}
	}
	
	// 영화관 수정 및 삭제 internalFrame 생성
	public void editFrame(){
		
	}

	// 영화관 추가 버튼
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
	
		if(obj==bt_add){
			addTheater=new AddTheater(this);
			System.out.println("영화관 추가");
		}
	}
}
