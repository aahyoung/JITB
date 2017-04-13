package com.manage.movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MovieTableModel extends AbstractTableModel{
	Vector<String> columnName=new Vector<String>();
	Vector<Vector> data=new Vector<Vector>();
	
	Connection con;
	
	public MovieTableModel(Connection con) {
		this.con=con;
		
		columnName.addElement("��ȣ");
		columnName.addElement("����");
		columnName.addElement("����");
		columnName.addElement("�ֿ� ���");
		columnName.addElement("�� ����");
		columnName.addElement("�� ����");
		columnName.addElement("�� �ð�");
		
		getMovieList();
	}
	
	// ���� �����ϴ� ��ȭ�� DB���� ��������
	public void getMovieList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select movie_id, name, director, main_actor, start_date, end_date, run_time from movie";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			while(rs.next()){
				Vector vector=new Vector();
				vector.add(rs.getInt("movie_id"));
				vector.add(rs.getString("name"));
				vector.add(rs.getString("director"));
				vector.addElement(rs.getString("main_actor"));
				vector.add(rs.getString("start_date"));
				vector.add(rs.getString("end_date"));
				vector.add(rs.getInt("run_time"));
				
				data.add(vector);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columnName.size();
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}
	
}
