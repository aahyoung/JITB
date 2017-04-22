package com.user.main.purchase.combo;

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
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class OptionChoiceScreen extends ScreenFrame{
	JLabel la_comboName;
	JPanel p_price;
	JLabel la_priceInfo;
	JLabel la_price;
	JLabel la_optInfo;
	JPanel p_topOpt;
	Canvas can_subOpt;
	Canvas bt_confirm;
	
	ArrayList<TopOption> topOpt = new ArrayList<TopOption>();
	ArrayList<SubOption> subOpts = new ArrayList<SubOption>();
	
	boolean isFirst = true;
	
	public OptionChoiceScreen(ClientMain main) {
		super(main);
		
		la_comboName = new JLabel("콤보이름");
		la_priceInfo = new JLabel("총 결제금액");
		p_price = new JPanel();
		la_price = new JLabel("0원");
		la_optInfo = new JLabel("콤보 옵션을 선택해주세요", JLabel.CENTER);
		p_topOpt = new JPanel();
		can_subOpt = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				if(!isFirst){
					can_subOpt.setBackground(new Color(25,25,25));
					for(int i=0; i<subOpts.size(); i++){
						SubOption subOpt = subOpts.get(i);
						subOpt.setBounds(25+(i*150), 25, 150, 200);
						
						URL url = getClass().getResource("/"+subOpt.getSub_opt_img());
						try {
							Image img = ImageIO.read(url);
							g2.drawImage(img, subOpt.x+25, subOpt.y, subOpt.width-50, subOpt.height-50, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
						g2.setColor(Color.WHITE);
						g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
						g2.drawString(subOpt.getSub_opt_name()+"("+subOpt.getSub_opt_size()+")", subOpt.x+10, subOpt.y+180);
						g2.drawString("(+"+subOpt.getPlus_price()+"원)", subOpt.x+30, subOpt.y+205);
						//g2.draw(subOpt);
					}
				}else{
					isFirst = false;
					g2.setColor(new Color(66, 106, 126));
					g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
					g2.drawString("선택하고자 하는 옵션 이미지를 눌러주세요.", 140, 80);
				}
			}
		};
		bt_confirm = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_select_end.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		la_comboName.setForeground(Color.WHITE);
		la_priceInfo.setForeground(Color.WHITE);
		la_price.setForeground(Color.WHITE);
		la_optInfo.setForeground(Color.WHITE);
		
		p_price.setBackground(new Color(33,33,33));
		p_topOpt.setBackground(new Color(33,33,33));
		can_subOpt.setBackground(new Color(33,33,33));
		
		la_comboName.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_priceInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_optInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		
		la_comboName.setPreferredSize(new Dimension(550, 80));
		p_price.setPreferredSize(new Dimension(150, 80));
		la_priceInfo.setPreferredSize(new Dimension(150, 25));
		la_price.setPreferredSize(new Dimension(150, 55));
		la_optInfo.setPreferredSize(new Dimension(700, 150));
		p_topOpt.setPreferredSize(new Dimension(700, 300));
		can_subOpt.setPreferredSize(new Dimension(800, 250));
		bt_confirm.setPreferredSize(new Dimension(200, 50));
		
		p_price.add(la_priceInfo);
		p_price.add(la_price);
		add(la_comboName);
		add(p_price);
		add(la_optInfo);
		add(p_topOpt);
		add(can_subOpt);
		add(bt_confirm);
	}
	
	public void selectSubOpt(int size_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select so.sub_opt_id as 옵션아이디, so.name as 옵션이름, so.price as 옵션가격, so.img as 옵션이미지, tos.opt_size as 옵션크기");
		sql.append(" from sub_opt so");
		sql.append(" inner join top_opt_size tos on tos.top_opt_size_id = so.top_opt_size_id");
		sql.append(" where tos.top_opt_size_id = ?");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, size_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				SubOption subOpt = new SubOption();
				subOpt.setSub_opt_id(rs.getInt("옵션아이디"));
				subOpt.setSub_opt_name(rs.getString("옵션이름"));
				subOpt.setSub_opt_img(rs.getString("옵션이미지"));
				subOpt.setPlus_price(rs.getInt("옵션가격"));
				subOpt.setSub_opt_size(rs.getString("옵션크기"));
				
				subOpts.add(subOpt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
