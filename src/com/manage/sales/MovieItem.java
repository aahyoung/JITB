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
				// �� img���� �ݺ��� ������ �޾ƿ´�.
				g.drawImage(poster, 0, 0, 120, 120, this);
			}
		};

		// �Ǹ���/�湮�ڼ�??
		la_movie = new JLabel(name);
		la_sales = new JLabel("��� ���� : " + sales + " �� \n");
		la_booking = new JLabel("��� ���� : " + booking + " ��");
		bt = new JButton("�󼼸���");

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
