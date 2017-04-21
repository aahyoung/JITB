package com.manage.movie;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * ��ȭ �ϳ��� ǥ���� UI ������Ʈ
 * - ��ȭ ������
 * - ��ȭ �̸�
 * - �� ���� ��ư
 * */
public class MovieItem extends JPanel implements ActionListener{
	Image img;
	String name, start_date, end_date;
	String type;
	
	Canvas can;
	JLabel la_name, la_start, la_end;
	
	JButton bt_detail;
	
	// ���� ������
	MovieMain movieMain;
	
	// ��ȭ ���� ����/���� ����
	EditMovie editMovie;
	
	// ���� ������ ��ȭ index
	int index;
	
	public MovieItem(MovieMain movieMain, Image img, String name, String start_date, String end_date) {
		this.movieMain=movieMain;
		// ��ȭ �����Ϳ� �̸��� DB�κ��� ��������
		this.img=img;
		this.name=name;
		this.start_date=start_date;
		this.end_date=end_date;
		
		// ���� ��ȭ Ÿ�� ����(����/����/�� ����)
		String type;
		
		can=new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 120, 150, this);
			}
		};
		
		can.setPreferredSize(new Dimension(120, 150));
		
		String[] start=start_date.split(" ");
		String[] end=end_date.split(" ");
		
		la_name=new JLabel(name);
		la_start=new JLabel("���� ���� : "+start[0]);
		la_end=new JLabel("���� ���� : "+end[0]);
		bt_detail=new JButton("�� ����");
		
		bt_detail.addActionListener(this);
		
		add(can);
		add(la_name);
		add(la_start);
		add(la_end);
		add(bt_detail);
		
		setPreferredSize(new Dimension(150, 270));
		
	}

	// �� ��ȭ �󼼺��⸦ ������
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		System.out.println(type);
		System.out.println("�� ���� ����");

		if(type.equals("����")){
			for(int i=0; i<movieMain.past_movies.size(); i++){
				
				// ������ ��ȭ�� �� ������ ����
				if(i==)
				System.out.println(movieMain.past_movies.get(i).name+" ����");
				
				// �� ���⸦ ������ ��ȭ�� id ���ϱ�
				int movie_id=movieMain.pastList.get(i).getMovie_id();
				
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}

		else if(type.equals("����")){
			for(int i=0; i<movieMain.present_movies.size(); i++){
				System.out.println(movieMain.present_movies.get(i).name+" ����");
				
				// �� ���⸦ ������ ��ȭ�� id ���ϱ�
				int movie_id=movieMain.presentList.get(i).getMovie_id();
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}
		else if(type.equals("����")){
			for(int i=0; i<movieMain.upcoming_movies.size(); i++){
				System.out.println(movieMain.upcoming_movies.get(i).name+" ����");
				
				// �� ���⸦ ������ ��ȭ�� id ���ϱ�
				int movie_id=movieMain.upcomingList.get(i).getMovie_id();
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}
		
	}
}
