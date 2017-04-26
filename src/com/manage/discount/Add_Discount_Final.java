package com.manage.discount;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jitb.db.DBManager;
import com.manage.inventory.TablePanel;

import javafx.scene.layout.Border;

public class Add_Discount_Final extends JFrame implements ActionListener{
	JPanel p_west,p_center; //기본 창
	JPanel p_center_top,p_center_center,p_center_south;//센터에 상세 분류
	JLabel la_combo,la_info;
	JLabel la_name;
	JTextField t_name;
	JButton bt_add;
	table_modelF tablemodel;
	DBManager manager;
	Connection con;
	JTable table_up;
	public Add_Discount_Final(JTable table_up) {
		init(); //connect 받기
		this.table_up=table_up;
		p_west=new JPanel();//이미지 넣어둘 패널
		p_center=new JPanel();//각종 옵션 넣어둘 패널
		
		p_center_top=new JPanel();
		p_center_center=new JPanel();
		p_center_south=new JPanel();

		la_combo=new JLabel("할인정보 추가");
		la_name=new JLabel("할인 명 :");
		la_info=new JLabel("추가 하실 내용은 '포인트','상품권','카드사',가 있으며 추가시에는 테이블을 추가한후 해주세요");
		t_name=new JTextField(15);
		bt_add=new JButton("추가");
		
		p_center_top.add(la_name);
		p_center_center.add(la_name);
		p_center_center.add(t_name);
		p_center_south.add(bt_add);
		
		p_center_center.setBackground(Color.red);
		
		p_center.setLayout(new BorderLayout());
		p_center.add(p_center_top, BorderLayout.NORTH);
		p_center.add(p_center_center);
		p_center.add(p_center_south,BorderLayout.SOUTH);
		
		add(p_west,BorderLayout.WEST);
		add(p_center);
		
		bt_add.addActionListener(this);
		
		
		setSize(400,200);
		setVisible(true);
	}
	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into discount_type(discount_type_id,name)";
		sql += "values(seq_discount_type.nextval,?)";
		try {
			pstmt = con.prepareStatement(sql);

			// 바인드 변수에 들어갈 값 설정!
			pstmt.setString(1, t_name.getText());

			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				JOptionPane.showMessageDialog(this, "등록성공");
			} else {
				JOptionPane.showMessageDialog(this, "등록실패");
			}
		} catch (SQLException e) {
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
		table_up.setModel(tablemodel=new table_modelF(con,"discount_type"));
	}
	
	//추가 버튼
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("버튼을 누르셨다");
		regist();
	}
	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
}
