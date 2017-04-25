/*
일별 영화관 매출액을 보여주는 테이블
 */
package com.manage.sales;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DailySalesTable extends AbstractTableModel {

	Connection con;
	DatePicker today;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Vector<String[]> chart = new Vector<String[]>();

	// 총매출, 영화매출, 스낵매출을 DailySales에 보내기위한 변수 선언
	int salesTotal;
	int movieTotal;
	int snackTotal;

	LocalDate date;
	String day;

	public DailySalesTable(Connection con, DatePicker today) {
		this.con = con;
		this.today = today;
		getTable();
		getChart();
	}

	public void getTable() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		date = today.getValue();

		StringBuffer sql = new StringBuffer();
		sql.append("select 'movie' as type, a.name as 상품명, e.TOTAL_PRICE as total, ");
		sql.append(" abs(e.TOTAL_PRICE-e.ORDER_PRICE) as 할인받은금액, e.ORDER_TIME as time ");
		sql.append(" FROM movie a, product b, buy_seat c, movie_price d, order_movie e");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.PRODUCT_ID=c.PRODUCT_ID");
		sql.append(" and d.TYPE_ID = c.TYPE_ID");
		sql.append(" and e.ORDER_ID = c.ORDER_ID");
		sql.append(" and to_char(e.order_time, 'yyyy-mm-dd')= ?");
		sql.append(" union all select 'snack' as type, f.name as 상품명, h.TOTAL_PRICE as total, ");
		sql.append(" abs(h.TOTAL_PRICE-h.ORDER_PRICE) as 할인받은금액, h.ORDER_TIME as time");
		sql.append(" from SUB_OPT f, BUY_SNACK g, ORDER_SNACK h");
		sql.append(" where g.ORDER_SNACK_ID = h.ORDER_SNACK_ID");
		sql.append(" and f.SUB_OPT_ID = f.SUB_OPT_ID ");
		sql.append(" and TO_CHAR(h.ORDER_TIME,'yyyy-mm-dd')= ?");
		sql.append(" order by time asc");
		System.out.println(sql);
		
		
		day = today.getValue().toString();
		// System.out.println("출력"+day);

		try {
			pstmt = con.prepareStatement(sql.toString());

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

			int total = 0;
			int movie = 0;
			int snack = 0;

			while (rs.next()) {
				Vector<String> vec = new Vector<String>();

				vec.add(rs.getString("type"));
				vec.add(rs.getString("상품명"));
				vec.add(rs.getString("total"));
				vec.add(rs.getString("할인받은금액"));
				vec.add(rs.getString("time"));

				if (rs.getString("type").equals("movie")) {
					movie += rs.getInt("total");
				} else if (rs.getString("type").equals("snack")) {
					snack += rs.getInt("total");
				}

				total += rs.getInt("total");

				movieTotal = movie;
				snackTotal = snack;
				salesTotal = total;
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
	

	// chart에 필요한 통계 Data를 얻는곳!
	public void getChart() {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		try {
			String[] chartData = new String[2];

			sql.delete(0, sql.length());

			sql.append("select type, sum(total) as total from dailysales");
			sql.append(" where time = '" + day + "'");
			sql.append(" group by type");
			System.out.println(sql);

			pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("type").equals("movie")) {				
					chartData[0] = rs.getString("total");
				} else if (rs.getString("type").equals("snack")){
					chartData[1] = rs.getString("total");
				} else {
					chartData[0] = Integer.toString(0);
					chartData[1] = Integer.toString(0);
				}
				chart.add(chartData);
			}

			System.out.println("최종적으로 구성된 그래프용 이차원 Vector는 " + chart.size());

			
			 // LIST에 잘 담아져있는지 확인하기 
			for (int a = 0; a < chart.size(); a++) {
			 System.out.println(chart.get(a)[0] + "," + chart.get(a)[1]); 
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

	// 차트 생성하는 곳
	private CategoryDataset createDataset() {		
		String movie = "MOVIE";
		String snack = "SNACK";

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int a = 0; a < chart.size(); a++) {
			//System.out.println(list.get(a)[0] + "," + list.get(a)[1]);

			if (chart.get(a)[0] != null) {
				dataset.addValue(Integer.parseInt(chart.get(a)[0]), movie, day);
			} else {
				dataset.addValue(0, "MOVIE", day);
			}
			
			if (chart.get(a)[1] != null) {
				dataset.addValue(Integer.parseInt(chart.get(a)[1]), snack, day);
			} else {
				dataset.addValue(0, "SNACK", day);
			}
		}
		//System.out.println("dataset :" + dataset);
		return dataset;
	}

	//화면에 차트 띄우기
	public ChartPanel createChart() {

		JFreeChart barChart = ChartFactory.createBarChart("", "Daily revenue", "Sales revenue", createDataset());

		Font font = new Font("Dodum", Font.PLAIN, 15);
		Font font_legend = new Font("Dodum", Font.BOLD, 15);
		Font font_title = new Font("Dodum", Font.BOLD, 15);

		// plot으로부터 가로축, 세로축 폰트변경
		CategoryPlot plot = barChart.getCategoryPlot();

		// 가로축 카테고리 글씨 바꾸기
		CategoryAxis category = plot.getDomainAxis();
		category.setTickLabelFont(font);
		category.setLabelFont(font_title);

		// 세로축 value 글씨 바꾸기
		ValueAxis value = plot.getRangeAxis();
		value.setTickLabelFont(font);
		value.setLabelFont(font_title);

		// 차트 밑에 movie, snack lenged 바꾸기
		LegendTitle legend = barChart.getLegend();
		legend.setItemFont(font_legend);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));

		return chartPanel;
	}

}
