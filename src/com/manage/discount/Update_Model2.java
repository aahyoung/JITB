package com.manage.discount;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import com.jitb.db.DBManager;

public class Update_Model2 {
	
			String name,val;
			Object value;
			int row,col,id;
			Connection con;
			DBManager manager;
			table_model tablemodel;
			
			public Update_Model2(Connection con, String name, int row,int col,Object value,int id){
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
				if(name.equals("discount_info"))
				{
					if(col==1){
						val="discount_info_id";
					}
					else if(col==2){
						val="name";
					}
					else if(col==3){
						val="rate";
					}
					else if(col==3){
						val="img";
					}
				}
				else if(name.equals("discount_type")){
					if(col==1){
						val="name";
					}
				}
				else if(name.equals("gift_info")){
					if(col==1){
						val="gift_type_id";
					}
					else if(col==2){
						val="no";
					}
					else if(col==3){
						val="price";
					}
					else if(col==4){
						val="status";
					}
					else if(col==5){
						val="img";
					}
				}
				else if(name.equals("gift_type")){
					if(col==1){
						val="name";
					}
				}
			}
}
