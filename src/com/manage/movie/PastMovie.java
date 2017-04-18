package com.manage.movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JPanel;

import com.jitb.db.DBManager;

/*
 * 과거 상영작을 출력할 패널
 * */

public class PastMovie extends JPanel{
	// DB 연동
	DBManager manager;
	Connection con;
	
	MovieItem movieItem;
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	public void getMovieList(){
		PreparedStatement pstmt_past=null;		// 과거 상영 영화 목록 id들
		ResultSet rs_past=null;
	}
}
