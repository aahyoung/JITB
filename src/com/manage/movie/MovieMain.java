package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jitb.db.DBManager;

import oracle.sql.DATE;

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
	JPanel p_form, p_list;
	JPanel p_upcoming, p_present, p_past;
	JButton bt_back, bt_form, bt_load, bt_add;
	
	JComboBox<String> list;
	
	JLabel lb_title;
	
	JTable table;
	JScrollPane scroll;
	
	// 현재 영화 데이터가 존재하는지 여부 확인
	boolean exist=false;
	
	// DB 연동
	DBManager manager;
	Connection con;
	
	// end_date와 현재 날짜를 비교하여 얻은 상영 예정 영화 id
	ArrayList upcomingId=new ArrayList();
	
	// end_date와 현재 날짜를 비교하여 얻은 현재 상영작 영화 id
	ArrayList presentId=new ArrayList();

	// end_date와 현재 날짜를 비교하여 얻은 과거 상영작 영화 id
	ArrayList pastId=new ArrayList();
	
	// DB로부터 상영 예정 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> upcomingList=new ArrayList<Movie>();
	
	// DB로부터 현재 상영작 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> presentList=new ArrayList<Movie>();

	// DB로부터 과거 상영작 영화 정보를 담아놓을 collection framework
	ArrayList<Movie> pastList=new ArrayList<Movie>();
	
	// 상영 예정 영화 패널을 담아놓을 collection framework
	ArrayList<MovieItem> upcoming_movies=new ArrayList<MovieItem>();
	
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
	
	//TheaterMain theaterMain;
	// 최상위 main
	MovieTheaterTab movieTheaterTab;
	
	// 지점 이름
	String branch_name;
	
	// 각 Excel 상영날짜에 대한 상영중인 영화를 담을 collection framework
	ArrayList<Movie> excelMovie=new ArrayList<Movie>();
	
	public MovieMain(MovieTheaterTab movieTheaterTab) {
		this.movieTheaterTab=movieTheaterTab;
		
		// DB 연동
		connect();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_upcoming=new JPanel();
		p_present=new JPanel();
		p_past=new JPanel();
		//present=new PresentMovie();
		//past=new PastMovie();
		
		p_form=new JPanel();
		p_list=new JPanel();
		
		list=new JComboBox<String>();
		list.addItem("현재 상영작");
		list.addItem("상영 예정작");
		list.addItem("과거 상영작");
		
		lb_title=new JLabel("영화 목록");
		
		bt_form=new JButton("상영시간표 양식");
		bt_load=new JButton("상영시간표 등록");
		bt_add=new JButton("새로운 영화 추가");
		
		//bt_back=new JButton("되돌아가기");
		
		// 영화 목록 패널에 scroll 붙이기
		//scroll=new JScrollPane(p_list);
		
		p_form.add(bt_form);
		p_form.add(bt_load);
		p_form.add(bt_add);
		p_list.add(list);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(p_list);
		p_north.add(p_form, BorderLayout.EAST);
		
		//p_present.setBackground(Color.green);
		//p_past.setBackground(Color.cyan);
		
		p_content.setLayout(new BorderLayout());
		//p_content.add(present);
		//p_content.add(past);
		//p_content.add(bt_add, BorderLayout.EAST);
		p_content.add(p_present);
		//p_content.add(p_past);
		
		bt_form.addActionListener(this);
		bt_load.addActionListener(this);
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
	
	// 현재 정보를(지점, 영화관, 등등) DB에서 가져오기
	public void getInfo(String date){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		excelMovie.removeAll(excelMovie);
		
		StringBuffer sql=new StringBuffer();
		// 현재 지점 정보 얻기
		sql.append("select b.name from theater t, branch b where t.branch_id=b.branch_id group by b.name");
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			rs.next();
			branch_name=rs.getString("name");
			
			// 상영 날짜와 비교하여 현재 상영중인 영화 목록 얻기
			sql.delete(0, sql.length());
			sql.append("select * from movie where start_date<=? and end_date>=?");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, date);
			pstmt.setString(2, date);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				Movie dto=new Movie();

				dto.setMovie_id(rs.getInt("movie_id"));
				dto.setPoster(rs.getString("poster"));
				dto.setName(rs.getString("name"));
				dto.setDirector(rs.getString("director"));
				dto.setMain_actor(rs.getString("main_actor"));
				dto.setStory(rs.getString("story"));
				dto.setStart_date(rs.getString("start_date"));
				dto.setEnd_date(rs.getString("end_date"));
				dto.setRun_time(rs.getInt("run_time"));
				
				excelMovie.add(dto);
				System.out.println(excelMovie.size());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
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
				
				getUpcommingList();
				
				for(int i=0; i<upcomingId.size(); i++){
					pstmt_result=con.prepareStatement(result);
					pstmt_result.setString(1, upcomingId.get(i).toString());
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
						dto.setStart_date(rs_result.getString("start_date"));
						dto.setEnd_date(rs_result.getString("end_date"));
						dto.setRun_time(rs_result.getInt("run_time"));
						
						upcomingList.add(dto);
						System.out.println(upcomingList.size());
					}
				}
				
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
						dto.setStart_date(rs_result.getString("start_date"));
						dto.setEnd_date(rs_result.getString("end_date"));
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
						dto.setStart_date(rs_result.getString("start_date"));
						dto.setEnd_date(rs_result.getString("end_date"));
						dto.setRun_time(rs_result.getInt("run_time"));
						
						pastList.add(dto);
					}
				}
				
				// 상영 예정작 리스트 출력
				setMovieList(upcomingList, upcoming_movies, p_upcoming);

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
	
	// 상영 예정작 id 불러오기
	public void getUpcommingList(){
		upcomingId.removeAll(upcomingId);
		
		PreparedStatement pstmt_upcoming=null;	// 현재 상영 영화 목록 id들
		ResultSet rs_upcoming=null;
		
		StringBuffer upcoming_sql=new StringBuffer();
		upcoming_sql.append("select movie_id from movie");
		upcoming_sql.append(" where start_date>to_char(sysdate, 'YYYY-MM-DD')");
		
		try {
			// 현재 상영작 id 저장
			pstmt_upcoming=con.prepareStatement(upcoming_sql.toString());
			rs_upcoming=pstmt_upcoming.executeQuery();
			
			while(rs_upcoming.next()){			
				upcomingId.add(rs_upcoming.getInt("movie_id"));
				System.out.println(upcomingId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs_upcoming!=null){
				try {
					rs_upcoming.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt_upcoming!=null){
				try {
					pstmt_upcoming.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 현재 상영작 id 불러오기
	public void getPresentList(){
		presentId.removeAll(presentId);
		
		PreparedStatement pstmt_present=null;	// 현재 상영 영화 목록 id들
		ResultSet rs_present=null;
		
		StringBuffer present_sql=new StringBuffer();
		present_sql.append("select movie_id from movie");
		present_sql.append(" where end_date>to_char(sysdate, 'YYYY-MM-DD')");
		
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
		past_sql.append("select movie_id from movie");
		past_sql.append(" where end_date<to_char(sysdate, 'YYYY-MM-DD')");
		
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
	
	// 상영시간표 양식 저장 및 다운(일단은 저장만 가능한 상태, 서버 구축되면 서버에 올릴 예정)
	public void saveExcelForm(){
		System.out.println("상영시간표 양식");
		
		if(present_movies.size()==0){
			JOptionPane.showMessageDialog(this, "현재 상영중인 영화가 없습니다.");
			return;
		}
		HSSFRow row;
		HSSFCell cell = null;
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		
		// sheet명 설정
		// 상영 날짜마다 새로운 sheet 생성(현재 날짜로부터 7일 후까지 설정 가능)
		HSSFSheet[] sheet=new HSSFSheet[7];
		
		Calendar cal=Calendar.getInstance();
		String date;
		//cal.add(cal.DATE, 7);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=0; i<sheet.length; i++){
			
			date=format.format(cal.getTime());
			sheet[i]=workbook.createSheet(date);
			cal.add(cal.DATE, 1);
			getInfo(date);
			
			// 테이블 형태를 모르는 관리자에게 간단한 정보 알려주기 위한 양식 추가
			// 기본 row는 100줄로 설정(CGV 신촌아트레온 보면 70줄 넘어감ㅜㅜ)
			for(int j=0; j<100; j++){
				// 출력 row 생성
				row=sheet[i].createRow(j);
				for(int k=0; k<14; k++){
					if(j==0){
						row.createCell(0).setCellValue("CGV "+branch_name);
					}
					else if(j==1){
						row.createCell(0).setCellValue("현재 상영 날짜");
						row.createCell(1).setCellValue(date);
					}
					else if(j==2){
						row.createCell(0).setCellValue("상영중인 영화 목록");
						row.createCell(6).setCellValue("상영관 목록");
						row.createCell(10).setCellValue("상영 시간표");
					}
					else if(j==3){
						row.createCell(0).setCellValue("번호");
						row.createCell(1).setCellValue("제목");
						row.createCell(2).setCellValue("개봉 일자");
						row.createCell(3).setCellValue("종료 일자");
						row.createCell(4).setCellValue("상영 시간");
						
						row.createCell(6).setCellValue("번호");
						row.createCell(7).setCellValue("이름");
						row.createCell(8).setCellValue("좌석수");
						
						row.createCell(10).setCellValue("관");
						row.createCell(11).setCellValue("날짜");
						row.createCell(12).setCellValue("시간");
						row.createCell(13).setCellValue("영화");
					}
					
					// 상영중인 영화 목록
					for(int movie_cnt=0; movie_cnt<excelMovie.size(); movie_cnt++){
						if(j==4+movie_cnt){
							row.createCell(0).setCellValue(excelMovie.get(movie_cnt).getMovie_id());
							row.createCell(1).setCellValue(excelMovie.get(movie_cnt).getName());
							row.createCell(2).setCellValue(excelMovie.get(movie_cnt).getStart_date());
							row.createCell(3).setCellValue(excelMovie.get(movie_cnt).getEnd_date());
							row.createCell(4).setCellValue(excelMovie.get(movie_cnt).getRun_time());
						}
					}
					
					// 상영관 목록
					for(int theater_cnt=0; theater_cnt<movieTheaterTab.theaterMain.theaterList.size(); theater_cnt++){
						if(j==4+theater_cnt){
							row.createCell(6).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getTheater_id());
							row.createCell(7).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getName()+"관");
							row.createCell(8).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getCount());
						}
					}
				}
			}
			
			// cell 가운데 정렬
			/*
			CellStyle center=workbook.createCellStyle();
			center.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(center);
			*/
			
			FileOutputStream fos;
			try {
				fos=new FileOutputStream("res_manager/상영시간표.xls");
				workbook.write(fos);
				fos.close();
				
				//JOptionPane.showMessageDialog(this, "상영시간표 양식 다운 완료");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				
			}
		}

	}

	// Excel파일로부터 상영시간표 등록
	public void setSchedule(){
		FileInputStream fis;
		try {
			fis=new FileInputStream("res_manager/상영시간표.xls");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
				String start_date=movieList.get(i).getStart_date();
				String end_date=movieList.get(i).getEnd_date();
				
				Date startToDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(start_date);
				Date endToDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(end_date);
				Calendar c=Calendar.getInstance();
				Date today=new Date();		// 오늘 날짜
				
				// the value 0 if the argument Date is equal to this Date; 
				// a value less than 0 if this Date is before the Date argument; 
				// and a value greater than 0 if this Date is after the Date argument.
				int end=endToDate.compareTo(today);
				int start=startToDate.compareTo(today);
				int upcoming=startToDate.compareTo(today);
				
				movieItem=new MovieItem(this, img, name, start_date, end_date);
				
				// 과거 상영작(종료 일자가 오늘보다 작은 경우)
				if(start<0 && end<0){
					System.out.println("과거");
					movieItem.type="과거";
					// 선택한 각 영화의 index를 넘겨줘야하는데ㅜㅜ
					//movieItem.index=
				}
				// 현재 상영작(시작 일자가 오늘보다 작거나 같고 종료 일자가 오늘보다 큰 경우)
				else if(start<=0 && end>0){
					System.out.println("현재");
					movieItem.type="현재";
				}
				// 상영 예정작(시작 일자가 오늘보다 큰 경우)
				else if(start>0 && end>0){
					System.out.println("예정");
					movieItem.type="예정";
				}
				
				panel.add(movieItem);
				
				movies.add(movieItem);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} 
		}
		System.out.println("영화 출력");
	}

	// 영화 추가 버튼
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_form){
			saveExcelForm();
			JOptionPane.showMessageDialog(this, "상영시간표 양식 다운 완료");
		}
		
		else if(obj==bt_load){
			setSchedule();
		}
		
		else if(obj==bt_add){
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
				p_content.remove(p_upcoming);
				p_content.updateUI();				
			}
			// 상영 예정작
			else if(list.getSelectedIndex()==1){
				System.out.println(upcomingList.size());
				//p_present.setVisible(true);
				//p_past.setVisible(false);
				p_content.remove(p_past);
				p_content.remove(p_present);
				p_content.add(p_upcoming);
				p_content.updateUI();
			}
			// 과거 상영작
			else if(list.getSelectedIndex()==2){
				System.out.println(pastList.size());
				//p_present.setVisible(false);
				//p_past.setVisible(true);
				p_content.remove(p_present);
				p_content.add(p_past);
				p_content.remove(p_upcoming);
				p_content.updateUI();
			}
				
		}
		
	}

}
