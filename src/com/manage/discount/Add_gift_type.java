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
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.scene.layout.Border;

public class Add_gift_type extends JFrame implements ActionListener{
	JPanel p_west,p_center; //�⺻ â
	JPanel p_center_top,p_center_center,p_center_south;//���Ϳ� �� �з�
	JLabel la_gift;
	JLabel la_name;
	JTextField t_name;
	JButton bt_add;
	Canvas can;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	
	DBManager manager;
	Connection con;
	gift_infoCategory gift_info;
	
	public Add_gift_type() {
		init(); //connect �ޱ�
		
		p_west=new JPanel();//�̹��� �־�� �г�
		p_center=new JPanel();//���� �ɼ� �־�� �г�
		
		p_center_top=new JPanel();
		p_center_center=new JPanel();
		p_center_south=new JPanel();

		la_gift=new JLabel("��ǰ�� ȸ�� �߰�");
		la_name=new JLabel("ȸ �� �� :");
		
		t_name=new JTextField(15);
		bt_add=new JButton("�߰�");
		
		chooser=new JFileChooser("/JITB/res_manager");
		
		try {
			URL url = this.getClass().getResource("/default.png");
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		can = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 135,135,this);
			}
		};
		can.setPreferredSize(new Dimension(135, 135));
		
		p_west.add(can);
		
		p_center_top.add(la_gift);
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
		
		can.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				select();
			}
		});
		
		setSize(400,200);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into gift_type(seq_gift_type,name,img)";
		sql += "values(seq_gift_type.nextval,?,?)";
		try {
			pstmt = con.prepareStatement(sql);

			// ���ε� ������ �� �� ����!
			pstmt.setString(1, t_name.getText());
			pstmt.setString(2, file.getName());

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
	
	public void select(){
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//ĵ������ �̹��� �׸���!
			file=chooser.getSelectedFile();
			
			//����� ������ ������ �̹����� ��ü�ϱ�
			try {
				image=ImageIO.read(file);
				can.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	public static void main(String[] args){
		new Add_gift_type();
	}
}