package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/*
 * 영화 추가 레이아웃
 * 영화 추가 시 DB 연동
 * 1. getTheaterList()		: 현재 할당 가능한 영화관 목록 choice에 대입
 * 2. insertMovie()			: 현재 레이아웃에서 입력한 정보를 movie테이블에 저장
 * 3. getMovieId()			: 가장 최근에 저장된 레코드의 movie_id 얻기
 * 4. setScreeningDate()	: movie_id를 이용해 screening_date테이블에 상영일자 저장
 * 5. getScreeningDateId()	: 가장 최근에 저장된 레코드의 screening_date_id 얻기(반복문으로 돌려 영화 추가 시 insert된 모든 screening_date_id 저장)
 * 6. calStartTime()		: 가장 최근에 저장된 영화 레코드의 run_time을 이용하여 상영 시작 시간 계산
 * 7. setStartTime()		: screening_date_id를 이용해 계산된 상영 시작 시간을 start_time테이블에 저장
 * 8. getStartTimeId()		: 가장 최근에 저장된 레코드의 start_time_id 얻기(반복문으로 돌려 영화 추가 시 insert된 모든 start_time_id 저장)
 * 9. setTheaterOperate()	: start_time_id를 이용해 할당된 상영관 정보를 theater_operate테이블에 저장
 * 10. getTheaterOperateId(): 가장 최근에 저장된 레코드의 theater_opearate_id 얻기(반복문으로 돌려 영화 추가 시 insert된 모든 theater_operate_id 저장)
 * 11. setSeat()			: theater_operate_id를 이용해 상영일-상영시간-상영관-이름-상태 seat테이블에 저장
 * */

public class AddMovie extends JDialog implements ActionListener, FocusListener{
	JPanel p_outer;
	
	JFXPanel p_date;
	
	Canvas can;
	
	JLabel lb_title;
	JTextField t_title;
	JLabel lb_director;
	JTextField t_director;
	JLabel lb_actor;
	JTextField t_actor;
	JLabel story;
	JTextArea ta_story;
	JLabel lb_start_date;
	// Date Picker
	JLabel lb_end_date;
	// Date Picker
	JLabel lb_run_time;
	JTextField t_run_time;
	
	// 할당할 영화관 고르기
	Choice ch_theater;
	
	JButton bt_confirm, bt_cancel;
	
	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	
	File file;
	JFileChooser chooser;
	
	// Date Picker
	private Stage stage;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    
    // DB 연동
    DBManager manager;
    Connection con;
    
    // textArea에 커서 올릴 때 워터마크
    boolean Flag=false;
    
    // textField default값
    String[] txtDefault={
    		"영화 제목을 입력하세요.",
    		"영화 감독을 입력하세요.",
    		"주연 배우를 입력하세요.",
    		"스토리를 입력하세요.",
    		"상영 시간을 입력하세요.",
    };
    
    JTextField[] t_input=new JTextField[5];
    
    ArrayList<TheaterItem> theater=new ArrayList<TheaterItem>();
    
    // 영화관 선택 index
    int theater_index;
    
    // 영화 id
    int movie_id;
    
    // 총 상영일수
    int run_date;
    
    // 영화 상영시간
    int run_time;
    
    // 상영 날짜 id
    ArrayList screening_date_id=new ArrayList();
    
    // 상영 시작시간 id
    //int[] start_time_id=new int[7];
    ArrayList start_time_id=new ArrayList();
    
    // theater_operate id
    ArrayList theater_operate_id=new ArrayList();
    
    // 부모 패널
    MovieMain movieMain;
    
    // 각 날짜별(영화)시작 시간 계산
    String[] start_time=new String[7];
    
    // DB로부터 현재 할당 가능한 영화관 정보를 담아놓을 collection framework
 	ArrayList<Theater> theaterList=new ArrayList<Theater>();
 	
 	LocalDate start_date;
 	LocalDate end_date;
 
	public AddMovie(MovieMain movieMain) {
		this.movieMain=movieMain;
		
		URL url=this.getClass().getResource("/shrek.jpg");
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// 포스터 등록
		can=new Canvas(){
			
			public void paint(Graphics g) {
				// 포스터 붙이기
				g.drawImage(img, 0, 0, 200, 300, this);
			}
		};
		
		can.setPreferredSize(new Dimension(500, 300));
		can.setBounds(0, 0, 200, 300);
		
		chooser=new JFileChooser("C:/Users/user/Pictures/AC3");
		
		can.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				getPoster();
			}
		});
		
		p_outer=new JPanel();
		p_date=new JFXPanel();
		p_date.setLayout(new BorderLayout());
		
		t_title=new JTextField(txtDefault[0],20);
		t_director=new JTextField(txtDefault[1],20);
		t_actor=new JTextField(txtDefault[2],20);
		ta_story=new JTextArea(txtDefault[3],4,20);
		t_run_time=new JTextField(txtDefault[4],20);
		ch_theater=new Choice();
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		t_title.addFocusListener(this);
		t_director.addFocusListener(this);
		t_actor.addFocusListener(this);
		ta_story.addFocusListener(this);
		t_run_time.addFocusListener(this);
		
		createCalendar();
				
		p_date.setLayout(new GridLayout(1, 2));
		p_outer.add(can);
		p_outer.add(t_title);
		p_outer.add(t_director);
		p_outer.add(t_actor);
		p_outer.add(ta_story);
		p_outer.add(p_date);
		p_outer.add(t_run_time);
		p_outer.add(ch_theater);
		p_outer.add(bt_confirm);
		p_outer.add(bt_cancel);
		add(p_outer);
		
		setBounds(250, 50, 500, 600);
		setVisible(true);
		
		// DB 연결
		connect();
		
		// 영화 리스트 가져오기
		getTheaterList();
	}
	
	// DB 연결
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 현재 할당 가능한 영화관 목록 받아오기
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		theaterList.removeAll(theaterList);
		ch_theater.removeAll();
		
		// 서브쿼리 조건 제외 not in
		// theater_operate 테이블의 theater_id에 없는 theater테이블의 theater_id 고르기
		String sql="select * from theater where theater_id not in(select theater_id from theater_operate) order by theater_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
	
			while(rs.next()){			
				Theater dto=new Theater();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setName(rs.getString("name"));
				dto.setRow_line(rs.getInt("row_line"));
				dto.setColumn_line(rs.getInt("column_line"));
				
				theaterList.add(dto);
				
				ch_theater.add(dto.getName()+" 관");
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
	
	// 설정한 영화 정보 저장
	// -> movie 테이블에 데이터 저장
	public void insertMovie(){
		PreparedStatement pstmt=null;
		/*
		// 입력값을 변수에 저장
		String poster=file.getName();
		String title=t_title.getText();
		String director=t_director.getText();
		String actor=t_actor.getText();
		String story=ta_story.getText();
		int run_time=Integer.parseInt(t_run_time.getText());
		*/
		//LocalDate start_date=startDatePicker.getValue();
		//LocalDate end_date=endDatePicker.getValue();
		start_date=startDatePicker.getValue();
		end_date=endDatePicker.getValue();
		
		System.out.println(start_date.toString()+","+end_date.toString());
		
		// movie_id, poster, name, director, main_actor, story, start_date, end_date, run_time
		StringBuffer sql_insert=new StringBuffer();
		sql_insert.append("insert into movie(movie_id, poster, name, director, main_actor, story, run_time)");
		sql_insert.append(" values(seq_movie.nextval, ?, ?, ?, ?, ?, ?)");
		//sql.append("to_date(?,'YYYY-MM-DD'), ");
		//sql.append("to_date(?,'YYYY-MM-DD'), ?)");
		
		// 제약조건(제약조건이 좀 많아유ㅠㅠ)
		/* 1. 이미지명이 제대로 들어오고
		 * 2. 영화 이름이 null이 아니고
		 * 3. 영화 감독이 null이 아니고
		 * 4. 주연 배우가 null이 아니고
		 * 5. 스토리가 null이 아니고
		 * 6. 상영시간이 0보다 커야하고
		 * 7. 개봉일자가 종료일자보다 크면 안되고
		 * */
		//if(file.getName()!=null&&!t_title.getText().equals("")&&!t_director.getText().equals("")&&!t_actor.getText().equals("")&&!ta_story.getText().equals("")&&Integer.parseInt(t_run_time.getText())>0&&start_date.isAfter(end_date)){
			try {
				//pstmt=con.prepareStatement(sql.toString());
				pstmt=con.prepareStatement(sql_insert.toString());
				pstmt.setString(1, file.getName());
				pstmt.setString(2, t_title.getText());
				pstmt.setString(3, t_director.getText());
				pstmt.setString(4, t_actor.getText());
				pstmt.setString(5, ta_story.getText());
				//pstmt.setString(6, startDatePicker.getValue().toString());
				//pstmt.setString(7, endDatePicker.getValue().toString());
				pstmt.setInt(6, Integer.parseInt(t_run_time.getText()));
				
				// 성공적으로 insert했다면 반환값은 1
				int result=pstmt.executeUpdate();
				if(result!=0){
					JOptionPane.showMessageDialog(this, "영화 추가 완료");
					
					// 등록과 동시에 movie_id값 얻기
					getMovieId();
					
					// 등록 완료했으면 포스터 파일 저장
					copyPoster();
					
					// screening_date 테이블 저장
					setScreeningDate(start_date, end_date);
					
					// start_time 테이블 저장
					setStartTime();
					
					// theater_operate 테이블 저장
					setTheaterOperate();
					
					// seat 테이블 저장
					setSeat();
					/*
					// start_time 테이블 저장
					setStartTime();
					
					// theater_operate 테이블 저장
					setTheaterOperate();
					
					// seat 테이블 저장
					setSeat();
					*/
				}
				else{
					JOptionPane.showMessageDialog(this, "영화 추가 실패");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				if(pstmt!=null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		//}
	/*
		else{
			JOptionPane.showMessageDialog(this, "입력값을 제대로 입력해주세요.");
		}
	*/
	}
	
	// 포스터 등록
	public void getPoster(){
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			file=chooser.getSelectedFile();
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	// 영화를 등록하면 포스터 저장
	public void copyPoster(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream("res_manager/"+file.getName());
			
			byte[] b=new byte[1024];
			int flag;
			while(true){
				flag=fis.read(b);
				if(flag==-1){
					break;
				}
				fos.write(b);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	// 영화 등록과 동시에 insert된 movie테이블의 record_id 받기
	public void getMovieId(){
		System.out.println("movie_id얻기");
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_movie.currval from dual";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			movie_id=rs.getInt("currval");
			
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

	// 영화 등록과 동시에 screening_date에 movie_id와 screening_date 생성
	public void setScreeningDate(LocalDate start_date, LocalDate end_date){
		PreparedStatement pstmt_insert=null;
		PreparedStatement pstmt_select=null;
		
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into screening_date(screening_date_id, movie_id, screening_date)");
		sql.append(" values(seq_screening_date.nextval, ?, ");
		sql.append("to_date(?, 'YYYY-MM-DD'))");
		
		Period period=Period.between(start_date, end_date);
		run_date=period.getDays()+1;
		
		String sql2="select seq_screening_date.currval from dual";
		
		try {
			
			for(int i=0; i<run_date; i++){
				pstmt_insert=con.prepareStatement(sql.toString());
				pstmt_insert.setInt(1, movie_id);
				pstmt_insert.setString(2, start_date.plusDays(i).toString());
				pstmt_insert.executeQuery();
				
				pstmt_select=con.prepareStatement(sql2);
				rs=pstmt_select.executeQuery();
				
				rs.next();
				screening_date_id.add(rs.getInt("currval"));
				
				// screening_date_id값 얻기
				//getScreeningDateId();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	// 영화 등록과 동시에 insert된 screening_date테이블의 record_id 받기
	public void getScreeningDateId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		screening_date_id.removeAll(screening_date_id);
		
		String sql="select seq_screening_date.currval from dual";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			screening_date_id.add(rs.getInt("currval"));
			
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
	
	// 영화 등록과 동시에 start_time 테이블에 start_time을 계산하고 screening_date_id를 받아 저장
	public void setStartTime(){
		
		calStartTime();
		
		PreparedStatement pstmt=null;
		
		// 영화별 상영시작시간만큼 insert
		StringBuffer sql=new StringBuffer();
		sql.append("insert into start_time(start_time_id, screening_date_id, start_time)");
		sql.append(" values(seq_start_time.nextval, ?, ?)");
		
		try {
			// 영화 상영 날짜 하나당 start_time배열 길이만큼
			for(int i=0; i<screening_date_id.size(); i++){
				for(int j=0; j<start_time.length; j++){
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setInt(1, (Integer)screening_date_id.get(i));
					pstmt.setString(2, start_time[j]);
					pstmt.executeQuery();
					
					// start_time_id값 얻기
					getStartTimeId();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	// 다음 상영시간을 구하는 메소드
	// screening_date, 상영일자만큼 생성되므로 for문 횟수는 movie_id로 불러온 screening_date 테이블 레코드 수
	public void calStartTime(){
		int run_time;
		int hour;		// 시작 시간을 분 단위로 계산
		int min;		// 시작 분
		int result;		// 다음 시작 시간 : 분 단위의 시작시간 + 상영시간 + 30

		// 표시하기 위한 string 변환
		//String[] start_time=new String[7];
		start_time[0]="09:00";	// 항상 영화의 첫번째 상영 시간은 오전 9시
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select run_time from movie where movie_id=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, movie_id);
			rs=pstmt.executeQuery();
			rs.next();
			run_time=rs.getInt("run_time");
			
			// 상영 시간은 7타임
			for(int i=1; i<7; i++){
				// screening_date id를 이용해서 start_time테이블에 값을 저장해야 함
				// 현재 영화의 상영시간을 이용하여 계산
				/*
				 * 현재 시작 시간 - 09:32
				 * 영화 러닝 타임 - 165분
				 * 다음 시작 시간 - 09:32 + 135분 => 
				 * -> 시작시간*60 + 시작시간 분 => 9*60+32=572
				 * -> 분단위로 변환한 시작시간 + 상영시간 + 30분 쉬는시간 = 다음 시작시간 => 767분
				 * -> 분단위의 다음 시작시간/60 => 12(시)
				 * -> 분단위의 다음 시작시간%60 => 47(분)
				 * => 다음 시작 시간은 12시 47분
				 * */
				String[] divide=start_time[i-1].split(":");
				hour=Integer.parseInt(divide[0])*60;
				min=Integer.parseInt(divide[1]);
				result=hour+min+run_time+20;
				
				// 분 단위가 10보다 작아서 14:2 이렇게 될 때는
				if(result%60<10){
					start_time[i]=result/60+":0"+result%60;
				}
				else{
					start_time[i]=result/60+":"+result%60;
				}

				System.out.println(start_time);
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
	
	// 영화 등록과 동시에 insert된 start_time테이블의 record_id 받기
	public void getStartTimeId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_start_time.currval from dual";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			start_time_id.add(rs.getInt("currval"));
			
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
	
	// 영화의 상영 시간에 따른 관 지정
	public void setTheaterOperate(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();
		
		sql.append("insert into theater_operate(theater_operate_id, theater_id, start_time_id)");
		sql.append(" values(seq_theater_operate.nextval, ?, ?)");
		
		// 현재 선택한 영화관 id 받기
		int theater_id=theaterList.get(ch_theater.getSelectedIndex()).getTheater_id();
		
		try {
			for(int i=0; i<start_time_id.size(); i++){
				pstmt=con.prepareStatement(sql.toString());
				pstmt.setInt(1, theater_id);
				pstmt.setInt(2, (Integer)start_time_id.get(i));
				pstmt.executeQuery();
				
				getTheaterOperateId();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	// theater_operate에 insert될 때마다 theater_operate 테이블의 record_id 받기
	public void getTheaterOperateId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_theater_operate.currval from dual";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			theater_operate_id.add(rs.getInt("currval"));
			
			System.out.print(theater_operate_id+" ");
			
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
	
	//cannot insert NULL into ("JITB"."SEAT"."NAME") 에러 발생
	// 좌석 테이블 지정
	public void setSeat(){
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into seat(seat_id, theater_operate_id, name, status)");
		sql.append(" values(seq_seat.nextval, ?, ?, 1)");
		
		// 행열 구하고 그 행과 열마다 좌석번호 새기기
		int row=theaterList.get(ch_theater.getSelectedIndex()).getRow_line();
		int col=theaterList.get(ch_theater.getSelectedIndex()).getColumn_line();
		
		String[] seatName=new String[row*col];
		
		// 4행 3열인 경우
		// A1 A2 A3
		// B1 B2 B3
		// C1 C2 C3
		// D1 D2 D3
		// seatName[i][j]=Character.toString((char)(65+i));
		// seatName[i][j]+=Integer.toString(j+1);
		int count=0;

		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				seatName[count]=Character.toString((char)(65+i))+Integer.toString(j+1);
				count++;
			}
		}
		
		try {
			// theater_operate_id 하나당 row*col 개의 seat 할당
			for(int i=0; i<theater_operate_id.size(); i++){
				for(int j=0; j<seatName.length; j++){
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setInt(1, (Integer)theater_operate_id.get(i));
					pstmt.setString(2, seatName[j]);
					pstmt.executeQuery();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 하나의 frame을 가지고 사용하므로 기존 값으로 항상 초기화
	public void setDefault(){
		t_title.setText(txtDefault[0]);
		t_director.setText(txtDefault[1]);
		t_actor.setText(txtDefault[2]);
		ta_story.setText(txtDefault[3]);
		t_run_time.setText(txtDefault[4]);
		ch_theater.select(0);
		startDatePicker.setValue(LocalDate.now());
		endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
		
	}
	
	public void createCalendar(){
		Group root=new Group();
		//VBox root=new VBox(20);
		Scene scene=new Scene(root,337,50);
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label checkInlabel = new Label("개봉 일자");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(startDatePicker, 0, 1);
        Label checkOutlabel = new Label("종료 일자");
        gridPane.add(checkOutlabel, 1, 0);
        GridPane.setHalignment(checkOutlabel, HPos.RIGHT);
        gridPane.add(endDatePicker, 1, 1);
        root.getChildren().add(gridPane);
        p_date.setScene(scene);
        
	}
	
	// 확인 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		JButton bt=(JButton)e.getSource();
		
		if(bt==bt_confirm){
			insertMovie();
			setVisible(false);
			movieMain.getMovieList();
			movieMain.p_present.updateUI();
			movieMain.p_present.setVisible(true);
			System.out.println("영화 추가 확인");
		}
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			movieMain.p_present.setVisible(true);
			System.out.println("영화 추가 취소");
		}
	}
	
	/*
	// 영화관 선택
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_theater.getSelectedItem()){
			
		}
		
	}
	*/
	
	// textfield default값 구하기
	public String getTextField(int index){
		return txtDefault[index];
	}

	// 커서를 올렸을 때
	public void focusGained(FocusEvent e) {
		//JTextArea txta=(JTextArea)e.getSource();
		if(e.getComponent().equals(ta_story)){
			JTextArea txta=(JTextArea)e.getSource();
			if(txta.getText().trim().equals(txtDefault[3])){
				txta.setText("");
			}
		}
		
		else{
			JTextField txt=(JTextField)e.getSource();
			
			for(int i=0; i<5; i++){
				String value=getTextField(i);
				if(txt.getText().trim().equals(value)){
					txt.setText("");
				}
			}
		}
	}

	// 커서를 뗐을 때
	public void focusLost(FocusEvent e) {
		
		if(e.getComponent().equals(ta_story)){
			JTextArea txta=(JTextArea)e.getSource();
			
			if(txta.getText().trim().equals("")){
				txta.setText(txtDefault[3]);
			}
		}
		else{
			JTextField txt=(JTextField)e.getSource();
			
			for(int i=0; i<5; i++){
				String value=getTextField(i);
				if(txt.getText().trim().equals("")){
					txt.setText(value);
				}
			}
		}
	}
	
}
