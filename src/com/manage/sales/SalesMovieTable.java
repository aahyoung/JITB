package com.manage.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class SalesMovieTable extends AbstractTableModel {

	Connection con;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();

	String movieName;

	public SalesMovieTable(String movieName, Connection con) {
		this.movieName=movieName;
		this.con=con;
		
		columName.add("상품명");
		columName.add("total");		
		columName.add("time");
		columName.add("할인방법");

	}

	public void getTable() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select a.name as 상품명, d.TOTAL_PRICE as total, d.ORDER_TIME as time, f.name as 할인방법");
		sql.append(" FROM movie a inner join product b on a.MOVIE_ID=b.MOVIE_ID");
		sql.append(" inner join buy_seat c on b.PRODUCT_ID=c.PRODUCT_ID");
		sql.append(" inner join order_movie d on c.ORDER_ID=d.ORDER_ID");
		sql.append(" full outer join movie_discount_history e on e.ORDER_ID=d.ORDER_ID");
		sql.append(" full outer join discount_type f on f.DISCOUNT_TYPE_ID=e.DISCOUNT_TYPE_ID");
		sql.append(" where a.name = '" + movieName + "'");
		sql.append(" order by time");

		
		try {
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();
			
			columName.removeAll(columName);
			data.removeAll(data);

			/*
			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				columName.add(meta.getColumnName(i));
				System.out.println("컬럼 출력하자"+columName);
			}*/
			
			while (rs.next()) {
				Vector<String> vec = new Vector<String>();

				vec.add(rs.getString("상품명"));
				vec.add(rs.getString("total"));
				vec.add(rs.getString("time"));
				vec.add(rs.getString("할인방법"));

				data.add(vec);
				//System.out.println(data+"를 담았다");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return columName.size();
	}

	public String getColumnName(int col) {
		return columName.get(col);
	}

	public Object getValueAt(int row, int col) {
		//System.out.println("row,col정보 출력"+data.elementAt(row).elementAt(col));
		return data.elementAt(row).elementAt(col);
	}

}
