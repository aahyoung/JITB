package com.manage.ticket;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

public class Ticket extends JFrame implements ActionListener{
	DBManager manager;
	Connection con;
	JTextField type,price;
	JPanel panel;
	JButton bt;
	JLabel la1,la2;
	
	public Ticket() {
		init();
		panel=new JPanel();
		type=new JTextField(20);
		price=new JTextField(20);
		bt=new JButton("추가");
		la1=new JLabel("청소년/성인");
		la2=new JLabel("가격");
	
		add(panel);
		
		panel.add(type);
		panel.add(price);
		panel.add(la1);
		panel.add(la2);
		
		add(bt,BorderLayout.SOUTH);
		
		bt.addActionListener(this);
		
		//la1.setPreferredSize(new Dimension(40,100));
		//la2.setPreferredSize(new Dimension(10,0));
		setSize(500,250);
		setVisible(true);
		
	}
	/*PreparedStatement pstmt = null;
		String sql = "insert into COMBO(combo_id,name,img,price)";
		sql += "values(seq_combo.nextval,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, t_name.getText());
			pstmt.setString(2, file.getName());
			pstmt.setInt(3, Integer.parseInt(t_price.getText()));

			int rs = pstmt.executeUpdate();
	 * */
	public static void main(String[] args) {
		new Ticket();
	}
	public void init(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	public void add(){

		String sql = "insert into movie_price(type_id,type,price) "
				+ "VALUES(seq_movie_price.nextval,?,?)";
		PreparedStatement pstmt = null;
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1,type.getText());
				pstmt.setInt(2,Integer.parseInt(price.getText()));
				
				int rs=pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		add();
	}
}
