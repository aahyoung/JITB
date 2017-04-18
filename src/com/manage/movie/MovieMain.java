package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JComboBox;
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
	JPanel p_list;
	JPanel p_present, p_past;
	JButton bt_back, bt_add;
	
	JComboBox<String> list;
	
	JLabel lb_title;
	
	JScrollPane scroll;
	
	// 현재 영화 데이터가 존재하는지 여부 확인
	boolean exist=false;
	
	// DB 연동
	DBManager manager;
	Connection con;
	
	// end_date와 현재 날짜를 비교하여 얻은 현재 상영작 영화 id
	ArrayList presentId=new ArrayList();

	// end_date와 현재 날짜를 비교하여 얻은 과거 상영작 영화 id
	ArrayList pastId=new ArrayList();
	
	// DB로부터 현재 상영작 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> presentList=new ArrayList<Movie>();

	// DB로부터 과거 상영작 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> pastList=new ArrayList<Movie>();
	
	// 현재 상영작 영화 패널을 담아놓을 collection framework
	ArrayList<MovieItem> present_movies=new ArrayList<MovieItem>();

	// 과거 상영작 영화 패널을 담아놓을 collection framework
	ArrayList<MovieItem> past_movies=new ArrayList<MovieItem>();
	
	// 각 영화 패널
	MovieItem movieItem;
	
	// 영화 추가 Dialog
	AddMovie addMovie;

	// default 이미지 파일 경로
	String path="res_manager/";
	
	PresentMovie present;
	
	PastMovie past;
	
	public MovieMain() {
		// DB 연동
		connect();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_present=new JPanel();
		p_past=new JPanel();
		//present=new PresentMovie();
		//past=new PastMovie();
		
		p_list=new JPanel();
		list=new JComboBox<String>();
		list.addItem("현재 상영작");
		list.addItem("과거 상영작");
		
		lb_title=new JLabel("영화 목록");
		
		bt_add=new JButton("영화 추가");
		
		//bt_back=new JButton("되돌아가기");
		
		// 영화 목록 패널에 scroll 붙이기
		//scroll=new JScrollPane(p_list);
		
		p_list.add(list);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(p_list);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_present.setBackground(Color.green);
		p_past.setBackground(Color.cyan);
		
		p_content.setLayout(new BorderLayout());
		//p_content.add(present);
		//p_content.add(past);
		p_content.add(p_present);
		//p_content.add(p_past);
		
		bt_add.addActionListener(this);
		list.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
		//p_present.setVisible(true);
		//p_past.setVisible(false);
		
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
		PreparedStatement pstmt_count=null;		// 현재 DB에 존재하는 영화 개수 얻기
		PreparedStatement pstmt_result=null;	// select * from movie where movie_id=? 결과, Movie DTO
		ResultSet rs_count=null;
		ResultSet rs_result=null;
		
		int count=0;
		
		// 현재, 과거 상영작 정보 모두 지우기
		presentList.removeAll(presentList);
		pastList.removeAll(pastList);
		
		String count_sql="select count(movie_id) from movie";
		
		String result="select * from movie where movie_id=?";
		
		try {
			// 현재 상영작 Id 구하기
			pstmt_count=con.prepareStatement(count_sql);
			rs_count=pstmt_count.executeQuery();
			rs_count.next();
			count=rs_count.getInt("count(movie_id)");
			
			// 현재 영화의 갯수가 0보다 크면, 영화가 존재하면
			if(count>0){
				
				getPresentList();
				
				for(int i=0; i<presentId.size(); i++){
					pstmt_result=con.prepareStatement(result);
					pstmt_result.setString(1, presentId.get(i).toString());
					rs_result=pstmt_result.executeQuery();
					
					while(rs_result.next()){
						Movie dto=new Movie();
						System.out.println("movie 객체 생성");
						dto.setMovie_id(rs_result.getInt("movie_id"));
						dto.setPoster(rs_result.getString("poster"));
						dto.setName(rs_result.getString("name"));
						dto.setDirector(rs_result.getString("director"));
						dto.setMain_actor(rs_result.getString("main_actor"));
						dto.setStory(rs_result.getString("story"));
						dto.setRun_time(rs_result.getInt("run_time"));
						
						presentList.add(dto);
						System.out.println(presentList.size());
					}
				}
				
				getPastList();
				
				for(int i=0; i<pastId.size(); i++){
					pstmt_result=con.prepareStatement(result);
					pstmt_result.setString(1, pastId.get(i).toString());
					rs_result=pstmt_result.executeQuery();
					
					while(rs_result.next()){
						Movie dto=new Movie();
						dto.setMovie_id(rs_result.getInt("movie_id"));
						dto.setPoster(rs_result.getString("poster"));
						dto.setName(rs_result.getString("name"));
						dto.setDirector(rs_result.getString("director"));
						dto.setMain_actor(rs_result.getString("main_actor"));
						dto.setStory(rs_result.getString("story"));
						dto.setRun_time(rs_result.getInt("run_time"));
						
						pastList.add(dto);
					}
				}

				// 현재 상영작 리스트 출력
				setMovieList(presentList, present_movies, p_present);
				
				// 과거 상영작 리스트 출력
				setMovieList(pastList, past_movies, p_past);
	
			}
			// 영화가 존재하지 않으면
			else{
				p_present.setVisible(false);
				//p_warning.setVisible(true);
				
				System.out.println("영화 없음");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	// 현재 상영작 id 불러오기
	public void getPresentList(){
		presentId.removeAll(presentId);
		
		PreparedStatement pstmt_present=null;	// 현재 상영 영화 목록 id들
		ResultSet rs_present=null;
		
		StringBuffer present_sql=new StringBuffer();
		present_sql.append("select m.movie_id from movie m, screening_date s");
		present_sql.append(" where m.movie_id=s.movie_id group by m.movie_id");
		present_sql.append(" having(max(screening_date)>=to_char(sysdate, 'YYYY-MM-DD'))");
		
		try {
			// 현재 상영작 id 저장
			pstmt_present=con.prepareStatement(present_sql.toString());
			rs_present=pstmt_present.executeQuery();
			
			while(rs_present.next()){			
				presentId.add(rs_present.getInt("movie_id"));
				System.out.println(presentId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs_present!=null){
				try {
					rs_present.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt_present!=null){
				try {
					pstmt_present.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 과거 상영작 id 불러오기
	public void getPastList(){
		pastId.removeAll(pastId);
		
		PreparedStatement pstmt_past=null;		// 과거 상영 영화 목록 id들
		ResultSet rs_past=null;
		
		StringBuffer past_sql=new StringBuffer();
		past_sql.append("select m.movie_id  from movie m, screening_date s");
		past_sql.append(" where m.movie_id=s.movie_id group by m.movie_id");
		past_sql.append(" having(max(screening_date)<to_char(sysdate, 'YYYY-MM-DD'))");
		
		try {
			// 과거 상영작 id 저장
			pstmt_past=con.prepareStatement(past_sql.toString());
			rs_past=pstmt_past.executeQuery();
			
			while(rs_past.next()){
				pastId.add(rs_past.getInt("movie_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs_past!=null){
				try {
					rs_past.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt_past!=null){
				try {
					pstmt_past.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	// 영화 패널 설정
	public void setMovieList(ArrayList<Movie> movieList, ArrayList<MovieItem> movies, JPanel panel){
		movies.removeAll(movies);
		panel.removeAll();
		
		System.out.println("movieList 크기 : "+movieList.size());
		// 현재 존재하는 영화만큼 생성하고 설정 및 출력
		for(int i=0; i<movieList.size(); i++){				
			Image img;
			try {
				img = ImageIO.read(new File(path+movieList.get(i).getPoster()));
				String name=movieList.get(i).getName();
				
				movieItem=new MovieItem(img, name);
				panel.add(movieItem);
				
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
		else if(obj==list){	
			
			// 현재 상영작
			if(list.getSelectedIndex()==0){
				System.out.println(presentList.size());
				//p_present.setVisible(true);
				//p_past.setVisible(false);
				p_content.remove(p_past);
				p_content.add(p_present);
				p_content.updateUI();
			}
			// 과거 상영작
			else if(list.getSelectedIndex()==1){
				System.out.println(pastList.size());
				//p_present.setVisible(false);
				//p_past.setVisible(true);
				p_content.remove(p_present);
				p_content.add(p_past);
				p_content.updateUI();
			}
			
		}
		
	}
	
	// 영화 목록 출력
}
