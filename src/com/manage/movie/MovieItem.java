package com.manage.movie;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * 영화 하나를 표현할 UI 컴포넌트
 * - 영화 포스터
 * - 영화 이름
 * - 상세 보기 버튼
 * */
public class MovieItem extends JPanel{
	Image img;
	String name;
	
	Canvas can;
	JLabel la_name;
	
	JButton bt_detail;
	
	public MovieItem(Image img, String name) {
		// 영화 포스터와 이름은 DB로부터 가져오기
		this.img=img;
		this.name=name;
		
		can=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 100, 150, this);
			}
		};
		
		can.setPreferredSize(new Dimension(100, 150));
		
		la_name=new JLabel(name);
		bt_detail=new JButton("상세 보기");
		
		add(can);
		add(la_name);
		add(bt_detail);
		
		setPreferredSize(new Dimension(100, 220));
		
	}
}
