package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.user.main.ClientMain;

public class BookingNumPanel extends JPanel{
	JPanel p_pilot;
	JPanel p_bn;
	JTextField t_init;
	JTextField t_num;
	JPanel p_label;
	JPanel bt_del_num;
	JLabel la_detail1, la_detail2;
	JPanel p_grid;
	Canvas can_line, can_separ;
	
	Image line, separ, del;
	
	ClientMain main;
	Image btnImg[];
	NumButton btnPanel[] = new NumButton[12];
	
	StringBuffer num = new StringBuffer();
	
	public BookingNumPanel(ClientMain main, Image btnImg[]) {
		this.main = main;
		this.btnImg = btnImg;
		
		String str1 = "�� 4�ڸ��� �����ϰ� �� 11�ڸ��� �Է����ּ���";
		String str2 = "���Ź�ȣ�� �𸣽Ŵٸ� �������+�޴�����ȣ�� ��ȸ���ּ���";
		
		setLine();
		setDel();
		
		can_line = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(line, 0, 0, 600, 1, this);
			}
		};
		can_separ = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(separ, 0, 10, 1, 60, this);
			}
		};
		bt_del_num = new JPanel(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(del, 0, 0, 50, 50, this);
			}
		};
		p_pilot = new JPanel();
		p_bn = new JPanel();
		t_init = new JTextField("0057"){
			@Override
			public void setBorder(Border border) {}
		};
		t_num = new JTextField("�� �ڸ�(11��)"){
			@Override
			public void setBorder(Border border) {}
		};
		p_label = new JPanel();
		la_detail1 = new JLabel(str1);
		la_detail2 = new JLabel(str2);
		p_grid = new JPanel(new GridLayout(4, 3));
		
		for(int i=0; i<btnPanel.length; i++){
			btnPanel[i] = new NumButton(btnImg[i], i);
			btnPanel[i].setPreferredSize(new Dimension(50, 50));
			btnPanel[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NumButton btn = (NumButton)e.getSource();
					
					if(btn.index == 10){
						deleteNum();
					}else if(btn.index == 11){
						main.setPage(3);
					}else{
						insertNum(btn.index);
					}
				}
			});
		}
		
		for(int i=0; i<9; i++){
			p_grid.add(btnPanel[i+1]);
		}
		p_grid.add(btnPanel[10]);
		p_grid.add(btnPanel[0]);
		p_grid.add(btnPanel[11]);
		
		t_init.setEditable(false);
		
		t_init.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_init.setForeground(Color.WHITE);
		t_init.setBackground(new Color(33,33,33));
		t_num.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_num.setForeground(Color.WHITE);
		t_num.setBackground(new Color(33,33,33));
		la_detail1.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail1.setForeground(Color.WHITE);
		la_detail2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail2.setForeground(Color.WHITE);
		
		p_bn.setBackground(new Color(33,33,33));
		p_pilot.setBackground(new Color(33,33,33));
		p_label.setBackground(new Color(33,33,33));
		p_grid.setBackground(new Color(33,33,33));
		
		can_line.setPreferredSize(new Dimension(600, 10));
		can_separ.setPreferredSize(new Dimension(10, 70));
		bt_del_num.setPreferredSize(new Dimension(50, 50));
		t_init.setPreferredSize(new Dimension(120, 100));
		t_num.setPreferredSize(new Dimension(380, 100));
		p_bn.setPreferredSize(new Dimension(600, 100));
		p_label.setPreferredSize(new Dimension(600, 130));
		p_grid.setPreferredSize(new Dimension(300, 500));
		p_pilot.setPreferredSize(new Dimension(600, 900));
		
		p_bn.add(t_init);
		p_bn.add(can_separ);
		p_bn.add(t_num);
		p_bn.add(bt_del_num);
		p_label.add(la_detail1);
		p_label.add(la_detail2);
		
		p_pilot.add(p_bn);
		p_pilot.add(can_line);
		p_pilot.add(p_label);
		p_pilot.add(p_grid);
		add(p_pilot);
		
		bt_del_num.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				num.delete(0, num.length());
				t_num.setText("�� �ڸ�(11��)");
			}
		});
		
		setBackground(new Color(33,33,33));
		setVisible(true);
	}
	
	public void insertNum(int index){
		num.append(index);
		t_num.setText(num.toString());
	}
	
	public void deleteNum(){
		if(num.length() != 0){
			num.delete(num.length()-1, num.length());
		}
		if(num.length() == 0){
			t_num.setText("�� �ڸ�(11��)");
		}else{
			t_num.setText(num.toString());
		}
	}
	
	public void setLine(){
		URL url1 = getClass().getResource("/line.png");
		URL url2 = getClass().getResource("/separ.png");
		try {
			line = ImageIO.read(url1);
			separ = ImageIO.read(url2);
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
