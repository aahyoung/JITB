package com.manage.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class WeeklySalesPanel extends JPanel {

	Connection con;
	JFreeChart barChart;
	DefaultCategoryDataset dataSet;

	ArrayList<ArrayList> list = new ArrayList<ArrayList>();

	public WeeklySalesPanel(Connection con) {
		this.con = con;
	}

	public void getData(String month, String week) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append("select 'movie' as type, sum(g.PRICE) as price, TO_CHAR(h.ORDER_TIME,'W') as week");
		sql.append(" from BUY_MOVIE f, movie_price g, ORDER_MOVIE h");
		sql.append(" where f.TYPE_ID = g.TYPE_ID");
		sql.append(" and f.ORDER_ID = h.ORDER_ID");
		sql.append(" and TO_CHAR(h.ORDER_TIME,'mm') = '" + month + "'");
		sql.append(" and to_char(h.ORDER_TIME, 'w') = '" + week + "'");
		sql.append(" group by TO_CHAR(h.ORDER_TIME,'W'), 'movie'");
		sql.append(" union all select 'snack' as type, sum(i.PRICE) as price, TO_CHAR(k.ORDER_TIME,'W') as week");
		sql.append(" from SUB_OPT i, BUY_SNACK j, ORDER_SNACK k");
		sql.append(" where k.ORDER_ID= j.ORDER_ID");
		sql.append(" and j.SUB_OPT_ID=i.SUB_OPT_ID");
		sql.append(" and TO_CHAR(k.ORDER_TIME,'mm') = '" + month + "'");
		sql.append(" and to_char(k.ORDER_TIME, 'w') = '" + week + "'");
		sql.append(" group by TO_CHAR(k.ORDER_TIME,'W'), 'snack'");

		System.out.println(sql);

		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			ArrayList data = new ArrayList();

			while (rs.next()) {
				if (rs.getString("type").equals("movie")) {
					data.add(rs.getInt("price"));
					System.out.println("영화의 가격은"+rs.getInt("price"));
				} else if (rs.getString("type").equals("snack")) {
					data.add(rs.getInt("price"));
					System.out.println("식품의 가격은"+rs.getInt("price"));
				}
				list.add(data);
				System.out.println(list);
			}
			createChart();

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

	public void createChart() {
		
		JFreeChart barChart = ChartFactory.createBarChart("abcd", "weekly of sales",  "price", createDataset());
	      
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		//setContentPane(chartPanel);
	}

	private CategoryDataset createDataset() {

		String fiat = "Food";
		String audi = "Snak";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < list.size(); i++) {
			ArrayList data = list.get(i);
			// dataset.addValue(list.get(), "영화" , "1주차" );
		}

		return dataset;
	}

}
