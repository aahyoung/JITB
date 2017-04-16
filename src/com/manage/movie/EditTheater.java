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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.DBManager;

public class EditTheater extends JDialog implements ActionListener, ItemListener{
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
	
	ArrayList<MovieItem> movie=new ArrayList<MovieItem>();
	
	// 부모 패널
	TheaterMain theaterMain;
	
	// 표시하기 위한 string 변환
	String[] start_time=new String[7];
	
	public EditTheater(TheaterMain theaterMain) {
		this.theaterMain=theaterMain;
		
		// DB 연동
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("현재 영화관에 상영할 영화를 선택해주세요.");
		ch_movie=new Choice();
		
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
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 현재 상영작들을 choice에 저장
	public void setMovieChoice(){
		
	}
	
	// 관에 영화가 지정되면 theater_operate 테이블에 정보 저장
	public void setTheaterOperate(){
		// 현재 선택된 영화관의 id와 현재 선택한 영화의 movie_id를 이용하여 movie->screening_date->start_time
		// start_time_id를 저장
		
	}
	
	// 관에 영화가 지정되면 seat테이블에 theater_operate_id를 이용하여 정보 저장
	public void setSeat(){
		// 현재 선택된 영화관의 행열 정보를 이용해 좌석 이름 설정
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
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
			
		}

		
		// 취소 버튼을 누르면
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
		}
	}
}
