package com.manage.sales;

import java.awt.Color;
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
	//ArrayList<Vector> list = new ArrayList<Vector>();

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
				//Vector vec = new Vector();
				String[] table = new String[2];

				// 실행될때마다 삭제해주기
				sql.delete(0, sql.length());

				sql.append("select 'movie' as type, sum(g.PRICE) as price, TO_CHAR(h.ORDER_TIME,'W') as week");
				sql.append(" from BUY_MOVIE f, movie_price g, ORDER_MOVIE h");
				sql.append(" where f.TYPE_ID = g.TYPE_ID");
				sql.append(" and f.ORDER_ID = h.ORDER_ID");
				sql.append(" and TO_CHAR(h.ORDER_TIME,'mm') = '" + month + "'");
				sql.append(" and to_char(h.ORDER_TIME, 'W') = '" + i + "'");
				sql.append(" group by TO_CHAR(h.ORDER_TIME,'W'), 'movie'");
				sql.append(" union all select 'snack' as type, sum(i.PRICE) as price,");
				sql.append(" TO_CHAR(k.ORDER_TIME,'W') as week");
				sql.append(" from SUB_OPT i, BUY_SNACK j, ORDER_SNACK k");
				sql.append(" where k.ORDER_ID= j.ORDER_ID");
				sql.append(" and j.SUB_OPT_ID=i.SUB_OPT_ID");
				sql.append(" and TO_CHAR(k.ORDER_TIME,'mm') = '" + month + "'");
				sql.append(" and to_char(k.ORDER_TIME, 'W') = '" + i + "'");
				sql.append(" group by TO_CHAR(k.ORDER_TIME,'W'), 'snack'");
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
							//vec.add(rs.getString("price"));
						} else if (rs.getString("type").equals("snack")) {
							table[1] = rs.getString("price");
							//vec.add(rs.getString("price"));
						}
					} else {
						table[0] = Integer.toString(0);
						table[1] = Integer.toString(0);
						//vec.add(0);//0
						//vec.add(0);//1
					}
				}
				System.out.println(Arrays.toString(table));
				//list.addAll(Arrays.asList(table));
				Collections.addAll(list, table);
			}

			System.out.println(list);
			System.out.println("최종적으로 구성된 그래프용 이차원 Vector는 " + list.size());
			for(int a=0; a<list.size(); a++){
				for(int b=0; b<list.get(a).length; b++){
					String[] str = list.get(a);
					System.out.println(str[b]+",");
				}
				
			}

			/*
			for (int a = 0; a < list.size(); a++) {
				Vector vc=list.get(a);
				System.out.print(vc.get(0) + ", " + vc.get(1));
				System.out.println("-------------------------------");
			}
			*/
			
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
		final String movie = "MOVIE";
		final String snack = "SNACK";

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);			
		}
		
		dataset.addValue(null, "SNACK", "1");
		dataset.addValue(300, "SNACK", "1");

		dataset.addValue(200, "MOVIE", "2");
		dataset.addValue(300, "SNACK", "2");

		return dataset;

	}

	public ChartPanel createChart() {

		JFreeChart barChart = ChartFactory.createBarChart("", "weekly revenue", "Sales revenue", createDataset());

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
		setBackground(Color.ORANGE);
		return chartPanel;
	}

}
