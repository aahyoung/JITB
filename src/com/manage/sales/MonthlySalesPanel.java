package com.manage.sales;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.jitb.date.DateUtil;

public class MonthlySalesPanel {
	
	Connection con;
	JFreeChart barChart;
	DefaultCategoryDataset dataSet;
	String getMonth;
	int week;

	ArrayList<String[]> list = new ArrayList<String[]>();

	public MonthlySalesPanel(Connection con) {
		this.con = con;
		createChart();
	}
	

	public void getData(String year) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		try {

			for (int i = 1; i <= 12; i++) {
				String[] table = new String[2];

				// ����ɶ����� �������ֱ�
				sql.delete(0, sql.length());
				
				String getMonth = DateUtil.getDateStr(Integer.toString(i));

				sql.append("select 'movie' as type, sum(d.TOTAL_PRICE) as price, TO_CHAR(d.ORDER_TIME,'mm') as month");
				sql.append(" FROM movie a, product b, buy_seat c, order_movie d");
				sql.append(" where a.MOVIE_ID = b.MOVIE_ID and b.PRODUCT_ID=c.PRODUCT_ID");
				sql.append(" and d.ORDER_ID = c.ORDER_ID");
				sql.append(" and c.PRODUCT_ID=b.PRODUCT_ID");
				sql.append(" and b.MOVIE_ID=a.MOVIE_ID");
				sql.append(" and TO_CHAR(d.ORDER_TIME,'yyyy') = '" + year + "'");
				sql.append(" and TO_CHAR(d.ORDER_TIME,'mm') = '" + getMonth + "'");
				sql.append(" group by 'type', TO_CHAR(d.ORDER_TIME,'mm')");
				sql.append(" union all select 'snack' as type, sum(f.TOTAL_PRICE) as total, TO_CHAR(f.ORDER_TIME,'mm') as month");
				sql.append(" from SUB_OPT d, BUY_SNACK e, ORDER_SNACK f");
				sql.append(" where e.ORDER_SNACK_ID = f.ORDER_SNACK_ID");
				sql.append(" and e.SUB_OPT_ID=d.SUB_OPT_ID");
				sql.append(" and TO_CHAR(f.ORDER_TIME,'yyyy') = '" + year + "'");
				sql.append(" and TO_CHAR(f.ORDER_TIME,'mm') = '" + getMonth + "'");
				sql.append(" group by 'type', TO_CHAR(f.ORDER_TIME,'mm')");
				sql.append(" order by month asc");
				

				//System.out.println(sql);

				pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					//DB���ڵ� �߿��� �ְ� ���� �����͵� ��, ������ ���� 04�� ������ ������ DateUtil���!
					if (rs.getString("month").equals(getMonth)) {
						//System.out.println("month�� ���� ���� month��" + i);
						
						//type�� ���� ������ �����ؼ� ���
						if (rs.getString("type").equals("movie")) {
							table[0] = rs.getString("price");
							//System.out.println("price ��Ҵ�"+rs.getString("price"));
						} else if (rs.getString("type").equals("snack")) {
							table[1] = rs.getString("price");
							//System.out.println("price ��Ҵ�"+rs.getString("price"));
						}
					} else {
						//DB���ڵ� �߿��� ���� �ְ� ������ (=�������0�̸�) 0���� ���
						table[0] = Integer.toString(0);
						table[1] = Integer.toString(0);
					}
				}
				Collections.addAll(list, table);
			}
			//System.out.println("���������� ������ �׷����� ������ Vector�� " + list.size());
			
			/* LIST�� �� ������ִ��� Ȯ���ϱ�
			for (int a = 0; a < list.size(); a++) {
				System.out.println(list.get(a)[0] + "," + list.get(a)[1]);
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
		
		String movie = "MOVIE";
		String snack = "SNACK";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String[] shortMonths = new DateFormatSymbols().getShortMonths();

		for (int a = 0; a < list.size(); a++) {
			//System.out.println(list.get(a)[0] + "," + list.get(a)[1]);

			if (list.get(a)[0] != null) {
				dataset.addValue(Integer.parseInt(list.get(a)[0]), movie, shortMonths[a]);
			} else {
				dataset.addValue(0, movie, shortMonths[a]);
			}
			
			if (list.get(a)[1] != null) {
				dataset.addValue(Integer.parseInt(list.get(a)[1]), snack, shortMonths[a]);
			} else {
				dataset.addValue(0, snack, shortMonths[a]);
			}
		}
		return dataset;
	}

	public ChartPanel createChart() {

		JFreeChart barChart = ChartFactory.createBarChart("", "Monthly revenue", "Sales revenue", createDataset());

		Font font = new Font("Dodum", Font.PLAIN, 15);
		Font font_legend = new Font("Dodum", Font.BOLD, 15);
		Font font_title = new Font("Dodum", Font.BOLD, 15);
		
		//plot���κ��� ������, ������ ��Ʈ����
		CategoryPlot plot = barChart.getCategoryPlot();
		
		//������ ī�װ� �۾� �ٲٱ�
		CategoryAxis category = plot.getDomainAxis();
		category.setTickLabelFont(font);
		category.setLabelFont(font_title);
		
		//������ value �۾� �ٲٱ�
		ValueAxis value = plot.getRangeAxis();
		value.setTickLabelFont(font);
		value.setLabelFont(font_title);
		
		//��Ʈ �ؿ� movie, snack lenged �ٲٱ�
		LegendTitle legend = barChart.getLegend();
		legend.setItemFont(font_legend);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
		
		return chartPanel;
	}


}
