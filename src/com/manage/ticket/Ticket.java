package com.manage.ticket;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.jitb.db.DBManager;

public class Ticket extends JPanel implements ActionListener, ItemListener {
	DBManager manager;
	Connection con;
	
	Choice ch_type;
	JTextField t_price;
	JButton bt;
	JTable table;
	JScrollPane scroll;
	TableModel model;
	
	ArrayList<MoviePrice> moviePrices = new ArrayList<MoviePrice>();
	int index;

	public Ticket() {
		init();
		
		ch_type = new Choice();
		t_price = new JTextField(10);
		bt = new JButton("가격 변경");
		
		table = new JTable();
		scroll = new JScrollPane(table);
		
		ch_type.setPreferredSize(new Dimension(100, 20));
		scroll.setPreferredSize(new Dimension(900, 600));
		
		selectMoviePrice();
		
		ch_type.add("▼ 타입선택");
		for(int i=0; i<moviePrices.size(); i++){
			ch_type.add(moviePrices.get(i).getType());
		}
		
		ch_type.addItemListener(this);
		bt.addActionListener(this);
		
		table.setModel(model = new MoviePriceModel(this));
		table.updateUI();
		
		add(ch_type);
		add(t_price);
		add(bt);
		add(scroll);
	}

	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
	
	public void setModel(){
		((MoviePriceModel)model).selectMoviePrice();
		table.updateUI();
	}
	
	public void selectMoviePrice(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from movie_price";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				MoviePrice dto = new MoviePrice();
				dto.setType_id(rs.getInt("type_id"));
				dto.setType(rs.getString("type"));
				dto.setTicket_price(rs.getInt("ticket_price"));
				
				moviePrices.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updatePrice(int price){
		PreparedStatement pstmt = null;
		
		String sql = "update movie_price set ticket_price = ? where type_id = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, price);
			pstmt.setInt(2, moviePrices.get(index-1).getType_id());
			
			int result = pstmt.executeUpdate();
			if(result != 0){
				JOptionPane.showMessageDialog(this, "가격 변경 완료");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == bt){
			String str_price = t_price.getText();
			try {
				if(index!=0){
					int price = Integer.parseInt(str_price);
					updatePrice(price);
					setModel();
				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "숫자를 입력하세요");
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		index = ((Choice)e.getSource()).getSelectedIndex();
	}
}
