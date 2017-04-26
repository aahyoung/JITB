package com.user.main.purchase;

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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class ApplyPointPanel  extends PurchasePanelFrame{
	JPanel p_point;
	JLabel la_warning;
	JLabel la_point_info;
	JLabel la_point;
	JLabel la_p;
	JLabel la_apply_info;
	JTextField t_apply;
	Canvas all_apply;
	Canvas can_btn;
	Canvas bt_apply;
	
	ArrayList<NumButton> btns = new ArrayList<NumButton>();
	StringBuffer str = new StringBuffer();
	
	UserPoint point;
	
	public ApplyPointPanel(ClientMain main) {
		super(main);
		
		p_point = new JPanel();
		la_warning = new JLabel();
		la_point_info = new JLabel("사용가능 포인트");
		la_point = new JLabel("0", JLabel.RIGHT);
		la_p = new JLabel("p", JLabel.LEFT);
		la_apply_info = new JLabel("적용 포인트");
		t_apply = new JTextField(){
			@Override
			public void setBorder(Border border) {
			}
		};
		all_apply = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_all_use.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 160, 60, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		bt_apply = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_apply.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		int x = 0;
		int y = 0;
		
		for(int i=1; i<10; i++){
			if(x == 3){
				x = 0;
				y++;
			}			
			NumButton btn = new NumButton(Integer.toString(i));
			
			btn.setBounds(85+x*150, y*80, 150, 80);
			btns.add(btn);
			
			x++;
		}
		x = 0;
		y++;
		NumButton btn1 = new NumButton("");
		btn1.setBounds(85+x*150, y*80, 150, 80);
		btns.add(btn1);
		x++;
		
		NumButton btn2 = new NumButton(Integer.toString(0));
		btn2.setBounds(85+x*150, y*80, 150, 80);
		btns.add(btn2);
		x++;
		
		NumButton btn3 = new NumButton("del");
		btn3.setBounds(85+x*150, y*80, 150, 80);
		btns.add(btn3);
		
		can_btn = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
				for(int i=0; i<btns.size(); i++){
					g2.setColor(new Color(204,204,204));
					g2.draw(btns.get(i));
					//g2.setColor(Color.WHITE);
					if(btns.get(i).index.equals("del")){
						g2.drawString(btns.get(i).index, btns.get(i).x+50, btns.get(i).y+50);
					}else{
						g2.drawString(btns.get(i).index, btns.get(i).x+65, btns.get(i).y+50);
					}
				}
			}
		};
		
		t_apply.setEditable(false);
		t_apply.setText("0");
		
		la_warning.setFont(new Font("Malgun Gothic", Font.PLAIN, 23));
		la_point_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 27));
		la_point.setFont(new Font("Malgun Gothic", Font.PLAIN, 27));
		la_p.setFont(new Font("Malgun Gothic", Font.PLAIN, 27));
		la_apply_info.setFont(new Font("Malgun Gothic", Font.PLAIN, 27));
		t_apply.setFont(new Font("Malgun Gothic", Font.PLAIN, 27));
		
		p_point.setBackground(new Color(33,33,33));
		can_btn.setBackground(new Color(33,33,33));
		t_apply.setBackground(Color.BLACK);
		
		la_warning.setForeground(new Color(165, 0, 0));
		la_point_info.setForeground(Color.WHITE);
		la_point.setForeground(Color.WHITE);
		la_p.setForeground(Color.WHITE);
		la_apply_info.setForeground(Color.WHITE);
		t_apply.setForeground(Color.WHITE);
		
		p_point.setPreferredSize(new Dimension(600, 250));
		la_warning.setPreferredSize(new Dimension(600, 50));
		la_point_info.setPreferredSize(new Dimension(200, 60));
		la_point.setPreferredSize(new Dimension(330, 60));
		la_p.setPreferredSize(new Dimension(40, 60));//570
		la_apply_info.setPreferredSize(new Dimension(220, 60));
		t_apply.setPreferredSize(new Dimension(190, 60));
		all_apply.setPreferredSize(new Dimension(160, 60)); //350
		can_btn.setPreferredSize(new Dimension(600, 350));
		bt_apply.setPreferredSize(new Dimension(200, 50));
		
		p_point.add(la_warning);
		p_point.add(la_point_info);
		p_point.add(la_point);
		p_point.add(la_p);
		p_point.add(la_apply_info);
		p_point.add(t_apply);
		p_point.add(all_apply);
		
		can_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				
				for(int i=0; i<btns.size(); i++){
					if(btns.get(i).contains(point)){
						String index = btns.get(i).index;
						if(index.equals("del")){
							if(str.length() == 0){
								t_apply.setText("0");
							}else{
								str.delete(str.length()-1, str.length());
								if(str.length() == 0){
									t_apply.setText("0");
								}else{
									t_apply.setText(str.toString());
								}
							}
						}else if(index.equals("")){
							
						}else{
							str.append(index);
							t_apply.setText(str.toString());
						}
					}
				}
				
			}
		});
		
		all_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				t_apply.setText(la_point.getText());
				str.delete(0, str.length());
				str.append(t_apply.getText());
			}
		});
		
		bt_apply.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaymentScreen screen = (PaymentScreen)main.screen.get(12);
				PurchasePanel nextPanel = (PurchasePanel)screen.content.get(2);

				if(Integer.parseInt(la_point.getText()) < Integer.parseInt(t_apply.getText())){
					la_warning.setText("입력하신 포인트가 사용가능 포인트를 넘어섰습니다");
				}else if(Integer.parseInt(screen.la_total_price.getText()) < Integer.parseInt(t_apply.getText())){
					la_warning.setText("총 할인금액이 결제금액을 초과하였습니다");
				}else{
					//할인금액 반영
					screen.la_disc_price.setText(t_apply.getText());
					int total = Integer.parseInt(screen.la_disc_price.getText())-Integer.parseInt(screen.la_disc_price.getText());
					screen.la_remain_price.setText(Integer.toString(total));
					
					point.setUse_point(Integer.parseInt(t_apply.getText()));
					
					//결제 후 포인트 삭감을 위한 정보 넘기기
					nextPanel.point = point;
					nextPanel.isPoint = true;
					
					//discount_type_id 넣기
					if(main.movie){
						main.selectList.setDiscount_type_id(point.getDiscount_type_id());
					}
					if(main.combo){
						main.selectCombo.setDiscount_type_id(point.getDiscount_type_id());
					}
					
					screen.setImg(2);
					screen.stepInfo.repaint();
					screen.setPanel(2);
				}
			}
		});
		
		add(p_point);
		add(can_btn);
		add(bt_apply);
	}
}
