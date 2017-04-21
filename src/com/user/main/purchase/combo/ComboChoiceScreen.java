package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.DBManager;
import com.manage.inventory.TopSizeCategory;
import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class ComboChoiceScreen extends ScreenFrame{
	JLabel la_top, la_down,la_null;
	JPanel p_center,p_south,p_all;
	Canvas can;
	Connection con;
	DBManager manager = DBManager.getInstance();
	StringBuffer sql;
	Vector<Combo> vec = new Vector<Combo>();
	BufferedImage image = null;
	public ComboChoiceScreen(ClientMain main) {
		super(main);
		con = manager.getConnect();
		la_top = new JLabel("맛있는 콤보와 함께 영화를 즐겨봐요!",JLabel.CENTER);
		la_null=new JLabel(" ");
		la_down = new JLabel("원산지 정보 : 옥수수(호주산)/나초칩(중국산)",JLabel.CENTER);
		p_center = new JPanel();
		p_south=new JPanel();
		comboset();

		la_top.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_down.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		
		la_top.setForeground(Color.white);
		la_down.setForeground(Color.WHITE);
		
		la_top.setPreferredSize(new Dimension(750, 100));
		la_down.setPreferredSize(new Dimension(750, 50));
		la_null.setPreferredSize(new Dimension(750, 60));
		can=new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.black);
				g.fillRect(265, 50, 200, 100);
			}
		};
		can.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				if(x>=265&&x<=465){
					if(y>=50&&y<=150){
						System.out.println("되냐?");
					}
				}
			}
		});
		can.setPreferredSize(new Dimension(750, 300));
		
		p_south.setPreferredSize(new Dimension(750, 500));
		p_south.add(la_down);
		p_south.add(can);
		
		p_south.setBackground(new Color(33, 33, 33));
		p_center.setBackground(new Color(33, 33, 33));

		setBackground(new Color(33,33,33));
		add(la_top);
		add(la_null);
		add(p_center);
		add(p_south);
		setVisible(true);
	}

	public void comboset() {
		sql = new StringBuffer();
		PreparedStatement pstmt = null;
		sql.append("select * from combo");
		try {
			pstmt = con.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("combo_id");
				String name = rs.getString("name");
				String img = rs.getString("img");
				int price = rs.getInt("price");
				Combo dto = new Combo(id, name, price, img);
				p_center.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		new test();
	}
}
