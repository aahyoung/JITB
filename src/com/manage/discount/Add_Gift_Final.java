package com.manage.discount;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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

import sun.security.krb5.internal.tools.Kinit;

public class Add_Gift_Final extends JFrame implements ActionListener {
	Canvas can;
	JPanel p_center, p_img;
	JTextField t_name, t_rate;
	JLabel la_name, la_rate;
	JButton bt;
	Connection con;
	DBManager manager = DBManager.getInstance();
	JFileChooser chooser;
	Image image;
	Toolkit kit = Toolkit.getDefaultToolkit();
	File file;
	int discount_type_id;
	JTable table;
	table_modelF tablemodel;
	String path;

	public Add_Gift_Final(int discount_type_id, JTable table,Connection con) {
		this.discount_type_id = discount_type_id;
		this.table = table;

		this.con = con;
		p_center = new JPanel();
		p_img = new JPanel();

		la_name = new JLabel("상 품 권 이름  : ");
		t_name = new JTextField(10);
		la_rate = new JLabel("할    인    율   : ");
		t_rate = new JTextField(10);
		bt = new JButton("추가");

		chooser = new JFileChooser("/JITB/res_manager");

		bt.addActionListener(this);
		URL url = this.getClass().getResource("/default.png");
		try {
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		can = new Canvas() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 230, 130, this);
			}
		};
		can.setPreferredSize(new Dimension(230, 130));

		p_img.add(can);

		p_center.add(la_name);
		p_center.add(t_name);
		p_center.add(la_rate);
		p_center.add(t_rate);
		p_center.add(bt);

		add(p_img, BorderLayout.NORTH);
		add(p_center);

		can.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				select();
			}
		});

		setVisible(true);
		setSize(250, 280);
	}

	public void select() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			// 캔버스에 이미지 그리자!
			file = chooser.getSelectedFile();

			// 얻어진 파일을 기존의 이미지로 대체하기

			// image=ImageIO.read(file);
			image = kit.getImage(file.getAbsolutePath());
			can.repaint();

		}
	}

	public void add() {
		PreparedStatement pstmt = null;
		String sql = "insert into gift(gift_id,name,rate,img,discount_type_id)";
		sql += "values(seq_gift.nextval,?,?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);

			// 바인드 변수에 들어갈 값 설정!
			pstmt.setString(1, t_name.getText());
			pstmt.setDouble(2, Double.parseDouble(t_rate.getText()));
			pstmt.setString(3, file.getName());
			pstmt.setInt(4, discount_type_id);// discountFinalMain에서 받아옴

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
		table.setModel(tablemodel = new table_modelF(con, "상품권"));
		copyPoster();
		this.dispose();
	}

	public void copyPoster() {
		path = file.getAbsolutePath();
		System.out.println("path 경로 : "+path);
		// System.out.println(filePath);
		Main main = Main.getMain();
		main.upload(path, "img", "discount/");
		// System.out.println("영화 포스터 저장"+Calendar.getInstance().getTime());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		add();
	}
}
