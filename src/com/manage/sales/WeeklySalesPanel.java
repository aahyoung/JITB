package com.manage.sales;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class WeeklySalesPanel extends JPanel {

	Connection con;
	JFreeChart barChart;
	DefaultCategoryDataset dataSet;
	String month;
	int week;

	ArrayList<String[]> list = new ArrayList<String[]>();

	public WeeklySalesPanel(Connection con) {
		this.con = con;
		createChart();
	}

	public void getData(String month, int week) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		System.out.println(sql);

		try {

			for (int i = 1; i <= week; i++) {
				String[] table = new String[2];

				// 실행될때마다 삭제해주기
				sql.delete(0, sql.length());

				sql.append("select 'movie' as type, sum(d.TOTAL_PRICE) as price, TO_CHAR(d.ORDER_TIME,'w') as week");
				sql.append(" FROM movie a, product b, buy_seat c, order_movie d");
				sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
				sql.append(" and b.PRODUCT_ID=c.PRODUCT_ID");
				sql.append(" and d.ORDER_ID = c.ORDER_ID");
				sql.append(" and TO_CHAR(d.ORDER_TIME,'mm') = '" + month + "'");
				sql.append(" and TO_CHAR(d.ORDER_TIME,'W') = '" + i + "'");
				sql.append(" group by 'type', TO_CHAR(d.ORDER_TIME,'W')");
				sql.append(
						" union all select 'snack' as type, sum(f.TOTAL_PRICE) as total, TO_CHAR(f.ORDER_TIME,'W') as week");
				sql.append(" from SUB_OPT d, BUY_SNACK e, ORDER_SNACK f ");
				sql.append(" where e.ORDER_SNACK_ID = f.ORDER_SNACK_ID");
				sql.append(" and e.SUB_OPT_ID=d.SUB_OPT_ID ");
				sql.append(" and TO_CHAR(f.ORDER_TIME,'mm') = '" + month + "'");
				sql.append(" and TO_CHAR(f.ORDER_TIME,'W') = '" + i + "'");
				sql.append(" group by 'type', TO_CHAR(f.ORDER_TIME,'W')");
				sql.append(" order by week asc");
				System.out.println(sql);

				pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					if (rs.getString("week").equals(Integer.toString(i))) {
						System.out.println("week가 같고 같은 week는" + i);

						if (rs.getString("type").equals("movie")) {
							table[0] = rs.getString("price");
						} else if (rs.getString("type").equals("snack")) {
							table[1] = rs.getString("price");
						}
					} else {
						table[0] = Integer.toString(0);
						table[1] = Integer.toString(0);
					}
				}
				System.out.println(Arrays.toString(table));
				Collections.addAll(list, table);
			}
			System.out.println("최종적으로 구성된 그래프용 이차원 Vector는 " + list.size());

			for (int a = 0; a < list.size(); a++) {
				System.out.println(list.get(a)[0] + "," + list.get(a)[1]);

				// for(int b=0; b<list.get(a).length; b++){
				// String[] str = list.get(a);
				// System.out.println(str[b]+",");
				// }
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

	private CategoryDataset createDataset() {
		
		String movie = "MOVIE";
		String snack = "SNACK";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int a = 0; a < list.size(); a++) {
			System.out.println(list.get(a)[0] + "," + list.get(a)[1]);

			if (list.get(a)[0] != null) {
				dataset.addValue(Integer.parseInt(list.get(a)[0]), "MOVIE", Integer.toString(a+1)+" week");
			} else {
				dataset.addValue(0, "MOVIE", Integer.toString(a+1)+" week");
			}
			
			if (list.get(a)[1] != null) {
				dataset.addValue(Integer.parseInt(list.get(a)[1]), "SNACK", Integer.toString(a+1)+" week");
			} else {
				dataset.addValue(0, "SNACK", Integer.toString(a+1)+" week");
			}
		}
		return dataset;
	}

	public ChartPanel createChart() {

		JFreeChart barChart = ChartFactory.createBarChart("", "weekly revenue", "Sales revenue", createDataset());

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
		
		return chartPanel;
	}

}
