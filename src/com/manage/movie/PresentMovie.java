package com.manage.movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JPanel;

import com.jitb.db.DBManager;

/*
 * ���� ������ ����� �г�
 * */

public class PresentMovie extends JPanel{
	// DB ����
	DBManager manager;
	Connection con;
	
	MovieItem movieItem;
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}	
	
	// ���� ���� id ����, ���� ����Ʈ ��������
	public void getMovieList(){
		PreparedStatement pstmt_present=null;	// ���� �� ��ȭ ��� id��
		ResultSet rs_present=null;
		
		
	}
}
