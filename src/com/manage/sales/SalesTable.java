/*
�Ϻ� ��ȭ�� ������� �����ִ� ���̺�
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
		sql.append("select a.NAME as ��ǰ��, d.BUY_MOVIE_ID as ��ǰID, d.SALES_QT, d.sales_tot, d.SALES_TIME");
		sql.append(" from movie a, START_TIME b, SEAT c, BUY_MOVIE d");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.START_TIME_ID = c.START_TIME_ID");
		sql.append(" and c.SEAT_ID = d.SEAT_ID");
		sql.append(" and to_char(d.SALES_TIME, 'yyyy-mm-dd')=?");
		sql.append(" union all select f.name as ��ǰ��, g.buy_snack_id as ��ǰID, g.sales_qt, g.SALES_TOT, g.SALES_TIME");
		sql.append(" from SUB_OPT f, buy_snack g ");
		sql.append(" where f.sub_opt_id=g.SUB_OPT_ID");
		sql.append(" and to_char(g.SALES_TIME, 'yyyy-mm-dd')=?");
		sql.append(" order by sales_time asc");

		//System.out.println(today.getValue().toString());
		System.out.println(sql.toString());
		String day = today.getValue().toString();
		System.out.println("���"+day);

		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, day);
			pstmt.setString(2, day);
			
			rs = pstmt.executeQuery();
			System.out.println(rs);

			// ���� �ٲ� ������ ���̺� �ʱ�ȭ �� ���� ����� x
			columName.removeAll(columName);
			data.removeAll(data);

			ResultSetMetaData meta = rs.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				columName.add(meta.getColumnName(i));
				System.out.println(meta.getColumnName(i));
			}
			
			System.out.println(rs);

			while(rs.next()) {
				System.out.println(rs.next());
				Vector<String> vec = new Vector<String>();
				vec.add(rs.getString("��ǰ��"));
				vec.add(rs.getString("��ǰID"));
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
		System.out.println("���ڵ��� ������"+data.size());
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
