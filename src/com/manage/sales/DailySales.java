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
		bt = new JButton("��ȸ");
		scroll = new JScrollPane(table);
		

		tot_sal = new JLabel("<html>��ȭ ���� ����: "+ " �� "
				+"<br>��ǰ ���� ����: "+ "��"
				+"<br>�� ���� ����: "+ "��</html>");

		p_south.add(tot_sal);

		// size����
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));
		p_south.setPreferredSize(new Dimension(1000, 100));
		
		//p_north.setBackground(Color.WHITE);
		//p_south.setBackground(Color.WHITE);
		//p_center.setBackground(Color.WHITE);
		//scroll.setBackground(Color.WHITE);

		// center�� ���̱�
		p_center.setLayout(new BorderLayout());
		p_center.add(p_east, BorderLayout.EAST);
		p_center.add(p_west, BorderLayout.WEST);
		
		p_west.add(scroll);
		
		scroll.setPreferredSize(new Dimension(500, 500));
		p_west.setPreferredSize(new Dimension(500, 500));
		p_east.setPreferredSize(new Dimension(500, 500));
		//p_east.setBackground(Color.pink);

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

		createCalendar();
		connect();
	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
	
	//��ư�� ������ ���̺� ��ȸ
	public void actionPerformed(ActionEvent e) {
		p_north.repaint();
		Object obj = e.getSource();
		if(obj==bt){

			table.setModel(salesTable = new DailySalesTable(con, today));
			table.updateUI();
			salesTable.getTable();
			
			int salesTotal = salesTable.salesTotal;
			int movieTotal = salesTable.movieTotal;
			int snackTtoal = salesTable.snackTotal;
			
			tot_sal.setText("<html>��ȭ ���� ����: "+ movieTotal+" ��"
					+"<br>��ǰ ���� ����: "+ snackTtoal+" ��"
					+"<br>�� ���� ����: "+ salesTotal+" ��</html>");	
			graph();
		}
	}
	
	// �׷��� �θ���!
	public void graph() {
		salesTable.createChart().repaint();
		salesTable.createChart().updateUI();
		p_east.removeAll();
		p_east.add(salesTable.createChart());
		p_east.revalidate();
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
