package com.manage.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class SalesMoviePanel extends JDialog {

	Connection con;

	private JPanel p_center;
	private JTable table;
	private JScrollPane scroll;

	private String movieName;

	TableModel model;
	int cols;
	
	SalesMovieTable movieTable;

	public SalesMoviePanel(String movieName, Connection con) {

		this.movieName = movieName;
		this.con = con;

		p_center = new JPanel();
		table = new JTable(3,2);
		scroll = new JScrollPane(table);

		p_center.add(scroll);
		add(p_center);
		
		p_center.setBackground(Color.pink);
		table.setLayout(new BorderLayout());
		scroll.setPreferredSize(new Dimension(600, 500));

		setSize(800,600);
		setVisible(true);
		setLocationRelativeTo(null);
		getTable();
	}
	
	public void getTable() {
		table.setModel(movieTable = new SalesMovieTable(movieName, con));
		table.updateUI();
		movieTable.getTable();
		p_center.add(scroll);
		p_center.updateUI();
	}

}
