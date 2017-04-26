package com.manage.ticket;

import java.awt.BorderLayout;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

public class Ticket extends JFrame implements ActionListener, ItemListener {
	DBManager manager;
	Connection con;
	JTextField txt, ticket_price, t_name;
	JPanel p_west, p_north, p_south, p_center, p_up, p_down;
	JButton bt;
	JLabel la1, la2;
	Choice ch_type, ch_price;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;

	JFileChooser chooser;
	File file;

	public Ticket() {
		init();
		p_west = new JPanel();
		p_north = new JPanel();
		p_south = new JPanel();
		p_center = new JPanel();
		p_up = new JPanel();
		p_down = new JPanel();
		txt = new JTextField(10);
		ticket_price = new JTextField(10);
		la1 = new JLabel("type",JLabel.RIGHT);
		la2 = new JLabel("가격");
		bt = new JButton("등록");
		ch_type = new Choice();
		ch_price = new Choice();
		table_up = new JTable();
		table_down = new JTable();
		scroll_up = new JScrollPane(table_up);
		scroll_down = new JScrollPane(table_down);

		setLayout(new FlowLayout());
		add(p_west);
		add(la1);
		add(txt);
		add(la2);
		add(ticket_price);
		add(bt);

		setPreferredSize(new Dimension(140, 45));
		//ch_type.add("▼");
		ch_price.add("가격");

		bt.addActionListener(this);

		p_north.setPreferredSize(new Dimension(250, 500));
		p_west.setPreferredSize(new Dimension(250, 500));
		la1.setPreferredSize(new Dimension(100, 60));
		//p_center.setLayout(new GridLayout(2, 1));
		add(p_center);
		p_center.add(p_up);
		p_center.add(p_down);

		p_north.add(ch_type);
		p_north.add(ch_price);
		p_north.add(txt);
		p_north.add(ticket_price);

		p_south.add(bt);

		add(p_north, BorderLayout.NORTH);
		add(p_west, BorderLayout.WEST);
		add(p_center);

		ch_type.addItemListener(this);

		// 버튼과 리스너 연결
		bt.addActionListener(this);

		bt.addMouseListener(new MouseAdapter() {
		});

		setSize(500, 250);
		setVisible(true);

		init();
		add();
	}

	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public static void main(String[] args) {
		new Ticket();
	}

	public void add() {

		PreparedStatement pstmt = null;
		String sql = "insert into movie_price(type_id, type,ticket_price)";
		sql += " values(seq_movie_price.nextval, ?, ?, ?, ?)";
		System.out.println(sql);
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, txt.getText());
			pstmt.setInt(2, Integer.parseInt(ticket_price.getText()));

			int result = pstmt.executeUpdate();
			if (result != 0) {
				JOptionPane.showMessageDialog(this, "등록 성공");
				table_up.updateUI();
			} else {
				JOptionPane.showMessageDialog(this, "등록 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getDetail(Vector vec) {
		txt.setText(vec.get(0).toString());
		ticket_price.setText(vec.get(2).toString());
		t_name.setText(vec.get(3).toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		add();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		getType();
	}
}
