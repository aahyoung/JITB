package com.manage.ticket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MoviePriceModel extends AbstractTableModel{
	Vector<String> columnName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	
	Ticket ticket;
	
	public MoviePriceModel(Ticket ticket) {
		this.ticket = ticket;
		
		columnName.add("아이디");
		columnName.add("종류");
		columnName.add("가격");
		
		selectMoviePrice();
	}
	
	@Override
	public int getColumnCount() {
		return columnName.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}
	
	@Override
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	public void selectMoviePrice(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		data.removeAll(data);
		
		String sql = "select * from movie_price";
		try {
			pstmt = ticket.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getString("type_id"));
				vec.add(rs.getString("type"));
				vec.add(rs.getString("ticket_price"));
				
				data.add(vec);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
