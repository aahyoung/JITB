package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

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
import com.manage.check.DataValidTest;
import com.manage.main.Main;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditMovie extends JDialog implements ActionListener, FocusListener{
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
	
	JButton bt_confirm, bt_cancel;
	
	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	
	File file;
	JFileChooser chooser;
	
	// Date Picker
	Stage stage;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    
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
    
    // 영화 poster
    String poster;
    
    // 총 상영일수
    int run_date;
    
    // 영화 상영시간
    int run_time;
    
    // 선택된 영화의 type을 알아오기
    MovieItem movieItem;
    
    // 부모 패널
    MovieMain movieMain;
 	
 	LocalDate start_date;
 	LocalDate end_date;
 	
 	LocalDate ori_start_date;
 	LocalDate ori_end_date;

 	URL url;
 	
 	String filePath;
 	/*
 	 * 1. 수정
 	 * 과거 상영작 : 영화 상세 정보
 	 * 현재 상영작 : 영화 상세 정보, 종료 일자만 수정 가능(현재 날짜로부터 7일 이후부터만 선택)
 	 * 상영 예정작 : 영화 상세 정보, 개봉/종료 일자 수정 가능
 	 * 
 	 * 2. 삭제
 	 * 상영 예정작 : 현재 날짜로부터 7일 이후가 현재 날짜보다 큰 경우에만 선택 가능
 	 * */
	public EditMovie(MovieMain movieMain, MovieItem movieItem, int movie_id) {
		this.movieMain=movieMain;
		this.movieItem=movieItem;
		this.movie_id=movie_id;
		
		/*
		url=this.getClass().getResource("/shrek.jpg");
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		// 포스터 등록
		can=new Canvas(){
			
			public void paint(Graphics g) {
				// 포스터 붙이기
				g.drawImage(img, 0, 0, 200, 300, this);
			}
		};
		
		can.setPreferredSize(new Dimension(500, 300));
		can.setBounds(0, 0, 200, 300);
		
		chooser=new JFileChooser("C:/Hyeona/myServer/data");
		
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
		p_outer.add(bt_confirm);
		p_outer.add(bt_cancel);
		add(p_outer);
		
		setSize(500, 600);
		setLocationRelativeTo(movieMain);
		setVisible(true);
		
		// DB 연결
		connect();
		
		// 선택한 영화 정보 가져오기
		getMovieData();
		
		// 값이 변경되기 전 개봉 일자와 종료 일자 저장
		ori_start_date=startDatePicker.getValue();
		ori_end_date=endDatePicker.getValue();
		
		//System.out.println("현재 선택한 영화 id : "+movie_id);
	}
	
	// DB 연결
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 영화 정보 불러오기
	public void getMovieData(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from movie where movie_id=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, movie_id);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				t_title.setText(rs.getString("name"));
				t_director.setText(rs.getString("director"));
				t_actor.setText(rs.getString("main_actor"));
				ta_story.setText(rs.getString("story"));
				startDatePicker.setValue(rs.getDate("start_date").toLocalDate());
				endDatePicker.setValue(rs.getDate("end_date").toLocalDate());
				t_run_time.setText(rs.getString("run_time"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 영화 정보 수정
	// -> movie 테이블에 데이터 수정
	public void updateMovie(){
		// 등록 완료했으면 포스터 파일 저장
		copyPoster();
		PreparedStatement pstmt=null;
		
		//System.out.println(start_date.toString()+","+end_date.toString());
		
		// movie_id, poster, name, director, main_actor, story, start_date, end_date, run_time
		StringBuffer sql_update=new StringBuffer();
		sql_update.append("update movie set poster=?, name=?, director=?, main_actor=?, story=?, start_date=?, end_date=?, run_time=?");
		sql_update.append(" where movie_id=?");

		try {
			pstmt=con.prepareStatement(sql_update.toString());
			pstmt.setString(1, file.getName());
			pstmt.setString(2, t_title.getText());
			pstmt.setString(3, t_director.getText());
			pstmt.setString(4, t_actor.getText());
			pstmt.setString(5, ta_story.getText());
			pstmt.setString(6, startDatePicker.getValue().toString());
			pstmt.setString(7, endDatePicker.getValue().toString());
			pstmt.setInt(8, Integer.parseInt(t_run_time.getText()));
			pstmt.setInt(9, movie_id);
			
			// 성공적으로 insert했다면 반환값은 1
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "영화 수정 완료");
				
				// 등록 완료했으면 포스터 파일 저장
				//copyPoster();
				
				System.out.println("updateMovie()실행 완료");
				movieMain.getMovieList();
				movieMain.p_past.updateUI();
				movieMain.p_present.updateUI();
				movieMain.p_upcoming.updateUI();
			}
			else{
				JOptionPane.showMessageDialog(this, "영화 수정 실패");
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
		filePath=file.getAbsolutePath();
		//System.out.println(filePath);
		Main main=Main.getMain();
		main.upload(filePath, "img", "movie/");
		//System.out.println("영화 포스터 저장"+Calendar.getInstance().getTime());
	}
	
	
	// 하나의 frame을 가지고 사용하므로 기존 값으로 항상 초기화
	public void setDefault(){
		t_title.setText(txtDefault[0]);
		t_director.setText(txtDefault[1]);
		t_actor.setText(txtDefault[2]);
		ta_story.setText(txtDefault[3]);
		t_run_time.setText(txtDefault[4]);
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
	
	// 입력값이 유효한지 검사
	public void checkDataFormat(){
		// 입력값을 변수에 저장
		//String poster=file.getName();
		String title=t_title.getText();
		String director=t_director.getText();
		String actor=t_actor.getText();
		String story=ta_story.getText();
		String run_time=t_run_time.getText();
		
		start_date=startDatePicker.getValue();
		end_date=endDatePicker.getValue();
		
		// 입력값 제약조건 함수 호출
		if(file==null){
			JOptionPane.showMessageDialog(this, "영화 포스터가 선택되지 않았습니다.");
			return;
		}
		if(!DataValidTest.isNumber(run_time)){
			JOptionPane.showMessageDialog(this, "상영 시간이 숫자로 입력되지 않았습니다.");
			return;
		}
		if(Integer.parseInt(run_time)<0){
			JOptionPane.showMessageDialog(this, "상영 시간은 양수로 입력해주세요.");
			return;
		}
		// 원래 상영 일자와 입력값이 다르면(변경)
		if(!ori_start_date.equals(start_date)){
			System.out.println(ori_start_date+"->"+start_date);
			// 현재 날짜에서 8일 이후의 날짜와 현재 선택된 날짜 비교해서 
			if(!DataValidTest.inDateRange(start_date)){
				JOptionPane.showMessageDialog(this, "개봉 일자는 "+DataValidTest.yymmdd+" 부터 선택 가능합니다.");
				return;
			}
		}
		// 원래 종료 일자와 입력값이 다르면(변경)
		if(!ori_end_date.equals(end_date)){
			System.out.println(ori_end_date+"->"+end_date);
			// 현재 날짜에서 8일 이후의 날짜와 현재 선택된 날짜 비교해서 
			if(!DataValidTest.inDateRange(end_date)){
				JOptionPane.showMessageDialog(this, "종료 일자는 "+DataValidTest.yymmdd+" 부터 선택 가능합니다.");
				return;
			}
		}
		// 유효성 검사가 끝나면 수정 쿼리 실행
		updateMovie();
	}
	
	// 확인 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		JButton bt=(JButton)e.getSource();
		
		if(bt==bt_confirm){
			checkDataFormat();
			setVisible(false);
			/*
			System.out.println("updateMovie()실행 완료");
			movieMain.getMovieList();
			movieMain.p_past.updateUI();
			movieMain.p_present.updateUI();
			movieMain.p_upcoming.updateUI();
			*/
			//movieMain.p_present.setVisible(true);
			//System.out.println("영화 수정 확인");
		}
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			//movieMain.p_present.setVisible(true);
			//System.out.println("영화 수정 취소");
		}
	}
	
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
				if(txt.getText().trim().equals("")){
					String value=getTextField(i);
					txt.setText(value);
				}
			}
		}
	}
	
}
