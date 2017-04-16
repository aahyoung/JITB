package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jitb.db.DBManager;

/*
 * 1. 레이아웃 구성
 * p_north		되돌아가기 버튼, 영화 추가 버튼 구현
 * p_content	- 영화 목록이 존재하면 			p_list 추가된 영화 목록 출력
 * 				- 영화 목록이 존재하지 않으면 	p_warning 영화 추가 요청 문구 출력
 * 2. 기능 구현
 * 1) 영화 추가 버튼은 internalFrame으로 구현
 * 2) p_list의 영화 리스트는 포스터/상세 보기 버튼 구현
 * 3) 상세 보기 버튼을 누르면 새로운 패널로 이동(뒤돌아가기 버튼을 누르면 p_content가 보이도록)
 * */

public class MovieMain extends JPanel implements ActionListener{
	JPanel p_north, p_content;
	JPanel p_list, p_warning;
	JButton bt_back, bt_add;
	
	JLabel lb_title;
	
	JScrollPane scroll;
	
	// 현재 영화 데이터가 존재하는지 여부 확인
	boolean exist=false;
	
	// DB 연동
	DBManager manager;
	Connection con;
	
	// DB로부터 현재 존재하는 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> movieList=new ArrayList<Movie>();
	
	// 영화 패널을 담아놓을 collection framework
	ArrayList<MovieItem> movies=new ArrayList<MovieItem>();
	
	// 각 영화 패널
	MovieItem movieItem;
	
	// 영화 추가 Dialog
	AddMovie addMovie;

	// default 이미지 파일 경로
	String path="res_manager/";
	
	public MovieMain() {
		// DB 연동
		connect();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_list=new JPanel();
		p_warning=new JPanel();
		
		lb_title=new JLabel("영화 목록");
		
		bt_add=new JButton("영화 추가");
		//bt_back=new JButton("되돌아가기");
		
		// 영화 목록 패널에 scroll 붙이기
		//scroll=new JScrollPane(p_list);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_list);
		
		bt_add.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
		// 영화 목록
		getMovieList();
	}
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
		
	// 현재 존재하는 영화를 DB에서 가져오기
	public void getMovieList(){
		PreparedStatement pstmt_list=null;
		PreparedStatement pstmt_count=null;
		ResultSet rs_count=null;
		ResultSet rs_list=null;
		
		int count=0;
		movieList.removeAll(movieList);
		
		String count_sql="select count(movie_id) from movie";
		String list_sql="select * from movie order by movie_id asc";
		
		try {
			pstmt_count=con.prepareStatement(count_sql);
			rs_count=pstmt_count.executeQuery();
			rs_count.next();
			count=rs_count.getInt("count(movie_id)");
			
			// 현재 영화의 갯수가 0보다 크면, 영화가 존재하면
			if(count>0){
				
				pstmt_list=con.prepareStatement(list_sql);
				rs_list=pstmt_list.executeQuery();
				
				while(rs_list.next()){			
					Movie dto=new Movie();
					dto.setMovie_id(rs_list.getInt("movie_id"));
					dto.setPoster(rs_list.getString("poster"));
					dto.setName(rs_list.getString("name"));
					dto.setDirector(rs_list.getString("director"));
					dto.setMain_actor(rs_list.getString("main_actor"));
					dto.setStory(rs_list.getString("story"));
					dto.setRun_time(rs_list.getInt("run_time"));
					
					movieList.add(dto);
				}
				
				setMovieList();
				
			}
			// 영화가 존재하지 않으면
			else{
				p_list.setVisible(false);
				//p_warning.setVisible(true);
				
				System.out.println("영화 없음");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 영화 패널 설정
	public void setMovieList(){
		movies.removeAll(movies);
		p_list.removeAll();
		
		// 현재 존재하는 영화만큼 생성하고 설정 및 출력
		for(int i=0; i<movieList.size(); i++){				
			Image img;
			try {
				img = ImageIO.read(new File(path+movieList.get(i).getPoster()));
				String name=movieList.get(i).getName();
				
				movieItem=new MovieItem(img, name);
				p_list.add(movieItem);
				
				movies.add(movieItem);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("영화 출력");
	}

	// 영화 추가 버튼
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_add){
			addMovie=new AddMovie(this);
			System.out.println("영화 추가");
		}
		
	}
	
	// 영화 목록 출력
}
