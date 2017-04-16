/*
당일 별 영화관 총 매출을 보여주는 패널
 */

package com.manage.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DailySales extends JPanel implements ActionListener {

	DBManager manager;
	private Connection con;

	Vector<String> columName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();

	private JPanel p_north, p_center, p_south;
	private JTable table;
	private JButton bt;
	private JScrollPane scroll;
	private JLabel movie, snack;
	private DatePicker today;
	private JFXPanel p_date;

	SalesTable salesTable;

	public DailySales() {

		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_date = new JFXPanel();

		table = new JTable();
		bt = new JButton("조회");
		scroll = new JScrollPane(table);
		movie = new JLabel("총 영화 매출 : "+"\n");
		snack = new JLabel("총 식품 매출 : ");

		p_south.add(movie);
		p_south.add(snack);

		p_north.setBackground(Color.yellow);
		p_south.setBackground(Color.orange);

		// size조정
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));
		p_south.setPreferredSize(new Dimension(1000, 100));

		// center에 붙이기
		p_center.setLayout(new BorderLayout());
		p_center.add(scroll);

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

		// init();
		//getList();
		createCalendar();
		connect();
		//getUpList();

	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
	
	//public void getUpList(){
	//	table.setModel(salesTable = new SalesTable(con));
	//	System.out.println(today.getValue().toString());
	//	table.updateUI();
	//}

	//버튼을 누르면 테이블 조회
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==bt){
			//getTable();
			table.setModel(salesTable = new SalesTable(con, today));
			table.updateUI();
			salesTable.getTable();
		}
	}
	
	public void getTotal() {
		movie = new JLabel("영화 총 매출: "+ movie+" 원 \n");
		snack = new JLabel("식품 총 매출 : "+ snack+" 원");
		
		
	}

	public void createCalendar() {
		VBox vbox = new VBox(20);
		Scene scene = new Scene(vbox, 300, 60);
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
