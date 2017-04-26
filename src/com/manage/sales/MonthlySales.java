package com.manage.sales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.date.DateUtil;
import com.jitb.db.DBManager;

import javafx.embed.swing.JFXPanel;

public class MonthlySales extends JPanel implements ActionListener{
	
	DBManager manager = DBManager.getInstance();
	Connection con;

	private JPanel p_north, p_center;
	private JButton bt_prev, bt_next;
	private JLabel la_title;
	
	MonthlySalesPanel monthlyChart;
	Calendar cal = Calendar.getInstance();
	int yy;

	public MonthlySales() {


		p_north = new JPanel();
		p_center = new JPanel();
		bt_prev = new JButton("◀");
		bt_next = new JButton("▶");
		la_title = new JLabel("");

		yy = cal.get(Calendar.YEAR);

		//p_north.setBackground(Color.white);
		p_north.add(bt_prev);
		p_north.add(la_title);
		p_north.add(bt_next);

		// size조정
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));

		// center에 붙이기
		p_center.setLayout(new BorderLayout());

		// 패널들 붙이기
		add(p_north, BorderLayout.NORTH);
		add(p_center);

		bt_prev.addActionListener(this);
		bt_next.addActionListener(this);

		connect();
		printDate();
		graph();
		
		this.setVisible(true);
		this.setBackground(Color.white);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public void printDate() {		
		
		monthlyChart = new MonthlySalesPanel(con);
		monthlyChart.list.removeAll(monthlyChart.list);

		p_center.updateUI();
		
		//현재 날짜를 라벨에 출력
		la_title.setText(Integer.toString(yy));
		System.out.println(yy);
		monthlyChart.getData(DateUtil.getDateStr(Integer.toString(yy)));
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// 이전년도, 다음년도 출력
		if (obj == bt_prev) {
			yy--;
			printDate();
			graph();
			
		} else if (obj == bt_next) {
			yy++;
			printDate();
			graph();
		}		
	}
	
	// 그래프 부르기!
	public void graph() {
		monthlyChart.createChart().repaint();
		monthlyChart.createChart().updateUI();
		p_center.removeAll();
		p_center.add(monthlyChart.createChart());
		p_center.revalidate();
	}
}
