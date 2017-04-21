package com.manage.discount;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.scene.layout.Border;

public class Add_Customer_Point extends JFrame implements ActionListener {
	JTextField t_name, t_point;
	JButton bt;
	JLabel la_sn, la_name, la_point, la_info, la_snValue;
	JPanel p_main, p_bt, p_top;
	Connection con;
	DBManager manager = DBManager.getInstance();
	boolean flag=false;
	String insert=null;
	public Add_Customer_Point() {
		insert=init();
		con = manager.getConnect();
		p_main = new JPanel();
		p_bt = new JPanel();
		p_top = new JPanel();
		la_info = new JLabel("회원 가입", JLabel.CENTER);
		la_snValue = new JLabel(check(insert));
		la_sn = new JLabel("일 련 번 호 : ");
		t_name = new JTextField(10);
		la_name = new JLabel("이          름 : ");
		t_point = new JTextField(10);
		la_point = new JLabel("포   인   트 :");
		bt = new JButton("완료");

		bt.addActionListener(this);
		p_main.setLayout(new GridLayout(3, 2));

		p_top.add(la_info);
		p_main.add(la_sn);
		p_main.add(la_snValue);
		p_main.add(la_name);
		p_main.add(t_name);
		p_main.add(la_point);
		p_main.add(t_point);
		p_bt.add(bt);

		add(p_bt, BorderLayout.SOUTH);
		add(p_top, BorderLayout.NORTH);
		add(p_main);
		setVisible(true);
		setSize(250, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void regist() {
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into customer_point(customer_id,serial_number,name,point,discount_type_id)");
		sql.append("values(seq_customer_point.nextval,?,?,?,?)");
		System.out.println("sql 받았습니다");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, la_snValue.getText());
			pstmt.setString(2, t_name.getText());
			pstmt.setInt(3, Integer.parseInt(t_point.getText()));
			pstmt.setInt(4, 1);

			int rs = pstmt.executeUpdate();

			if (rs != 0) {
				JOptionPane.showMessageDialog(this, "등록성공");
				t_name.setText("");
				t_point.setText("");
				la_snValue.setText(check(insert));
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

	public static void main(String[] args) {
		new Add_Customer_Point();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		regist();

	}

	public String init() {
		StringBuffer SN = new StringBuffer();
		Random rndchar = new Random();
		String randomStr = String.valueOf((char) ((int) (rndchar.nextInt(26)) + 65));

		SN.append(randomStr);
		SN.append("_");
		for (int i = 0; i < 4; i++) {
			Random rnd = new Random();
			String randomNum = String.valueOf(rnd.nextInt(10));
			SN.append(randomNum);
		}
		return SN.toString();

	}

	public String check(String name) {
		String SN=name;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select serial_number from customer_point");
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			while(rs.next()){
				String num=rs.getString("serial_number");
				if(!(num.equalsIgnoreCase(SN))){
					SN=init();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SN;
	}
}
