package com.manage.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.jitb.db.DBManager;

public class delMain{
	String name;
	int row;
	Connection con;
	DBManager manager;
	JTable table_up;
	TablePanel tablepanel;
	
	public delMain(Connection con,JTable table_up, String name, int row){
		this.con=con;
		this.name=name;
		this.row=row;
		this.table_up=table_up;
		init();
		delete();
	}
	public void delete(){
	PreparedStatement pstmt = null;
	StringBuffer sql=new StringBuffer();
	sql.append("delete from ");
	sql.append(name);
	sql.append(" where ");
	sql.append(name+"_id="+row);
	System.out.println(sql);
	try {
		pstmt = con.prepareStatement(sql.toString());
		int rs = pstmt.executeUpdate();
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	table_up.setModel(tablepanel=new TablePanel(con,name));
	}
	
	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
}
