package com.manage.inventory;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.manage.inventory.ComboCategory;
import com.manage.inventory.TopCategory;
import com.manage.inventory.TopSizeCategory;

import javafx.scene.layout.Border;

public class Add_top_size extends JFrame implements ActionListener {
	JPanel p_west, p_center; // 기본 창
	JPanel p_center_top, p_center_center, p_center_south;// 센터에 상세 분류
	JLabel la_sub;
	JLabel la_name;
	JButton bt_add;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	Choice choice;
	Choice option;
	DBManager manager;
	Connection con;
	ArrayList<TopCategory> topList = new ArrayList<TopCategory>();
	ComboCategory combocategory;
	JTable table_up;
	TablePanel tablepanel;
	
	public Add_top_size(JTable table_up) {
		this.table_up=table_up;
		init();

		p_west = new JPanel();// 이미지 넣어둘 패널
		p_center = new JPanel();// 각종 옵션 넣어둘 패널

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_sub = new JLabel("상품옵션 추가");
		la_name = new JLabel("상 품 명 :");

		choice = new Choice();
		option= new Choice();

		option.add("L");
		option.add("M");
		option.add("S");
		
		bt_add = new JButton("추가");

		chooser = new JFileChooser("/JITB/res_manager");

		try {
			URL url = this.getClass().getResource("/default.png");
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		p_center_top.add(choice);
		p_center_top.add(la_sub);
		p_center_center.add(la_name);
		p_center_center.add(option);
		p_center_south.add(bt_add);

		p_center_center.setBackground(Color.red);

		p_center.setLayout(new BorderLayout());
		p_center.add(p_center_top, BorderLayout.NORTH);
		p_center.add(p_center_center);
		p_center.add(p_center_south, BorderLayout.SOUTH);

		add(p_west, BorderLayout.WEST);
		add(p_center);

		bt_add.addActionListener(this);

		setSize(400, 200);
		setVisible(true);
		choice.add("종류를 선택하세요");
		setChoice();
	}
	
	public void setChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from top_opt order by top_opt_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TopCategory dto = new TopCategory();
				dto.setTop_opt_id(rs.getInt("top_opt_id"));
				dto.setName(rs.getString("name"));
				topList.add(dto);// 리스트에 탑재
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
		table_up.setModel(tablepanel=new TablePanel(con,"top_opt_size"));

	}

	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into top_opt_size(top_opt_size_id,top_opt_id,opt_size)";
		sql += "values(seq_top_opt_size.nextval,?,?)";

		try {
			pstmt = con.prepareStatement(sql);

			int index = choice.getSelectedIndex()-1;
			TopCategory vo = topList.get(index);
			// 바인드 변수에 들어갈 값 설정!
			pstmt.setInt(1, vo.getTop_opt_id());
			
			pstmt.setString(2, option.getSelectedItem().toString());
			
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
		table_up.setModel(tablepanel=new TablePanel(con,"top_opt_size"));
	}

	// 추가 버튼
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
