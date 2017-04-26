package com.manage.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.jitb.db.DBManager;

public class Update_Model{
	String name,val;
	Object value;
	int row,col,id;
	Connection con;
	DBManager manager;
	TablePanel tablepanel;
	
	public Update_Model(Connection con, String name, int row,int col,Object value,int id){
		this.id=id;
		this.con=con;
		this.name=name;
		this.row=row;
		this.col=col;
		this.value=value;
		init();
		update();
	}
	public void update(){
	PreparedStatement pstmt = null;
	StringBuffer sql=new StringBuffer();
	sql.append("update "+name+" set "+val+"="+"'"+value.toString()+"'"+" where "+name+"_id="+id);
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
	}
	
	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
		if(name.equals("combo"))
		{
			if(col==1){
				val="name";
			}
			else if(col==2){
				val="img";
			}
			else if(col==3){
				val="price";
			}
		}
		else if(name.equals("combo_list")){
			if(col==1){
				val="amount";
			}
			else if(col==2){
				val="combo_id";
			}
			else if(col==3){
				val="top_opt_size_id";
			}
		}
		else if(name.equals("top_opt")){
			if(col==1){
				val="name";
			}
			else if(col==2){
				val="img";
			}
		}
		else if(name.equals("top_opt_size")){
			if(col==1){
				val="top_opt_id";
			}
			else if(col==2){
				val="opt_size";
			}
		}
		else if(name.equals("sub_opt")){
			if(col==0){
				val="sub_opt_id";
			}
			else if(col==1){
				val="name";
			}
			else if(col==2){
				val="price";
			}
			else if(col==3){
				val="img";
			}
			else if(col==4){
				val="stock";
			}
			else if(col==5){
				val="top_opt_size_id";
			}
		}
	}
}
