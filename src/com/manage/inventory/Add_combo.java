package com.manage.inventory;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
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
import com.manage.main.Main;

import javafx.scene.layout.Border;

public class Add_combo extends JFrame implements ActionListener {
	JPanel p_west, p_center; // 기본 창
	JPanel p_center_top, p_center_center, p_center_south;// 센터에 상세 분류
	JLabel la_combo;
	JLabel la_name, la_price;
	JTextField t_name, t_price;
	JButton bt_add;
	Canvas can;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	JTable table_up;
	DBManager manager;

	TablePanel tablepanel;

	Connection con;
	ComboCategory combocategory;
	int count = 0;// 콤보 갯수를 확인하는 변수

	public Add_combo(JTable table_up) {
		init(); // connect 받기
		this.table_up = table_up;
		p_west = new JPanel();// 이미지 넣어둘 패널
		p_center = new JPanel();// 각종 옵션 넣어둘 패널

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_combo = new JLabel("Combo 추가");
		la_name = new JLabel("상 품 명 :");
		la_price = new JLabel("가        격:");

		t_name = new JTextField(15);
		t_price = new JTextField(15);
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

		can = new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135, 135, this);
			}
		};
		can.setPreferredSize(new Dimension(135, 135));

		p_west.add(can);
		p_center_top.add(la_combo);
		p_center_center.add(la_name);
		p_center_center.add(t_name);
		p_center_center.add(la_price);
		p_center_center.add(t_price);
		p_center_south.add(bt_add);

		p_center_center.setBackground(Color.red);

		p_center.setLayout(new BorderLayout());
		p_center.add(p_center_top, BorderLayout.NORTH);
		p_center.add(p_center_center);
		p_center.add(p_center_south, BorderLayout.SOUTH);

		add(p_west, BorderLayout.WEST);
		add(p_center);

		bt_add.addActionListener(this);

		can.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				select();
			}
		});
		setSize(400, 200);
		setVisible(true);
	}

	public void check() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from combo order by combo_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ComboCategory dto = new ComboCategory();
				dto.setCombo_id(rs.getInt("combo_id"));
				dto.setName(rs.getString("name"));
				count++;
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

	public void regist() {
		check();
		if (count < 3) {
			PreparedStatement pstmt = null;
			String sql = "insert into COMBO(combo_id,name,img,price)";
			sql += "values(seq_combo.nextval,?,?,?)";
			try {
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, t_name.getText());
				pstmt.setString(2, file.getName());
				pstmt.setInt(3, Integer.parseInt(t_price.getText()));
				System.out.println(file.getAbsolutePath());
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
						e.printStackTrace();
					}
				}
			}
			String filepath=file.getAbsolutePath();
			Main.main.upload(filepath, "img");
			table_up.setModel(tablepanel = new TablePanel(con, "combo"));
		}
		else{
			JOptionPane.showMessageDialog(this, "콤보의 종류가 3개 이상입니다.");
		}
	}

	public void select() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			// 캔버스에 이미지 그리자!
			file = chooser.getSelectedFile();

			// 얻어진 파일을 기존의 이미지로 대체하기
			try {
				image = ImageIO.read(file);
				can.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
