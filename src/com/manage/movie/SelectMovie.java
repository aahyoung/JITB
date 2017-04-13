package com.manage.movie;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jitb.db.DBManager;

// 영화관 추가 레이아웃
public class SelectMovie extends JInternalFrame implements ActionListener, ItemListener{
	JPanel p_outer;
	JPanel p_title;
	JPanel p_select;
	JPanel p_button;
	
	JLabel lb_title;

	Choice ch_movie;

	JButton bt_cancel, bt_confirm;

	// DB연동에 필요
	DBManager manager;
	Connection con;
	
	// 현재 선택된 panel의 index
	int index;	
	
	ArrayList<MovieData> movie=new ArrayList<MovieData>();
	
	public SelectMovie(String title, boolean resizable, boolean closable, boolean maximizable, int index) {
		this.title=title;
		this.resizable=resizable;
		this.closable=closable;
		this.maximizable=maximizable;
		this.index=index;
		
		// DB 연결
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("현재 영화관에 상영할 영화를 선택해주세요.");
		ch_movie=new Choice();
		getMovieList();
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		
		// choice와 ItemListener 연결
		ch_movie.addItemListener(this);
		
		p_title.add(lb_title);
		
		p_select.add(ch_movie);
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		
		p_outer.add(p_title);
		p_outer.add(p_select);
		p_outer.add(p_button);
		
		add(p_outer);
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		setBounds(370, 220, 500, 200);
		setVisible(true);
		
	}
	
	// DB 연결
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// movie 테이블에 존재하는 영화 리스트 가져오기
	public void getMovieList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select * from movie";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				MovieData movieData=new MovieData();
				movieData.setMovie_id(rs.getInt("movie_id"));
				movieData.setPoster(rs.getString("poster"));
				movieData.setName(rs.getString("name"));
				movieData.setDirector(rs.getString("director"));
				movieData.setMain_actor(rs.getString("main_actor"));
				movieData.setStory(rs.getString("story"));
				movieData.setStart_date(rs.getString("start_date"));
				movieData.setEnd_date(rs.getString("end_date"));
				movieData.setRun_time(rs.getInt("run_time"));
				
				movie.add(movieData);
				
				ch_movie.add(movieData.getName());
			}
			
			System.out.println("영화 목록 받아옴");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 선택한 영화의 movie_id 정보 저장
	// -> theater 테이블에 데이터 저장
	public void setMovieID(){
		
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("update theater set movie_id=? where theater_id=?");
		
		String id=Integer.toString(movie.get(ch_movie.getSelectedIndex()).getMovie_id());
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, Integer.toString(index));
			
			// 성공적으로 insert문 반환시 무조건 1 반환
			int result=pstmt.executeUpdate();
			if(result!=0){
				System.out.println("movie_id 변경 성공");
			}
			else{
				System.out.println("movie_id 변경 실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 하나의 frame을 가지고 사용하므로 기존 값으로 항상 초기화
	public void setDefault(){
		ch_movie.select(0);
	}
	
	// 영화 선택
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		Choice ch=(Choice)obj;

	}

	// 확인 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)(obj);
		
		// 확인 버튼을 누르면 
		if(bt==bt_confirm){
			System.out.println("확인 누름");
			setMovieID();
			this.setVisible(false);
		}

		
		// 취소 버튼을 누르면
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
		}
	}

}

