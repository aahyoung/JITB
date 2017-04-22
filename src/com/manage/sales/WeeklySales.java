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
		bt_prev = new JButton("◀");
		bt_next = new JButton("▶");
		la_title = new JLabel("");

		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH);
		
		//la_title.setText(yy + "-" + (mm+1));
		//System.out.println("label에 찍히는 달은?" +mm+1);

		p_north.setBackground(Color.ORANGE);
		p_north.add(bt_prev);
		p_north.add(la_title);
		p_north.add(bt_next);

		// size조정
		p_north.setPreferredSize(new Dimension(1000, 50));
		p_center.setPreferredSize(new Dimension(1000, 500));

		// center에 붙이기
		p_center.setLayout(new BorderLayout());
		//p_center.add();

		//p_date.setLayout(new BorderLayout());

		// 패널들 붙이기
		add(p_north, BorderLayout.NORTH);
		add(p_center);

		bt_prev.addActionListener(this);
		bt_next.addActionListener(this);

		connect();
		printDate();
		graph();
		
		this.setVisible(true);
		this.setBackground(Color.ORANGE);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public void printDate() {
		
		
		weeklyChart = new WeeklySalesPanel(con);
		//위클리차트의 ArrayList는 월을 바꿀때마다 초기화 되거나, 모두 삭제되어야 한다.
		//왜?? for문 돌릴때마다 5개씩 누적되니깐...
		weeklyChart.list.removeAll(weeklyChart.list);

		p_center.updateUI();
		
		//현재 날짜를 라벨에 출력
		la_title.setText(yy + "-" + (cal.get(Calendar.MONTH)+1));
		cal.set(yy, (mm+1), 0);
		System.out.println("여기서 mm은"+cal.get(Calendar.MONTH));

		int lastDay = cal.get(Calendar.DATE);
		System.out.println("마지막 날은=" + lastDay);
		
		// 현재 선택한 달의 마지막 날짜를 구하기
		int month = cal.get(Calendar.MONTH);
		System.out.println("현재 월은" + (month+1));

		int lastWeek = cal.get(Calendar.WEEK_OF_MONTH);
		System.out.println("마지막 주는" + lastWeek);

		System.out.println(DateUtil.getDateStr(Integer.toString(month+1)));

		//for (int i = 1; i <= lastWeek; i++) {
			//System.out.println("주가 찍힌다" + i);
			//월이 한자리수로 찍히는 문제점을 해결하기 위해 getDateStr 이용해 04로 출력!
			weeklyChart.getData(DateUtil.getDateStr(Integer.toString(month+1)), lastWeek);
		//}
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// 이전달, 다음달 구분
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
			printDate();
		}
		
	}

	// 그래프 부르기!
	public void graph() {
		p_center.add(weeklyChart.createChart());
		p_center.updateUI();
		
	}

}
