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


public class SalesTable extends AbstractTableModel {

	Connection con;
	DatePicker today;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	
	//total을 DailySales에 보내기위한 변수 선언
	int salesTotal;
	

	public SalesTable(Connection con, DatePicker today) {
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
		sql.append("select a.name as 상품명, g.PRICE as total, h.ORDER_TIME");
		sql.append(" from movie a, SCREENING_DATE b, START_TIME c, THEATER_OPERATE d");
		sql.append(" ,SEAT e, BUY_MOVIE f, movie_price g, ORDER_MOVIE h");
		sql.append(" where a.MOVIE_ID=b.MOVIE_ID");
		sql.append(" and b.SCREENING_DATE_ID=c.SCREENING_DATE_ID");
		sql.append(" and c.START_TIME_ID=d.START_TIME_ID");
		sql.append(" and d.THEATER_OPERATE_ID=e.THEATER_OPERATE_ID");
		sql.append(" and e.SEAT_ID=f.SEAT_ID");
		sql.append(" and f.TYPE_ID=g.TYPE_ID");
		sql.append(" and f.ORDER_ID = h.ORDER_ID");
		sql.append(" and to_char(h.order_time, 'yyyy-mm-dd')= ?");
		sql.append(" union all select i.NAME as 상품명, i.PRICE as price, k.ORDER_TIME");
		sql.append(" from SUB_OPT i, BUY_SNACK j, ORDER_SNACK k");
		sql.append(" where k.ORDER_ID= j.ORDER_ID");
		sql.append(" and j.SUB_OPT_ID=i.SUB_OPT_ID");
		sql.append(" and to_char(k.order_time, 'yyyy-mm-dd')= ?");
		sql.append(" order by order_time");
		

		//System.out.println(today.getValue().toString());
		//System.out.println(sql.toString());
		String day = today.getValue().toString();
		//System.out.println("출력"+day);

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

			while(rs.next()) {
				//System.out.println(rs.next());
				Vector<String> vec = new Vector<String>();
				vec.add(rs.getString("상품명"));
				vec.add(rs.getString("total"));
				vec.add(rs.getString("ORDER_TIME"));
				
				total+= rs.getInt("total");
				salesTotal=total;
				System.out.println(total);
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
