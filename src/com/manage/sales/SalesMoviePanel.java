package com.manage.sales;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.jitb.db.DBManager;

public class SalesMoviePanel extends JDialog {

	Connection con;

	private JPanel p_center;
	private JTable table;
	private JScrollPane scroll;

	private String movieName;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();

	TableModel model;
	int cols;

	public SalesMoviePanel(String movieName, Connection con) {

		this.movieName = movieName;
		this.con = con;

		p_center = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);

		p_center.add(scroll);
		add(p_center);

		p_center.setLayout(new BorderLayout());
		scroll.setPreferredSize(new Dimension(600, 500));

		columName.add("상품명");
		columName.add("total");
		columName.add("할인받은금액");
		columName.add("time");

		getTable();
		// 테이블 모델을 JTable에 적용
		model = new AbstractTableModel() {

			public int getRowCount() {
				return data.size();
			}

			public int getColumnCount() {
				return cols;
			}

			public Object getValueAt(int row, int col) {

				return data.get(row).get(col);
			}
		};

		table.setModel(model);

		setSize(600, 500);
		setVisible(true);

	}

	public void getTable() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select a.name as 상품명, e.TOTAL_PRICE as total,");
		sql.append(" abs(e.TOTAL_PRICE-e.ORDER_PRICE) as 할인받은금액, e.ORDER_TIME as time");
		sql.append(" FROM movie a, product b, buy_seat c, movie_price d, order_movie e");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.PRODUCT_ID=c.PRODUCT_ID");
		sql.append(" and d.TYPE_ID = c.TYPE_ID");
		sql.append(" and e.ORDER_ID = c.ORDER_ID");
		sql.append(" and a.name = '" + movieName + "'");
		sql.append(" order by time");

		try {
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();
			cols = rs.getMetaData().getColumnCount();
			columName.removeAll(columName);
			data.removeAll(data);

			while (rs.next()) {
				Vector<String> vec = new Vector<String>();

				vec.add(rs.getString("상품명"));
				vec.add(rs.getString("total"));
				vec.add(rs.getString("할인받은금액"));
				vec.add(rs.getString("time"));

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

}
