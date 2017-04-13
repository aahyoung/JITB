package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.user.main.ClientMain;

public class BirthPhonePanel extends JPanel{
	JPanel p_pilot;
	JTextField t_birth;
	JPanel p_phone;
	JTextField t_init;
	JTextField t_phone;
	JPanel p_label;
	JPanel bt_del_birth, bt_del_phone;
	JLabel la_detail1, la_detail2;
	JPanel p_grid;
	Canvas can_line, can_red_line, can_separ;
	
	Image line, red_line, separ, del;

	ClientMain main;
	Image btnImg[];
	NumButton btnPanel[] = new NumButton[12];
	int a[] = new int[10];
	
	StringBuffer birth = new StringBuffer();
	StringBuffer phone = new StringBuffer();
	boolean flag = false;

	public BirthPhonePanel(ClientMain main, Image btnImg[]) {
		this.main = main;
		this.btnImg = btnImg;
		getRandom();

		String str1 = "법정생년월일(6자)과 휴대폰 뒷자리(7~8자)를 입력해주세요.\n";
		String str2 = "비회원으로 예매한 고객님께서는 예매번호로 조회해주세요.";
		
		setLine();
		setDel();
		
		can_line = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(line, 0, 0, 600, 1, this);
			}
		};
		can_red_line = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(red_line, 0, 0, 600, 1, this);
			}
		};
		can_separ = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(separ, 0, 10, 1, 60, this);
			}
		};
		bt_del_birth = new JPanel(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(del, 0, 0, 50, 50, this);
			}
		};
		bt_del_phone = new JPanel(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(del, 0, 0, 50, 50, this);
			}
		};
		p_pilot = new JPanel();
		t_birth = new JTextField("생년월일(6자)") {
			@Override
			public void setBorder(Border border) {
			}
		};
		p_phone = new JPanel();
		t_init = new JTextField("01X") {
			@Override
			public void setBorder(Border border) {
			}
		};
		t_phone = new JTextField("뒷 자리(7~8자)") {
			@Override
			public void setBorder(Border border) {
			}
		};
		p_label = new JPanel();
		la_detail1 = new JLabel(str1);
		la_detail2 = new JLabel(str2);
		p_grid = new JPanel(new GridLayout(4, 3));

		for (int i = 0; i < btnPanel.length; i++) {
			btnPanel[i] = new NumButton(btnImg[i], i);
			btnPanel[i].setPreferredSize(new Dimension(50, 50));
			btnPanel[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NumButton btn = (NumButton)e.getSource();
					
					if(btn.index == 10){
						deleteNum();
					}else if(btn.index == 11){
						main.setPage(10);
					}else{
						insertNum(btn.index);
					}
				}
			});
		}
		
		for(int i=0; i<9; i++){
			p_grid.add(btnPanel[a[i]]);
		}
		p_grid.add(btnPanel[10]);
		p_grid.add(btnPanel[a[9]]);
		p_grid.add(btnPanel[11]);

		t_init.setEditable(false);
		
		t_birth.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_birth.setForeground(Color.WHITE);
		t_birth.setBackground(new Color(33, 33, 33));
		t_init.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_init.setForeground(Color.WHITE);
		t_init.setBackground(new Color(33, 33, 33));
		t_phone.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_phone.setForeground(Color.WHITE);
		t_phone.setBackground(new Color(33, 33, 33));
		la_detail1.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail1.setForeground(Color.WHITE);
		la_detail2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail2.setForeground(Color.WHITE);

		p_phone.setBackground(new Color(33, 33, 33));
		p_label.setBackground(new Color(33, 33, 33));
		p_pilot.setBackground(new Color(33, 33, 33));
		p_grid.setBackground(new Color(33,33,33));

		can_line.setPreferredSize(new Dimension(600, 10));
		can_red_line.setPreferredSize(new Dimension(600, 10));
		can_separ.setPreferredSize(new Dimension(10, 70));
		bt_del_birth.setPreferredSize(new Dimension(50, 50));
		bt_del_phone.setPreferredSize(new Dimension(50, 50));
		t_init.setPreferredSize(new Dimension(100, 100));
		t_phone.setPreferredSize(new Dimension(400, 100));
		t_birth.setPreferredSize(new Dimension(520, 100));
		p_phone.setPreferredSize(new Dimension(600, 100));
		p_label.setPreferredSize(new Dimension(600, 130));
		p_grid.setPreferredSize(new Dimension(300, 500));
		p_pilot.setPreferredSize(new Dimension(600, 900));

		p_phone.add(t_init);
		p_phone.add(can_separ);
		p_phone.add(t_phone);
		p_phone.add(bt_del_phone);
		p_label.add(la_detail1);
		p_label.add(la_detail2);

		p_pilot.add(t_birth);
		p_pilot.add(bt_del_birth);
		p_pilot.add(can_line);
		p_pilot.add(p_phone);
		p_pilot.add(can_red_line);
		p_pilot.add(p_label);
		p_pilot.add(p_grid);

		add(p_pilot);
		
		t_birth.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				flag = false;
			}
		});
		
		t_phone.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				flag = true;
			}
		});
		
		bt_del_birth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				birth.delete(0, birth.length());
				t_birth.setText("생년월일(6자)");
			}
		});
		
		bt_del_phone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				phone.delete(0, phone.length());
				t_phone.setText("뒷 자리(7~8자)");
			}
		});
		
		setBackground(new Color(33, 33, 33));
		setVisible(false);
	}

	public void getRandom() {
		Random r = new Random();
		for (int i=0; i<10; i++) {
			a[i] = r.nextInt(10);
			for (int j=0; j<i; j++) {
				if (a[i]==a[j]) {
					i--;
				}
			}
		}
	}
	
	public void insertNum(int index){
		if(flag == false){
			birth.append(index);
			t_birth.setText(birth.toString());
		}else{
			phone.append(index);
			t_phone.setText(phone.toString());
		}
	}
	
	public void deleteNum(){
		if(flag == false){
			if(birth.length() != 0){
				birth.delete(birth.length()-1, birth.length());
			}
			if(birth.length() == 0){
				t_birth.setText("생년월일(6자)");
			}else{
				t_birth.setText(birth.toString());
			}
		}else{
			if(phone.length() != 0){
				phone.delete(phone.length()-1, phone.length());
			}
			if(phone.length() == 0){
				t_phone.setText("뒷 자리(7~8자)");
			}else{
				t_phone.setText(phone.toString());
			}
		}
	}
	
	public void setLine(){
		URL url1 = getClass().getResource("/line.png");
		URL url2 = getClass().getResource("/red_line.png");
		URL url3 = getClass().getResource("/separ.png");
		try {
			line = ImageIO.read(url1);
			red_line = ImageIO.read(url2);
			separ = ImageIO.read(url3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDel(){
		URL url = getClass().getResource("/remove.png");
		try {
			del = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
