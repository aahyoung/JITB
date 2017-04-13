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

public class DailySales extends JPanel implements ActionListener{

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

	//SalesTable salesTable;

	public DailySales() {

		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_date = new JFXPanel();

		table = new JTable();
		bt = new JButton("조회");
		scroll = new JScrollPane(table);
		movie = new JLabel();
		snack = new JLabel();

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
			getTable();
			//this.setVisible(false);
		}
	}
	
	public void getTable() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		LocalDate date = today.getValue();
		System.out.println(date.toString());

		StringBuffer sql = new StringBuffer();
		sql.append("select a.NAME as 판매상품명, d.BUY_MOVIE_ID as 상품ID, d.SALES_QT, d.sales_tot, d.SALES_TIME");
		sql.append(" from movie a, START_TIME b, SEAT c, BUY_MOVIE d");
		sql.append(" where a.MOVIE_ID = b.MOVIE_ID");
		sql.append(" and b.START_TIME_ID = c.START_TIME_ID");
		sql.append(" and c.SEAT_ID = d.SEAT_ID");
		sql.append(" and to_char(d.SALES_TIME, 'yyyy-mm-dd')= ?");
		sql.append(" union all select e.name as 상품명, g.buy_snack_id, g.sales_qt, g.SALES_TOT, g.SALES_TIME");
		sql.append(" from TOP_OPT e, SUB_OPT f, buy_snack g");
		sql.append(" where e.TOP_OPT_ID=f.TOP_OPT_ID");
		sql.append(" and f.sub_opt_id=g.SUB_OPT_ID");
		sql.append(" and to_char(g.SALES_TIME, 'yyyy-mm-dd')= ?");
		sql.append(" order by sales_time asc");


		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, today.getValue().toString());
			pstmt.setString(2, today.getValue().toString());
			
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
