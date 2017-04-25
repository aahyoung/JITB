package com.manage.discountFinal;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

public class Add_Point_SerialF extends JFrame implements ActionListener{
	JPanel p_center,p_north;
	JTextField t_name,t_point;
	JLabel la_name,la_SN,la_point;
	Choice choice;
	Connection con;
	DBManager manager=DBManager.getInstance();
	PointFCategory dto=new PointFCategory();
	JButton bt;
	int discount_type_id;
	JTable table;
	String insert=null;
	
	public Add_Point_SerialF(int discount_type_id) {
		this.discount_type_id=discount_type_id;
		this.table=table;
		con=manager.getConnect();
		insert=init();
		p_center=new JPanel();
		p_north=new JPanel();
		t_point=new JTextField(10);
		la_name=new JLabel("Serial_number :");
		la_SN=new JLabel(check(insert));
		la_point=new JLabel("Point :");
		choice=new Choice();
		bt=new JButton("추가");
		
		p_north.add(choice);
		p_center.add(la_name);
		p_center.add(la_SN);
		p_center.add(la_point);
		p_center.add(t_point);
		p_center.add(bt);
		
		setChoice();
		
		bt.addActionListener(this);
		
		add(p_north,BorderLayout.NORTH);
		add(p_center);
		setSize(230,300);
		setVisible(true);
	}
	public void setChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from point order by point_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dto.setDiscount_type_id(rs.getInt("point_id"));
				dto.setName(rs.getString("name"));
				dto.setImg(rs.getString("img"));
				dto.setDiscount_type_id(rs.getInt("discount_type_id"));
				choice.add(dto.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void add(){
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into point_serial(point_serial_id,serial_number,point,point_id)");
		sql.append("values(seq_point_serial.nextval,?,?,?)");
		System.out.println("sql 받았습니다");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, la_SN.getText());
			pstmt.setInt(2, Integer.parseInt(t_point.getText()));
			pstmt.setInt(3, discount_type_id);

			int rs = pstmt.executeUpdate();

			if (rs != 0) {
				JOptionPane.showMessageDialog(this, "등록성공");
				t_point.setText("");
				la_SN.setText(check(insert));
			} else {
				JOptionPane.showMessageDialog(this, "등록실패");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		add();
	}
	
	//숫자 랜덤  생성!
	public String init() {
		StringBuffer SN = new StringBuffer();
		Random rndchar = new Random();
		// String randomStr = String.valueOf((char) ((int) (rndchar.nextInt(26))
		// + 65));
		// SN.append(randomStr);

		for (int i = 0; i < 12; i++) {
			Random rnd = new Random();
			String randomNum = String.valueOf(rnd.nextInt(10));
			SN.append(randomNum);
		}
		return SN.toString();

	}
	//숫자 중복 확인 및 중복시 재 생성!
	public String check(String name) {
		String SN = name;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select serial_number from point_serial");
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String num = rs.getString("serial_number");
				if (!(num.equalsIgnoreCase(SN))) {
					SN = init();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return SN;
	}
}
