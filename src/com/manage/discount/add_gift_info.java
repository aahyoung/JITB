package com.manage.discount;

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
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.scene.layout.Border;

public class add_gift_info extends JFrame implements ActionListener, ItemListener {
	JPanel p_west, p_center; // �⺻ â
	JPanel p_center_top, p_center_center, p_center_south;// ���Ϳ� �� �з�
	JLabel la_sub;
	JLabel la_price, la_no;
	JTextField t_name, t_price, t_no;
	JButton bt_add;
	Canvas can;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	Choice choice;
	
	DBManager manager;
	Connection con;
	ArrayList<gift_typeCategory> topList = new ArrayList<gift_typeCategory>();
	gift_infoCategory giftinfoCategory;

	public add_gift_info() {
		init();

		p_west = new JPanel();// �̹��� �־�� �г�
		p_center = new JPanel();// ���� �ɼ� �־�� �г�

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_sub = new JLabel("��ǰ�� �߰�");
		la_no = new JLabel("�Ϸù�ȣ :");
		la_price = new JLabel("��         �� :");

		choice = new Choice();
		t_no = new JTextField(15);
		t_price = new JTextField(15);
		bt_add = new JButton("�߰�");

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

		p_center_top.add(choice);
		p_center_top.add(la_sub);
		p_center_center.add(la_no);
		p_center_center.add(t_no);
		p_center_center.add(la_price);
		p_center_center.add(t_price);
		p_center_center.add(la_no);
		p_center_center.add(t_no);
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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		choice.add("ȸ�縦 �����ϼ���");
		setChoice();
	}
	
	public void setChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from gift_type order by gift_type_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gift_typeCategory dto = new gift_typeCategory();
				dto.setgift_type_id(rs.getInt("gift_type_id"));
				dto.setName(rs.getString("name"));
				topList.add(dto);// ����Ʈ�� ž��
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

	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into gift_info(gift_info_id,gift_type_id,no,price,status,img)";
		sql += "values(seq_gift_info.nextval,?,?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);

			int index = choice.getSelectedIndex();
			gift_typeCategory vo = topList.get(index);
			// ���ε� ������ �� �� ����!
			pstmt.setInt(1, vo.getgift_type_id()-1);
			System.out.println(vo.getgift_type_id());
			
			pstmt.setString(2, t_name.getText());
			System.out.println(t_name.getText());
			
			pstmt.setInt(3, Integer.parseInt(t_price.getText()));
			System.out.println(t_price.getText());
			
			pstmt.setString(4, file.getName());
			System.out.println(file.getName());
			
			pstmt.setInt(5, Integer.parseInt(t_no.getText()));
			System.out.println(t_no.getText());

			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				JOptionPane.showMessageDialog(this, "��ϼ���");
			} else {
				JOptionPane.showMessageDialog(this, "��Ͻ���");
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
	}

	public void select() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			// ĵ������ �̹��� �׸���!
			file = chooser.getSelectedFile();

			// ����� ������ ������ �̹����� ��ü�ϱ�
			try {
				image = ImageIO.read(file);
				can.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// �߰� ��ư
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("��ư�� �����̴�");
		regist();
	}

	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}

	public void itemStateChanged(ItemEvent e) {
		/// ���� ī�װ��� ���ϱ�!

	}
	
}