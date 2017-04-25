/*
일별 영화관 매출액을 보여주는 테이블
 */
package com.manage.sales;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class DailySalesTable extends AbstractTableModel {

	Connection con;
	DatePicker today;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	
	//총매출, 영화매출, 스낵매출을 DailySales에 보내기위한 변수 선언
	int salesTotal;
	int movieTotal;
	int snackTotal;
	

	public DailySalesTable(Connection con, DatePicker today) {
		this.con = con;
		this.today = today;
		getTable();
	}

	public void getTable() {
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		
		LocalDate date = today.getValue();
		//System.out.println(date.toString());
		//System.out.println(con);

		StringBuffer sql = new StringBuffer();
		sql.append("select 'movie' as type, a.name as 상품명, e.TOTAL_PRICE as total, e.ORDER_TIME as time");
		sql.append(" FROM movie a, product b, buy_seat c, movie_price d, order_movie e");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.PRODUCT_ID=c.PRODUCT_ID");
		sql.append(" and d.TYPE_ID = c.TYPE_ID");
		sql.append(" and e.ORDER_ID = c.ORDER_ID");
		sql.append(" and to_char(e.order_time, 'yyyy-mm-dd')= ?");
		sql.append(" union all select 'snack' as type, d.name as 상품명, f.TOTAL_PRICE as total, f.ORDER_TIME as time");
		sql.append(" from SUB_OPT d, BUY_SNACK e, ORDER_SNACK f ");
		sql.append(" where e.ORDER_SNACK_ID = f.ORDER_SNACK_ID");
		sql.append(" and e.SUB_OPT_ID=d.SUB_OPT_ID ");
		sql.append(" and TO_CHAR(f.ORDER_TIME,'yyyy-mm-dd')= ?");
		sql.append(" order by time asc");
		System.out.println(sql);
	

		String day = today.getValue().toString();
		System.out.println("출력"+day);

		try {
			pstmt=con.prepareStatement(sql.toString());
			
			pstmt.setString(1, day);
			pstmt.setString(2, day);
			
			rs = pstmt.executeQuery();

			// 요일 바꿀 때마다 테이블 초기화 할 것임 현재는 x
			columName.removeAll(columName);
			data.removeAll(data);

			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				columName.add(meta.getColumnName(i));
			}
			
			int total=0;
			int movie =0;
			int snack=0;

			while(rs.next()) {
				Vector<String> vec = new Vector<String>();
				vec.add(rs.getString("type"));
				vec.add(rs.getString("상품명"));
				vec.add(rs.getString("total"));
				vec.add(rs.getString("time"));
				
				if(rs.getString("type").equals("movie")){
					movie+=rs.getInt("total");

				} else if(rs.getString("type").equals("snack")) {
					snack+=rs.getInt("total");
				}
				
				total+= rs.getInt("total");
				
				movieTotal = movie;
				snackTotal = snack;
				salesTotal=total;

				data.add(vec);		
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
		return data.elementAt(row).elementAt(col);
	}

}
