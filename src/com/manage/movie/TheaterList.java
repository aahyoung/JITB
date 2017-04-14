/*
 * 영화관 목록 출력 및 추가 클래스
 * 현재 상황)
 * getTheaterList()를 이용하여 theater 테이블 가져오는 메소드 구현중
 * */
package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import com.jitb.db.DBManager;

// 관 목록 레이아웃
public class TheaterList extends JPanel implements ActionListener{
	JPanel p_north, p_content, p_theater, p_none;
	JLabel lb_title, lb_none;
	JButton bt_add;
	
	AddTheater addTheater;
	
	JDesktopPane desktop1=new JDesktopPane();
	JDesktopPane desktop2=new JDesktopPane();
	
	DBManager manager;
	Connection con;
	
	// innerFrame이 켜져있는 상태면 추가 버튼 비활성화
	boolean add=false;
	
	// 현재 존재하는 영화관 정보를 담아놓을 collection framework
	ArrayList<TheaterData> theaterList=new ArrayList<TheaterData>();
	
	// 영화관 패널을 담아놓을 collection framework
	ArrayList<Theater> theaters=new ArrayList<Theater>();
	
	// 각 영화관 패널
	Theater theater;
	
	// 선택된 영화관의 id
	int id;
	
	EditTheater selectMovie;
	
	public TheaterList() {
		// DB 연결
		connect();
						
		// 영화관 가져오기
		getTheaterList();
		
		p_north=new JPanel();
		p_content=new JPanel();
		p_none=new JPanel();	// 영화관이 존재하지 않을 때 보여줄 패널
		p_theater=new JPanel();	// 영화관이 존재할 때 보여줄 패널
		lb_title=new JLabel("영화관 목록");
		lb_none=new JLabel("등록된 영화관이 없습니다. 추가 버튼을 눌러 영화관을 선택하세요.");
		bt_add=new JButton("추가");
		
		showTheaterList();
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_none);
		p_content.add(p_theater);
		
		bt_add.addActionListener(this);
		
		if(add){
			bt_add.setEnabled(false);
		}
		else{
			bt_add.setEnabled(true);
		}
		
		setLayout(new BorderLayout());
		add(p_north,BorderLayout.NORTH);
		add(p_content);
		
		setBackground(Color.red);

	}
	
	// DB 연결
		public void connect(){
			manager=DBManager.getInstance();
			con=manager.getConnect();
		}
		
	// 현재 존재하는 영화관을 DB에서 가져오기
	public void getTheaterList(){
		theaterList.removeAll(theaterList);
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from theater order by theater_id";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				TheaterData dto=new TheaterData();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setName(rs.getString("name"));
				dto.setRow_line(rs.getInt("row_line"));
				dto.setColumn_line(rs.getInt("column_line"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setMovie_id(rs.getString("movie_id"));
				
				theaterList.add(dto);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 추가 버튼을 누르면 생성되는 InnerFrame
	public void makeAddFrame(){
		//JDesktopPane desktop=new JDesktopPane();
		
		Dimension outerSize=this.getSize();
		
		addTheater=new AddTheater(this,"영화관 추가", true, false, true);
		//addTheater.setBounds(outerSize.width/2, outerSize.height/2, 300, 200);
		
		desktop1.add(addTheater);
		
		this.p_content.add(desktop1);
		
		p_theater.setVisible(false);
		
	}
	
	// 영화관 패널을 선택하면 영화를 선택할 수 있는 innerFrame 생성
	public void makeSelectFrame(){
		Dimension outerSize=this.getSize();
		selectMovie=new EditTheater(this, "영화 선택", true, false, true, id);
		
		desktop1.add(selectMovie);
		
		this.p_content.add(desktop1);
		
		p_theater.setVisible(false);

	}
	
	// 영화관 패널 설정 -> 각 Theater에 theater_id를 저장하고 있음
	public void setTheaterList(){
		theaters.removeAll(theaters);
		p_theater.removeAll();
		
		for(int i=0; i<theaterList.size(); i++){
			theater=new Theater();
			String name=theaterList.get(i).getName();
			theater.lb_name.setText(name+"관");
			int row=theaterList.get(i).getRow_line();
			int col=theaterList.get(i).getColumn_line();
			theater.lb_number.setText(row+"행 "+col+" 열");
			theater.theater_id=theaterList.get(i).getTheater_id();
			p_theater.add(theater);
			
			theaters.add(theater);
			
		}
		
		for(int j=0; j<theaters.size(); j++){
			theaters.get(j).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object obj=e.getSource();
					
					for(int k=0; k<theaters.size(); k++){
						if(obj==theaters.get(k)){
							System.out.println(theaters.get(k).theater_id+"관 선택");
							id=theaters.get(k).theater_id;
							makeSelectFrame();
						}
					}
				}
			});
		}
	}
	
	// 영화관 목록 출력
	public void showTheaterList(){
		if(theaterList.size()==0){
			p_none.add(lb_none);
			p_none.setVisible(true);
			p_theater.setVisible(false);
		}
		else{
			setTheaterList();
			p_none.setVisible(false);
			p_theater.setVisible(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)obj;
		
		// innerFrame이 생성된 적이 없으면
		if(!add){
			// 영화관 추가 버튼
			if(bt==bt_add){
				makeAddFrame();
				System.out.println("추가 누름");
				add=true;
			}
		}
		else{
			addTheater.setDefault();
			addTheater.setVisible(true);
		}
	}
}
