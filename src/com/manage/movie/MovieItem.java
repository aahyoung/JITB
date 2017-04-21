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
 * 영화 하나를 표현할 UI 컴포넌트
 * - 영화 포스터
 * - 영화 이름
 * - 상세 보기 버튼
 * */
public class MovieItem extends JPanel implements ActionListener{
	Image img;
	String name, start_date, end_date;
	String type;
	
	Canvas can;
	JLabel la_name, la_start, la_end;
	
	JButton bt_detail;
	
	// 메인 프레임
	MovieMain movieMain;
	
	// 영화 정보 수정/삭제 가능
	EditMovie editMovie;
	
	// 현재 선택한 영화 index
	int index;
	
	public MovieItem(MovieMain movieMain, Image img, String name, String start_date, String end_date) {
		this.movieMain=movieMain;
		// 영화 포스터와 이름은 DB로부터 가져오기
		this.img=img;
		this.name=name;
		this.start_date=start_date;
		this.end_date=end_date;
		
		// 현재 영화 타입 지정(과거/현재/상영 예정)
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
		la_start=new JLabel("개봉 일자 : "+start[0]);
		la_end=new JLabel("종료 일자 : "+end[0]);
		bt_detail=new JButton("상세 보기");
		
		bt_detail.addActionListener(this);
		
		add(can);
		add(la_name);
		add(la_start);
		add(la_end);
		add(bt_detail);
		
		setPreferredSize(new Dimension(150, 270));
		
	}

	// 각 영화 상세보기를 누르면
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		System.out.println(type);
		System.out.println("상세 보기 누름");

		if(type.equals("과거")){
			for(int i=0; i<movieMain.past_movies.size(); i++){
				
				// 선택한 영화의 상세 정보만 띄우기
				if(i==)
				System.out.println(movieMain.past_movies.get(i).name+" 선택");
				
				// 상세 보기를 선택한 영화의 id 구하기
				int movie_id=movieMain.pastList.get(i).getMovie_id();
				
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}

		else if(type.equals("현재")){
			for(int i=0; i<movieMain.present_movies.size(); i++){
				System.out.println(movieMain.present_movies.get(i).name+" 선택");
				
				// 상세 보기를 선택한 영화의 id 구하기
				int movie_id=movieMain.presentList.get(i).getMovie_id();
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}
		else if(type.equals("예정")){
			for(int i=0; i<movieMain.upcoming_movies.size(); i++){
				System.out.println(movieMain.upcoming_movies.get(i).name+" 선택");
				
				// 상세 보기를 선택한 영화의 id 구하기
				int movie_id=movieMain.upcomingList.get(i).getMovie_id();
				
				editMovie=new EditMovie(movieMain, this, movie_id);
			}
		}
		
	}
}
