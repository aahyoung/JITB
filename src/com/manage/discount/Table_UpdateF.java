package com.manage.discount;

	import java.sql.Connection;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;
	import javax.swing.JOptionPane;
	import javax.swing.JTable;
	import com.jitb.db.DBManager;

	public class Table_UpdateF {
		
				String name,val,table_name;
				Object value;
				int row,col,id;
				Connection con;
				DBManager manager;
				table_modelF tablemodel;
				
				public Table_UpdateF(Connection con, String name, int row,String col,Object value,int id,String table_name){
					this.id=id;
					this.con=con;
					this.name=name;
					this.row=row;
					this.val=col;
					this.value=value;
					this.table_name=table_name;
					init();
					update();
					
				}
				public void update(){
				PreparedStatement pstmt = null;
				StringBuffer sql=new StringBuffer();
				sql.append("update "+table_name+" set "+val.toLowerCase()+"="+"'"+value.toString()+"'"+" where "+name+"_id="+id);
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
					
				}
}

