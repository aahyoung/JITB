package com.manage.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.jitb.db.DBManager;

public class delMain{
	String name;
	int row,id;
	Connection con;
	DBManager manager;
	JTable table;
	TablePanel tablepanel;
	
	public delMain(Connection con,JTable table, String name, int row,int id){
		this.con=con;
		this.id=id;
		this.name=name;
		this.row=row;
		this.table=table;
		delete();
	}
	public void delete(){
	PreparedStatement pstmt = null;
	StringBuffer sql=new StringBuffer();
	sql.append("delete from ");
	sql.append(name);
	sql.append(" where ");
	sql.append(name+"_id="+id);
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
	table.setModel(tablepanel=new TablePanel(con,name));
	}
}
