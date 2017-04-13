/*
일별 영화관 매출액을 보여주는 테이블
 */
package com.manage.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


public class SalesTable extends AbstractTableModel {

	Connection con;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();

	public SalesTable(Connection con) {
		this.con = con;
		getList();
	}

	public void getList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select a.NAME as 판매상품명, d.BUY_MOVIE_ID as 상품ID, d.SALES_QT, d.sales_tot, d.SALES_TIME");
		sql.append(" from movie a, START_TIME b, SEAT c, BUY_MOVIE d");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.START_TIME_ID = c.START_TIME_ID");
		sql.append(" and c.SEAT_ID = d.SEAT_ID");
		sql.append(" and to_char(d.SALES_TIME, 'yyyymmdd')= 20170410");
		sql.append(" union all select e.name as 상품명, g.buy_snack_id, g.sales_qt, g.SALES_TOT, g.SALES_TIME");
		sql.append(" from TOP_OPT e, SUB_OPT f, buy_snack g");
		sql.append(" where e.TOP_OPT_ID=f.TOP_OPT_ID");
		sql.append(" and f.sub_opt_id=g.SUB_OPT_ID");
		sql.append(" and to_char(g.SALES_TIME, 'yyyymmdd')= 20170410");
		sql.append(" order by sales_time asc");


		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			// 요일 바꿀 때마다 테이블 초기화 할 것임 현재는 x
			// columName.removeAll(columName);
			// data.removeAll(data);

			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				columName.add(meta.getColumnName(i));
			}

			while (rs.next()) {
				Vector<String> vec = new Vector<String>();
				vec.add(rs.getString("판매상품명"));
				vec.add(rs.getString("상품ID"));
				vec.add(rs.getString("sales_qt"));
				vec.add(rs.getString("SALES_TOT"));
				vec.add(rs.getString("SALES_TIME"));

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
		return data.get(row).get(col);
	}

}
