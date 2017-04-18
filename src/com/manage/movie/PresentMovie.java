package com.manage.movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JPanel;

import com.jitb.db.DBManager;

/*
 * 현재 상영작을 출력할 패널
 * */

public class PresentMovie extends JPanel{
	// DB 연동
	DBManager manager;
	Connection con;
	
	MovieItem movieItem;
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}	
	
	// 현재 상영작 id 저장, 상영작 리스트 가져오기
	public void getMovieList(){
		PreparedStatement pstmt_present=null;	// 현재 상영 영화 목록 id들
		ResultSet rs_present=null;
		
		
	}
}
