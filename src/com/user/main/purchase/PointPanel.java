package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PointPanel extends PurchasePanelFrame{
	ArrayList<Point> points = new ArrayList<Point>();
	Canvas content;
	Canvas bt_go_next;
	
	public PointPanel(ClientMain main) {
		super(main);
		
		selectGift();
		
		int x = 0;
		int y = 0;
		for(int i=0; i<points.size(); i++){
			if(x == 3){
				x = 0;
				y++;
			}
			points.get(i).setBounds(0+(x*250), 0+(y*200), 250, 200); //230, 130
			x++;
		}
		
		content = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
				
				for(int i=0; i<points.size(); i++){
					URL url = getClass().getResource("/"+points.get(i).getImg());
					try {
						Image img = ImageIO.read(url);
						g2.drawImage(img, points.get(i).x+10, points.get(i).y+10, points.get(i).width-20, points.get(i).height-70, this);
						g2.drawString(points.get(i).getName(), points.get(i).x+30, points.get(i).y+180);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2.draw(points.get(i));
				}
			}
		};
		
		bt_go_next = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_go_next.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 25, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		content.setBackground(Color.PINK);
		
		content.setPreferredSize(new Dimension(750, 550));
		bt_go_next.setPreferredSize(new Dimension(200, 100));
		
		add(content);
		add(bt_go_next);
	}
	
	public void selectGift(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from point";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Point point = new Point();
				point.setPoint_id(rs.getInt("point_id"));
				point.setName(rs.getString("name"));
				point.setImg(rs.getString("img"));
				point.setDiscount_type_id(rs.getInt("discount_type_id"));
				
				points.add(point);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
