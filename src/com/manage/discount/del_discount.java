package com.manage.discount;

	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;

	import javax.swing.JOptionPane;
	import javax.swing.JTable;

	import com.jitb.db.DBManager;

	public class del_discount{
		String name;
		int row,id;
		Connection con;
		DBManager manager;
		JTable table_up;
		table_model tablemodel;
		
		public del_discount(Connection con,JTable table_up, String name, int row,int id){
			this.id=id;
			this.con=con;
			this.name=name;
			this.row=row;
			this.table_up=table_up;
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
			pstmt.executeUpdate();
			
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
		table_up.setModel(tablemodel=new table_model(con,name));
		}
	}

