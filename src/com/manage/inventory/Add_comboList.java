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
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.scene.layout.Border;

public class Add_comboList extends JFrame implements ActionListener, ItemListener {
	JPanel p_center; // �⺻ â
	JPanel p_center_top, p_center_center, p_center_south;// ���Ϳ� �� �з�
	JLabel la_sub;
	JLabel la_name;
	JTextField t_name;
	JButton bt_add;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	Choice choice;
	Choice subchoice;

	DBManager manager;
	Connection con;
	ArrayList<ComboCategory> topList = new ArrayList<ComboCategory>();
	ArrayList<TopSizeCategory> subList = new ArrayList<TopSizeCategory>();
	ComboCategory combocategory;

	TopSizeCategory dto = new TopSizeCategory();
	
	public Add_comboList() {
		init();

		p_center = new JPanel();// ���� �ɼ� �־�� �г�

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_sub = new JLabel("�޺��ɼǰ���");
		la_name = new JLabel("��  ��  ��  �� :");

		choice = new Choice();
		subchoice=new Choice();
		t_name = new JTextField(15);
		bt_add = new JButton("�߰�");
		try {
			URL url = this.getClass().getResource("/default.png");
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		p_center_top.add(choice);
		p_center_top.add(subchoice);
		p_center_top.add(la_sub);
		p_center_center.add(la_name);
		p_center_center.add(t_name);
		p_center_south.add(bt_add);

		p_center_center.setBackground(Color.red);

		p_center.setLayout(new BorderLayout());
		p_center.add(p_center_top, BorderLayout.NORTH);
		p_center.add(p_center_center);
		p_center.add(p_center_south, BorderLayout.SOUTH);
		add(p_center);

		bt_add.addActionListener(this);
		
		choice.addItemListener(this);
		
		setSize(400, 200);
		setVisible(true);
		choice.add("������ �����ϼ���");
		subchoice.add("����� ����ּ���");
		subchoice.add("�����ɼ��� �������ּ���");
		setChoice();
	}
	
	public void setChoice() {
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
		String sql = "insert into combo_list(combo_list_id,amount,combo_id,top_opt_size_id)";
		sql += "values(seq_combo_list.nextval,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);

			int index = choice.getSelectedIndex()-1;
			int subindex =subchoice.getSelectedIndex()-1;
			ComboCategory vo = topList.get(index);
			dto=subList.get(subindex);
			// ���ε� ������ �� �� ����!
			
			pstmt.setInt(1,Integer.parseInt(t_name.getText()));
			
			pstmt.setInt(2, vo.getCombo_id());
			
			pstmt.setInt(3, dto.getTop_opt_size_id());
			
			
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
		this.dispose();
	}
	
	public void setSubChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from top_opt_size order by top_opt_size_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			subchoice.removeAll();
			subchoice.add("����� ����ּ���");
			while (rs.next()) {
				dto.setTop_opt_size_id(rs.getInt("Top_opt_size_id"));
				dto.setOpt_size(rs.getString("opt_size"));
				subList.add(dto);// ����Ʈ�� ž��
				subchoice.add(dto.getOpt_size());
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
		/// ���� ī�װ� ���ϱ�!
		setSubChoice();
	}
}
