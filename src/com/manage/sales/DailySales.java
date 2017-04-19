/*
���� �� ��ȭ�� �� ������ �����ִ� �г�
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
	private JLabel sales;
	private DatePicker today;
	private JFXPanel p_date;

	DailySalesTable salesTable;

	public DailySales() {

		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		p_date = new JFXPanel();

		table = new JTable();
		bt = new JButton("��ȸ");
		scroll = new JScrollPane(table);
		
		sales = new JLabel("�� ���� ���� : "+"\n");

		p_north.setBackground(Color.yellow);
		p_south.setBackground(Color.orange);
		p_south.add(sales);

		// size����
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));
		p_south.setPreferredSize(new Dimension(1000, 100));

		// center�� ���̱�
		p_center.setLayout(new BorderLayout());
		p_center.add(scroll);

		// datepicker �г�
		p_date.setLayout(new BorderLayout());
		p_north.add(p_date);
		p_north.add(bt);

		// �гε� ���̱�
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

	//��ư�� ������ ���̺� ��ȸ
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==bt){
			//getTable();
			table.setModel(salesTable = new DailySalesTable(con, today));
			table.updateUI();
			salesTable.getTable();
			
			int salesTotal = salesTable.salesTotal;
			sales.setText("�� ���� ����: "+ salesTotal+" �� \n");
			
		}
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
