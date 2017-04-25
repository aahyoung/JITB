package com.manage.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JTable;

import com.jitb.db.DBManager;

public class updateMain {
	String name;
	int row;
	Connection con;
	DBManager manager;
	JTable table_up;
	TablePanel tablepanel;
	
	public updateMain(Connection con,JTable table_up, String name){//이름 참고해서 수정해볼것.
		this.con=con;
		this.name=name;
		this.table_up=table_up;
		row=table_up.getSelectedRow();
		init();
		update();
	}
	public void update(){
		StringBuffer sql=new StringBuffer();
		PreparedStatement pstmt=null;
		sql.append("update "+name+" set combo name=?,img=?,price=? where combo_id=?");//쿼리문 넣어서 돌려볼것
		try {
			pstmt.setString(1, table_up.getValueAt(row, 1).toString());
			pstmt.setString(2, table_up.getValueAt(row, 2).toString());
			pstmt.setInt(3, Integer.parseInt(table_up.getValueAt(row, 3).toString()));
			pstmt.setInt(4, row);

			System.out.println(sql);
			
			pstmt=con.prepareStatement(sql.toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
}
