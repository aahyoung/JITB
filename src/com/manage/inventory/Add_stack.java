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

public class Add_stack extends JFrame implements ActionListener, ItemListener {
	JPanel p_center; // �⺻ â
	JPanel p_center_top, p_center_center, p_center_south;// ���Ϳ� �� �з�
	JLabel la_sub;
	JLabel la_name, la_price, la_stock, la_nameShow;
	JTextField t_stock;
	JButton bt_add;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	Choice choice;
	Connection con;
	ArrayList<SubCategory> topList = new ArrayList<SubCategory>();
	ComboCategory combocategory;
	JTable table_up;
	TablePanel tablepanel;
	DBManager manager = DBManager.getInstance();
	int id = 0;

	public Add_stack(Connection con, JTable table) {// �������� ���̺�,con �޴°� �صѰ�
		this.table_up = table;
		this.con = con;
		con = manager.getConnect();
		p_center = new JPanel();// ���� �ɼ� �־�� �г�

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_sub = new JLabel("��ǰ�ɼ� �߰�");
		la_name = new JLabel("�� ǰ �� :");
		la_nameShow = new JLabel();
		la_stock = new JLabel("��        ��:");

		choice = new Choice();
		t_stock = new JTextField(15);
		bt_add = new JButton("�߰�");

		p_center_top.add(choice);
		p_center_top.add(la_sub);
		p_center_center.add(la_name);
		p_center_center.add(la_nameShow);
		p_center_center.add(la_stock);
		p_center_center.add(t_stock);
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
		setChoice();
	}

	public void setChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from sub_opt order by sub_opt_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SubCategory dto = new SubCategory();
				dto.setSub_opt_id((rs.getInt("sub_opt_id")));
				dto.setTop_opt_size_id((rs.getInt("top_opt_size_id")));
				dto.setName((rs.getString("name")));
				dto.setPrice((rs.getInt("price")));
				dto.setStock((rs.getString("img")));
				dto.setStock((rs.getString("stock")));
				topList.add(dto);// ����Ʈ�� ž��
				String list = dto.getName();
				list += "SIZE_ID : ";
				list += dto.getTop_opt_size_id();
				choice.add(list);
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

		int total = Integer.parseInt((la_nameShow.getText()));
		total += (Integer.parseInt(t_stock.getText()));
		System.out.println(total);
		String sql = null;
		PreparedStatement pstmt = null;
		sql = "update sub_opt set stock=" + total + " where sub_opt_id=" + id;// ������
																				// �־
			System.out.println(sql);																	// ��������
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("sql���ư��ϴ�");
		t_stock.setText("");
		select();
		table_up.setModel(tablepanel = new TablePanel(con, "sub_opt"));
	}

	// �߰� ��ư
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("��ư�� �����̴�");
		regist();
	}

	public void select() {
		id = topList.get(choice.getSelectedIndex()-1).getSub_opt_id();
		System.out.println(id);
		la_nameShow.setText(topList.get((choice.getSelectedIndex() - 1)).getStock());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		select();

	}

}
