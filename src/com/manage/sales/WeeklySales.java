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

public class WeeklySales extends JPanel implements ActionListener {

	DBManager manager = DBManager.getInstance();
	Connection con;

	private JPanel p_north, p_center;
	private JButton bt_prev, bt_next;
	private JLabel la_title;
	private JFXPanel p_date;
	
	WeeklySalesPanel weeklyChart;
	Calendar cal = Calendar.getInstance();
	int yy;
	int mm;

	public WeeklySales() {

		p_north = new JPanel();
		p_center = new JPanel();
		bt_prev = new JButton("��");
		bt_next = new JButton("��");
		la_title = new JLabel("");


		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH);

		la_title.setText(yy + "-" + (mm + 1));

		p_north.setBackground(Color.yellow);
		p_north.add(bt_prev);
		p_north.add(la_title);
		p_north.add(bt_next);

		// size����
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));

		// center�� ���̱�
		p_center.setLayout(new BorderLayout());
		//p_center.add();

		// datepicker �г�
		//p_date.setLayout(new BorderLayout());
		//p_north.add(p_date);

		// �гε� ���̱�
		add(p_north, BorderLayout.NORTH);
		add(p_center);

		bt_prev.addActionListener(this);
		bt_next.addActionListener(this);

		connect();
		
		setVisible(true);
		setPreferredSize(new Dimension(1000, 650));

		this.setVisible(true);
		this.setBackground(Color.magenta);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public void printDate() {
		
		weeklyChart = new WeeklySalesPanel(con);

		//p_center.add(weeklyChart);
		p_center.updateUI();
		
		// ���� ��¥�� �󺧿� ���
		la_title.setText(yy + "-" + mm);
		cal.set(yy, mm + 1, 0);

		// ���� ������ ���� ������ ��¥�� ���ϱ�
		int lastDay = cal.get(Calendar.DATE);
		int lastWeek = cal.get(Calendar.DAY_OF_WEEK);
		int month = cal.get(Calendar.MONTH);

		System.out.println(DateUtil.getDateStr(Integer.toString(month)));

		for (int i = 1; i <= lastWeek; i++) {
			//���� ���ڸ����� ������ �������� �ذ��ϱ� ���� getDateStr �̿��� 04�� ���!
			weeklyChart.getData(DateUtil.getDateStr(Integer.toString(month)), Integer.toString(i));
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// ������, ������ ����
		if (obj == bt_prev) {
			mm--;
			if (mm < 0) {
				mm = 11;
				yy--;
			}
			printDate();
		} else if (obj == bt_next) {
			mm++;
			if (mm > 11) {
				mm = 0;
				yy++;
			}
		}
		printDate();
	}

	// �׷��� �θ���!
	public void graph() {
		p_center.updateUI(); // ������Ʈ ������� ��
		// p_center.add(pie.showChart());
	}

}
