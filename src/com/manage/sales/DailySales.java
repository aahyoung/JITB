/*
당일 별 영화관 총 매출을 보여주는 패널
 */

package com.manage.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jitb.db.DBManager;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DailySales extends JPanel implements ActionListener {

	DBManager manager;
	private Connection con;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	
	DailyChartPanel dailyChart;

	private JPanel p_north, p_center, p_south, p_west, p_east;
	private JTable table;
	private JButton bt;
	private JScrollPane scroll;
	private JLabel tot_sal;
	private DatePicker today;
	private JFXPanel p_date;

	DailySalesTable salesTable;

	public DailySales() {

		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_date = new JFXPanel();
		p_west = new JPanel();
		p_east = new JPanel();

		table = new JTable();
		bt = new JButton("조회");
		scroll = new JScrollPane(table);
		

		tot_sal = new JLabel("<html>영화 당일 매출: "+ " 원 "
				+"<br>식품 당일 매출: "+ "원"
				+"<br>총 당일 매출: "+ "원</html>");

		p_south.add(tot_sal);

		// size조정
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));
		p_south.setPreferredSize(new Dimension(1000, 100));
		
		p_north.setBackground(Color.pink);
		p_south.setBackground(Color.pink);

		// center에 붙이기
		p_center.setLayout(new BorderLayout());
		p_center.add(p_east, BorderLayout.EAST);
		p_center.add(p_west, BorderLayout.WEST);
		
		p_west.add(scroll);

		p_west.setPreferredSize(new Dimension(500, 500));
		p_east.setPreferredSize(new Dimension(500, 500));

		// datepicker 패널
		p_date.setLayout(new BorderLayout());
		p_north.add(p_date);
		p_north.add(bt);

		// 패널들 붙이기
		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		bt.addActionListener(this);
		
		setVisible(true);
		setPreferredSize(new Dimension(1000, 650));

		createCalendar();
		connect();
	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
	
	//버튼을 누르면 테이블 조회
	public void actionPerformed(ActionEvent e) {
		//graph();
		p_north.repaint();
		Object obj = e.getSource();
		if(obj==bt){
			table.setModel(salesTable = new DailySalesTable(con, today));
			table.updateUI();
			salesTable.getTable();
			
			int salesTotal = salesTable.salesTotal;
			int movieTotal = salesTable.movieTotal;
			int snackTtoal = salesTable.snackTotal;
			
			tot_sal.setText("<html>영화 당일 매출: "+ movieTotal+" 원"
					+"<br>식품 당일 매출: "+ snackTtoal+" 원"
					+"<br>총 당일 매출: "+ salesTotal+" 원</html>");	
		}
	}
	
	// 그래프 부르기!
	public void graph() {
		//dailyChart.createChart().repaint();
		//dailyChart.createChart().updateUI();
		p_west.removeAll();
		//p_center.add(dailyChart.createChart());
		p_west.revalidate();	
		
	}

	public void createCalendar() {
		VBox vbox = new VBox(20);
		Scene scene = new Scene(vbox, 180, 60);
		today = new DatePicker();
		today.setValue(LocalDate.now());
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(today, 1, 0);
		vbox.getChildren().add(gridPane);

		p_date.setScene(scene);
	}
}
