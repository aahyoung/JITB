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
import com.manage.main.Main;

import javafx.scene.layout.Border;

public class Add_sub extends JFrame implements ActionListener {
	JPanel p_west, p_center; // 기본 창
	JPanel p_center_top, p_center_center, p_center_south;// 센터에 상세 분류
	JLabel la_sub;
	JLabel la_name, la_price, la_stock;
	JTextField t_name, t_price, t_stock;
	JButton bt_add;
	Canvas can;
	JFileChooser chooser;
	BufferedImage image = null;
	File file;
	Choice choice;

	DBManager manager;
	Connection con;
	ArrayList<TopSizeCategory> topList = new ArrayList<TopSizeCategory>();
	ArrayList<TopCategory> sub = new ArrayList<TopCategory>();
	ComboCategory combocategory;
	JTable table_up;
	TablePanel tablepanel;
	public Add_sub(JTable table_up) {
		this.table_up=table_up;
		init();

		p_west = new JPanel();// 이미지 넣어둘 패널
		p_center = new JPanel();// 각종 옵션 넣어둘 패널

		p_center_top = new JPanel();
		p_center_center = new JPanel();
		p_center_south = new JPanel();

		la_sub = new JLabel("상품옵션 추가");
		la_name = new JLabel("상 품 명 :");
		la_price = new JLabel("가        격:");
		la_stock = new JLabel("재        고:");

		choice = new Choice();
		t_name = new JTextField(15);
		t_price = new JTextField(15);
		t_stock = new JTextField(15);
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

		p_center_top.add(choice);
		p_center_top.add(la_sub);
		p_center_center.add(la_name);
		p_center_center.add(t_name);
		p_center_center.add(la_price);
		p_center_center.add(t_price);
		p_center_center.add(la_stock);
		p_center_center.add(t_stock);
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
		choice.add("종류를 선택하세요1");
		setChoice();
	}
	
	public void setChoice() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from top_opt_size order by top_opt_size_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int count=0;
			while (rs.next()) {
				TopSizeCategory dto = new TopSizeCategory();
				dto.setTop_opt_size_id(rs.getInt("top_opt_size_id"));
				dto.setTop_opt_id(rs.getInt("top_opt_id"));
				dto.setOpt_size(rs.getString("opt_size"));
				topList.add(dto);// 리스트에 탑재
				selectChoice(dto.getTop_opt_id());
				String list=dto.getOpt_size()+"  "+sub.get(count).getName();
				choice.add(list);
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
	public void selectChoice(int top_opt) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from top_opt where top_opt_id="+top_opt;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TopCategory dto = new TopCategory();
				dto.setTop_opt_id(rs.getInt("top_opt_id"));
				dto.setName((rs.getString("name")));
				dto.setImg((rs.getString("img")));
				sub.add(dto);// 리스트에 탑재
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
		String sql = "insert into sub_opt(sub_opt_id,top_opt_size_id,name,price,img,stock)";
		sql += "values(seq_sub_opt.nextval,?,?,?,?,?)";

		try {
			pstmt = con.prepareStatement(sql);

			int index = choice.getSelectedIndex()-1;
			TopSizeCategory vo = topList.get(index);
			// 바인드 변수에 들어갈 값 설정!
			pstmt.setInt(1, vo.getTop_opt_size_id());
			System.out.println(vo.getTop_opt_id());
			
			pstmt.setString(2, t_name.getText());
			System.out.println(t_name.getText());
			
			pstmt.setInt(3, Integer.parseInt(t_price.getText()));
			System.out.println(t_price.getText());
			
			pstmt.setString(4, file.getName());
			System.out.println(file.getName());
			
			pstmt.setInt(5, Integer.parseInt(t_stock.getText()));
			System.out.println(t_stock.getText());

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
		String filepath=file.getAbsolutePath();
		Main.main.upload(filepath, "img");
		table_up.setModel(tablepanel=new TablePanel(con,"sub_opt"));
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
