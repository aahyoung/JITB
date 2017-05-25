package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.MalformedURLException;
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
import com.user.main.purchase.ChoiceConfirmScreen;

public class OptionChoiceScreen extends ScreenFrame{
	JLabel la_comboName;
	JPanel p_price;
	JLabel la_priceInfo;
	JLabel la_price;
	JLabel la_optInfo;
	JPanel p_topOpt;
	Canvas can_subOpt;
	Canvas bt_confirm;
	
	ArrayList<TopOption> topOpts = new ArrayList<TopOption>();
	ArrayList<SubOption> subOpts = new ArrayList<SubOption>();
	
	boolean isFirst = true;
	boolean isDragged;
	int[] offX;
	
	Image buffrImg;
	Graphics2D buffr;
	
	public OptionChoiceScreen(ClientMain main) {
		super(main);
		
		la_comboName = new JLabel("�޺��̸�");
		la_priceInfo = new JLabel("�� �����ݾ�");
		p_price = new JPanel();
		la_price = new JLabel("0");
		la_optInfo = new JLabel("�޺� �ɼ��� �������ּ���", JLabel.CENTER);
		p_topOpt = new JPanel();
		
		setSuboptBounds();
		can_subOpt = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				
				buffrImg = createImage(800, 250);
				buffr = (Graphics2D)buffrImg.getGraphics();
				
				if(!isFirst){
					can_subOpt.setBackground(new Color(25,25,25));
					for(int i=0; i<subOpts.size(); i++){
						SubOption subOpt = subOpts.get(i);
						
						URL url = null;
						try {
							url = new URL("http://localhost:8989/image/snack/"+subOpt.getSub_opt_img());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						//URL url = getClass().getResource("/"+subOpt.getSub_opt_img());
						try {
							Image img = ImageIO.read(url);
							buffr.drawImage(img, subOpt.x+25, subOpt.y, subOpt.width-50, subOpt.height-50, this);
						} catch (IOException e) {
							e.printStackTrace();
						}
						buffr.setColor(Color.WHITE);
						buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
						buffr.drawString(subOpt.getSub_opt_name()+"("+subOpt.getSub_opt_size()+")", subOpt.x+10, subOpt.y+180);
						buffr.drawString("(+"+subOpt.getPlus_price()+"��)", subOpt.x+30, subOpt.y+205);
						//buffr.draw(subOpt);
					}
				}else{
					isFirst = false;
					buffr.setColor(new Color(66, 106, 126));
					buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
					buffr.drawString("�����ϰ��� �ϴ� �ɼ� �̹����� �����ּ���.", 140, 80);
				}
				g2.drawImage(buffrImg, 0, 0, 800, 250, this);
			}
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
		};
		bt_confirm = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_select_end.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 50, 200, 50, this);
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
		bt_confirm.setPreferredSize(new Dimension(200, 100));
		
		can_subOpt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				clickSubOpt(point);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				isDragged = true;
				for(int i=0; i<subOpts.size(); i++){
					offX[i] = e.getX() - subOpts.get(i).x;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
			}
		});
		
		can_subOpt.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(isDragged){
					for(int i=0; i<subOpts.size(); i++){
						subOpts.get(i).x = e.getX()-offX[i];
						subOpts.get(i).translate(10, 0);
					}
				}
				can_subOpt.repaint();
			}
		});
		
		bt_confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clickConfirm();
			}
		});
		
		p_price.add(la_priceInfo);
		p_price.add(la_price);
		add(la_comboName);
		add(p_price);
		add(la_optInfo);
		add(p_topOpt);
		add(can_subOpt);
		add(bt_confirm);
	}
	
	public void setSuboptBounds(){
		for(int i=0; i<subOpts.size(); i++){
			subOpts.get(i).setBounds(25+(i*150), 25, 150, 200);
		}
	}
	
	public void clickSubOpt(Point point){
		for(int i=0; i<subOpts.size(); i++){
			SubOption subOpt = subOpts.get(i);
			if(subOpts.get(i).contains(point)){
				for(int j=0; j<topOpts.size(); j++){
					TopOption topOpt = topOpts.get(j);
					if(subOpt.getOpt_size_id() == topOpt.comboList.getSize_id()){
						for(int k=0; k<topOpt.isSelectBuffr.length; k++){
							if(topOpt.isSelectBuffr[k] == false){
								topOpt.isSelectBuffr[k] = true;
								topOpt.selectedId[k] = subOpt.getSub_opt_id();
								topOpt.la_tags.get(k).setText(subOpt.getSub_opt_name()+"("+subOpt.getSub_opt_size()+")");
								
								int price = Integer.parseInt(la_price.getText());
								price -= topOpt.plus_price;
								topOpt.plus_price = subOpt.getPlus_price();
								la_price.setText(Integer.toString(price+topOpt.plus_price));
								
								return;
							}
						}
						
						//���� ���۰� ���� ���ٸ� �ٽ� false�� �ǵ������� ��� ������ ���� ����Ѵ�.
						for(int k=0; k<topOpt.isSelectBuffr.length; k++){
							topOpt.isSelectBuffr[k] = false;
						}
						topOpt.isSelectBuffr[0] = true;
						topOpt.selectedId[0] = subOpt.getSub_opt_id();
						topOpt.la_tags.get(0).setText(subOpt.getSub_opt_name()+"("+subOpt.getSub_opt_size()+")");
						
						int price = Integer.parseInt(la_price.getText());
						price -= topOpt.plus_price;
						topOpt.plus_price = subOpt.getPlus_price();
						la_price.setText(Integer.toString(price+topOpt.plus_price));
					}
				}
			}
		}
	}
	
	public void clickConfirm(){
		ArrayList<Integer> sub_opt_id = new ArrayList<Integer>();
		for(int i=0; i<topOpts.size(); i++){
			for(int j=0; j<topOpts.get(i).selectedId.length; j++){
				if(topOpts.get(i).selectedId[j] == 0){
					isFirst = true;
					can_subOpt.repaint();
					return;
				}else{
					sub_opt_id.add(topOpts.get(i).selectedId[j]);
				}
			}
		}
		main.selectCombo.setSub_opt_id(sub_opt_id);
		main.selectCombo.setPrice(Integer.parseInt(la_price.getText()));
		
		ChoiceConfirmScreen nextScreen = ((ChoiceConfirmScreen)main.screen.get(11));
		if(main.movie == true){
			nextScreen.la_movie_price.setText(Integer.toString(main.selectList.getPrice()));
			nextScreen.selectChoiceProduct(main.selectList.getProduct_id());
		}
		main.combo = true;
		nextScreen.la_combo_price.setText(Integer.toString(main.selectCombo.getPrice()));
		nextScreen.setTotalPrice();
		nextScreen.extractCombo(main.selectCombo.getSub_opt_id());
		nextScreen.createInfo();
		main.setPage(11);
	}
	
	public void selectSubOpt(int size_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select so.sub_opt_id as �ɼǾ��̵�, so.name as �ɼ��̸�, so.price as �ɼǰ���, so.img as �ɼ��̹���, tos.opt_size as �ɼ�ũ��");
		sql.append(" from sub_opt so");
		sql.append(" inner join top_opt_size tos on tos.top_opt_size_id = so.top_opt_size_id");
		sql.append(" where tos.top_opt_size_id = ?");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, size_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				SubOption subOpt = new SubOption();
				subOpt.setSub_opt_id(rs.getInt("�ɼǾ��̵�"));
				subOpt.setSub_opt_name(rs.getString("�ɼ��̸�"));
				subOpt.setSub_opt_img(rs.getString("�ɼ��̹���"));
				subOpt.setPlus_price(rs.getInt("�ɼǰ���"));
				subOpt.setSub_opt_size(rs.getString("�ɼ�ũ��"));
				subOpt.setOpt_size_id(size_id);
				
				subOpts.add(subOpt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
