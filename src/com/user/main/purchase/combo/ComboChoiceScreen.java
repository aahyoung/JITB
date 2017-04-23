package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class ComboChoiceScreen extends ScreenFrame{
	JLabel la_comboInfo;
	JLabel la_origin;
	JPanel p_default;
	JPanel p_container;
	Canvas bt_cancle;
	
	public ComboChoiceScreen(ClientMain main) {
		super(main);
		
		la_comboInfo = new JLabel("맛있는 콤보와 함께 영화를 즐겨봐요!", JLabel.CENTER);
		p_default = new JPanel();
		la_origin = new JLabel("팝콘:옥수수(미국산)/즉석구이오징어:몸통(페루산)/나쵸:미국산", JLabel.CENTER);
		p_container = new JPanel();
		bt_cancle = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_combo.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		la_comboInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_origin.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_comboInfo.setForeground(Color.white);
		la_origin.setForeground(Color.WHITE);
		
		la_comboInfo.setPreferredSize(new Dimension(800, 100));
		p_default.setPreferredSize(new Dimension(800, 60));
		p_container.setPreferredSize(new Dimension(800, 400));
		la_origin.setPreferredSize(new Dimension(800, 100));
		bt_cancle.setPreferredSize(new Dimension(200, 50));
		
		p_default.setBackground(new Color(33,33,33));
		p_container.setBackground(new Color(33,33,33));
		setBackground(new Color(33,33,33));
		
		selectCombo();
		
		
		add(la_comboInfo);
		add(p_default);
		add(p_container);
		add(la_origin);
		add(bt_cancle);
	}

	public void selectCombo(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from combo";
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Combo combo = new Combo(
						rs.getInt("combo_id"), 
						rs.getString("name"), 
						rs.getInt("price"), 
						rs.getString("img"));
				combo.setComboList(selectComboList(rs.getInt("combo_id")));
				combo.combo_img.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						main.selectCombo.setCombo_id(combo.combo_id);
						
						OptionChoiceScreen nextScreen = (OptionChoiceScreen)main.screen.get(9);
						for(int i=0; i<combo.comboLists.size(); i++){
							TopOption topOpt = new TopOption(combo.comboLists.get(i));
							
							topOpt.opt_img.addMouseListener(new MouseAdapter(){
								@Override
								public void mouseClicked(MouseEvent e) {
									nextScreen.subOpts.removeAll(nextScreen.subOpts);
									nextScreen.selectSubOpt(topOpt.comboList.getSize_id());
									nextScreen.can_subOpt.repaint();
									System.out.println("나눌렸어??");
								}
							});
							
							nextScreen.p_topOpt.add(topOpt);
							nextScreen.topOpts.add(topOpt);
							if(i < combo.comboLists.size()-1){
								JLabel label = new JLabel("+");
								label.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
								label.setForeground(Color.WHITE);
								nextScreen.p_topOpt.add(label);
							}
						}
						nextScreen.la_comboName.setText(combo.name);
						nextScreen.la_price.setText(Integer.toString(combo.price)+"원");
						
						main.setPage(9);
					}
				});
				
				p_container.add(combo);
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
	
	public ArrayList<ComboList> selectComboList(int combo_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArrayList<ComboList> comboLists = new ArrayList<ComboList>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select cl.combo_list_id as 콤보리스트아이디, cl.amount as 수량,");
		sql.append(" tos.opt_size as 사이즈, tos.top_opt_size_id as 사이즈아이디,");
		sql.append(" topt.name as 탑옵션이름, topt.img as 탑옵션이미지");
		sql.append(" from combo c");
		sql.append(" inner join combo_list cl on cl.combo_id = c.combo_id");
		sql.append(" inner join top_opt_size tos on tos.top_opt_size_id = cl.top_opt_size_id");
		sql.append(" inner join top_opt topt on topt.top_opt_id = tos.top_opt_id");
		sql.append(" where c.combo_id = ?");
		
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setInt(1, combo_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ComboList comboList = new ComboList();
				comboList.setCombo_list_id(rs.getInt("콤보리스트아이디"));
				comboList.setAmount(rs.getInt("수량"));
				comboList.setSize_id(rs.getInt("사이즈아이디"));
				comboList.setSize(rs.getString("사이즈"));
				comboList.setTop_opt_name(rs.getString("탑옵션이름"));
				comboList.setTop_opt_img(rs.getString("탑옵션이미지"));
				comboLists.add(comboList);
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
		
		return comboLists;
	}
}
