package com.manage.sales;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jitb.db.DBManager;

public class MovieItem extends JPanel implements ActionListener {

	private Canvas can;
	private JLabel la_movie, la_sales, la_booking;
	private JButton bt;
	
	DBManager manager;
	private Connection con;

	SalesMoviePanel saelsMovie;

	public MovieItem(Image poster, String name, String sales, String booking, Connection con) {
		this.con=con;

		can = new Canvas() {
			public void paint(Graphics g) {
				// 이 img들은 반복문 돌려서 받아온다.
				g.drawImage(poster, 0, 0, 120, 120, this);
			}
		};

		// 판매율/방문자수??
		la_movie = new JLabel(name);
		la_sales = new JLabel("평균 매출 : " + sales + " 원 \n");
		la_booking = new JLabel("평균 예매 : " + booking + " 건");
		bt = new JButton("상세매출");

		add(can);
		add(la_movie);
		add(la_sales);
		add(la_booking);
		add(bt);

		bt.addActionListener(this);

		can.setPreferredSize(new Dimension(120, 120));
		setPreferredSize(new Dimension(130, 250));

	}

	public void connect() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt) {
			String movieName = la_movie.getText();
			saelsMovie = new SalesMoviePanel(movieName, con);
		}
	}

}
