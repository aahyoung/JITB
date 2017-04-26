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
	JPanel p_west,p_center; //�⺻ â
	JPanel p_center_top,p_center_center,p_center_south;//���Ϳ� �� �з�
	JLabel la_combo,la_info;
	JLabel la_name;
	JTextField t_name;
	JButton bt_add;
	table_modelF tablemodel;
	DBManager manager;
	Connection con;
	JTable table_up;
	public Add_Discount_Final(JTable table_up) {
		init(); //connect �ޱ�
		this.table_up=table_up;
		p_west=new JPanel();//�̹��� �־�� �г�
		p_center=new JPanel();//���� �ɼ� �־�� �г�
		
		p_center_top=new JPanel();
		p_center_center=new JPanel();
		p_center_south=new JPanel();

		la_combo=new JLabel("�������� �߰�");
		la_name=new JLabel("���� �� :");
		la_info=new JLabel("�߰� �Ͻ� ������ '����Ʈ','��ǰ��','ī���',�� ������ �߰��ÿ��� ���̺��� �߰����� ���ּ���");
		t_name=new JTextField(15);
		bt_add=new JButton("�߰�");
		
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

			// ���ε� ������ �� �� ����!
			pstmt.setString(1, t_name.getText());

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
		table_up.setModel(tablemodel=new table_modelF(con,"discount_type"));
	}
	
	//�߰� ��ư
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("��ư�� �����̴�");
		regist();
	}
	public void init() {
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
}
