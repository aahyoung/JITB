package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jitb.db.DBManager;
import com.jitb.file.FileUtil;
import com.manage.main.Main;

/*
 * 1. ���̾ƿ� ����
 * p_north		�ǵ��ư��� ��ư, ��ȭ �߰� ��ư ����
 * p_content	- ��ȭ ����� �����ϸ� 			p_list �߰��� ��ȭ ��� ���
 * 				- ��ȭ ����� �������� ������ 	p_warning ��ȭ �߰� ��û ���� ���
 * 2. ��� ����
 * 1) ��ȭ �߰� ��ư�� internalFrame���� ����
 * 2) p_list�� ��ȭ ����Ʈ�� ������/�� ���� ��ư ����
 * 3) �� ���� ��ư�� ������ ���ο� �гη� �̵�(�ڵ��ư��� ��ư�� ������ p_content�� ���̵���)
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
	
	// ���� ��ȭ �����Ͱ� �����ϴ��� ���� Ȯ��
	boolean exist=false;
	
	// DB ����
	DBManager manager;
	Connection con;
	
	// end_date�� ���� ��¥�� ���Ͽ� ���� �� ���� ��ȭ id
	ArrayList upcomingId=new ArrayList();
	
	// end_date�� ���� ��¥�� ���Ͽ� ���� ���� ���� ��ȭ id
	ArrayList presentId=new ArrayList();

	// end_date�� ���� ��¥�� ���Ͽ� ���� ���� ���� ��ȭ id
	ArrayList pastId=new ArrayList();
	
	// DB�κ��� �� ���� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> upcomingList=new ArrayList<Movie>();
	
	// DB�κ��� ���� ���� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> presentList=new ArrayList<Movie>();

	// DB�κ��� ���� ���� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> pastList=new ArrayList<Movie>();
	
	// �� ���� ��ȭ �г��� ��Ƴ��� collection framework
	ArrayList<MovieItem> upcoming_movies=new ArrayList<MovieItem>();
	
	// ���� ���� ��ȭ �г��� ��Ƴ��� collection framework
	ArrayList<MovieItem> present_movies=new ArrayList<MovieItem>();

	// ���� ���� ��ȭ �г��� ��Ƴ��� collection framework
	ArrayList<MovieItem> past_movies=new ArrayList<MovieItem>();
	
	// �� ��ȭ �г�
	MovieItem movieItem;
	
	// ��ȭ �߰� Dialog
	AddMovie addMovie;

	// default �̹��� ���� ���
	String path="res_manager/";
	
	//TheaterMain theaterMain;
	
	// ��ȭ ���� main	
	MovieTheaterTab movieTheaterTab;
	
	// ���� �̸�
	String branch_name;
	
	// �� Excel �󿵳�¥�� ���� ������ ��ȭ�� ���� collection framework
	ArrayList<Movie> excelMovie=new ArrayList<Movie>();
	
	// �ϳ��� sheet�� �� row�� ���� �󿵽ð�ǥ ������(�� or ��¥  or �ð� or ��ȭ)
	// theater_name,screening_date,start_time,movie_name
	ArrayList<String> excelRow;
	
	// �ϳ��� sheet�� �󿵽ð�ǥ ������(��/��¥/�ð�/��ȭ)�� ��Ƴ��� collection framework
	ArrayList<ArrayList> excelSheet;
	
	// �ϳ��� Excel���� �󿵽ð�ǥ�� ��Ƴ��� collection framework
	ArrayList<ArrayList> excelData=new ArrayList<ArrayList>();
	
	//URL url_image;
	
	public MovieMain(MovieTheaterTab movieTheaterTab) {
		this.movieTheaterTab=movieTheaterTab;
		
		// DB ����
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
		list.addItem("���� ����");
		list.addItem("�� ������");
		list.addItem("���� ����");
		
		lb_title=new JLabel("��ȭ ���");
		
		bt_form=new JButton("�󿵽ð�ǥ ���");
		bt_load=new JButton("�󿵽ð�ǥ ���");
		bt_add=new JButton("���ο� ��ȭ �߰�");
		
		//bt_back=new JButton("�ǵ��ư���");
		
		// ��ȭ ��� �гο� scroll ���̱�
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
		
		// ��ȭ ���
		getMovieList();
	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// ���� ������(����, ��ȭ��, ���) DB���� ��������
	public void getInfo(String date){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		excelMovie.removeAll(excelMovie);
		
		StringBuffer sql=new StringBuffer();
		// ���� ���� ���� ���
		sql.append("select b.name from theater t, branch b where t.branch_id=b.branch_id group by b.name");
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			rs.next();
			branch_name=rs.getString("name");
			
			// �� ��¥�� ���Ͽ� ���� ������ ��ȭ ��� ���
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
			
			// product ���̺� ���� �˾ƿ���
			sql.delete(0, sql.length());
			
			
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
		
	// ���� �����ϴ� ��ȭ�� DB���� ��������
	public void getMovieList(){
		PreparedStatement pstmt_count=null;		// ���� DB�� �����ϴ� ��ȭ ���� ���
		PreparedStatement pstmt_result=null;	// select * from movie where movie_id=? ���, Movie DTO
		ResultSet rs_count=null;
		ResultSet rs_result=null;
		
		int count=0;
		
		// ����, ����, ���� ���� ���� ��� �����
		presentList.removeAll(presentList);
		pastList.removeAll(pastList);
		upcomingList.removeAll(upcomingList);
		
		String count_sql="select count(movie_id) from movie";
		
		String result="select * from movie where movie_id=?";
		
		try {
			// ���� ���� Id ���ϱ�
			pstmt_count=con.prepareStatement(count_sql);
			rs_count=pstmt_count.executeQuery();
			rs_count.next();
			count=rs_count.getInt("count(movie_id)");
			
			// ���� ��ȭ�� ������ 0���� ũ��, ��ȭ�� �����ϸ�
			if(count>0){
				
				getUpcommingList();
				
				for(int i=0; i<upcomingId.size(); i++){
					pstmt_result=con.prepareStatement(result);
					pstmt_result.setString(1, upcomingId.get(i).toString());
					rs_result=pstmt_result.executeQuery();
					
					while(rs_result.next()){
						Movie dto=new Movie();
						System.out.println("movie ��ü ����");
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
						System.out.println("movie ��ü ����");
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
				
				// �� ������ ����Ʈ ���
				setMovieList(upcomingList, upcoming_movies, p_upcoming);

				// ���� ���� ����Ʈ ���
				setMovieList(presentList, present_movies, p_present);
				
				// ���� ���� ����Ʈ ���
				setMovieList(pastList, past_movies, p_past);
	
			}
			// ��ȭ�� �������� ������
			else{
				p_present.setVisible(false);
				//p_warning.setVisible(true);
				
				System.out.println("��ȭ ����");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	// �� ������ id �ҷ�����
	public void getUpcommingList(){
		upcomingId.removeAll(upcomingId);
		
		PreparedStatement pstmt_upcoming=null;	// ���� �� ��ȭ ��� id��
		ResultSet rs_upcoming=null;
		
		StringBuffer upcoming_sql=new StringBuffer();
		upcoming_sql.append("select movie_id from movie");
		upcoming_sql.append(" where start_date>to_char(sysdate, 'YYYY-MM-DD')");
		
		try {
			// ���� ���� id ����
			pstmt_upcoming=con.prepareStatement(upcoming_sql.toString());
			rs_upcoming=pstmt_upcoming.executeQuery();
			
			while(rs_upcoming.next()){			
				upcomingId.add(rs_upcoming.getInt("movie_id"));
			}
			System.out.println("upcomingId : "+upcomingId);
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
	
	// ���� ���� id �ҷ�����
	public void getPresentList(){
		presentId.removeAll(presentId);
		
		PreparedStatement pstmt_present=null;	// ���� �� ��ȭ ��� id��
		ResultSet rs_present=null;
		
		StringBuffer present_sql=new StringBuffer();
		present_sql.append("select movie_id from movie");
		present_sql.append(" where start_date<=to_char(sysdate, 'YYYY-MM-DD') and end_date>to_char(sysdate, 'YYYY-MM-DD')");
		
		try {
			// ���� ���� id ����
			pstmt_present=con.prepareStatement(present_sql.toString());
			rs_present=pstmt_present.executeQuery();
			
			while(rs_present.next()){			
				presentId.add(rs_present.getInt("movie_id"));
			}
			System.out.println("presentId : "+presentId);
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
	
	// ���� ���� id �ҷ�����
	public void getPastList(){
		pastId.removeAll(pastId);
		
		PreparedStatement pstmt_past=null;		// ���� �� ��ȭ ��� id��
		ResultSet rs_past=null;
		
		StringBuffer past_sql=new StringBuffer();
		past_sql.append("select movie_id from movie");
		past_sql.append(" where end_date<to_char(sysdate, 'YYYY-MM-DD')");
		
		try {
			// ���� ���� id ����
			pstmt_past=con.prepareStatement(past_sql.toString());
			rs_past=pstmt_past.executeQuery();
			
			while(rs_past.next()){
				pastId.add(rs_past.getInt("movie_id"));
			}
			System.out.println("pastId : "+pastId);
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
	
	// �󿵽ð�ǥ ��� ���� �� �ٿ�(�ϴ��� ���常 ������ ����, ���� ����Ǹ� ������ �ø� ����)
	public void saveExcelForm(){
		System.out.println("�󿵽ð�ǥ ���");
		
		if(present_movies.size()==0){
			JOptionPane.showMessageDialog(this, "���� ������ ��ȭ�� �����ϴ�.");
			return;
		}
		HSSFRow row;
		HSSFCell cell = null;
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		
		// sheet�� ����
		// �� ��¥���� ���ο� sheet ����(���� ��¥�κ��� 7�� �ı��� ���� ����)
		HSSFSheet[] sheet=new HSSFSheet[7];
		
		Calendar cal=Calendar.getInstance();
		String date;
		cal.add(cal.DATE, 1);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=0; i<sheet.length; i++){
			
			date=format.format(cal.getTime());
			sheet[i]=workbook.createSheet(date);
			cal.add(cal.DATE, 1);
			System.out.println(cal.getTime());
			getInfo(date);
			
			// ���̺� ���¸� �𸣴� �����ڿ��� ������ ���� �˷��ֱ� ���� ��� �߰�
			// �⺻ row�� 100�ٷ� ����(CGV ���̾�Ʈ���� ���� 70�� �Ѿ�̤�)
			for(int j=0; j<100; j++){
				// ��� row ����
				row=sheet[i].createRow(j);
				for(int k=0; k<14; k++){
					if(j==0){
						row.createCell(0).setCellValue("CGV "+branch_name);
					}
					else if(j==1){
						row.createCell(0).setCellValue("���� �� ��¥");
						row.createCell(1).setCellValue(date);
					}
					else if(j==2){
						row.createCell(0).setCellValue("������ ��ȭ ���");
						row.createCell(6).setCellValue("�󿵰� ���");
						row.createCell(10).setCellValue("�� �ð�ǥ");
					}
					else if(j==3){
						row.createCell(0).setCellValue("��ȣ");
						row.createCell(1).setCellValue("����");
						row.createCell(2).setCellValue("���� ����");
						row.createCell(3).setCellValue("���� ����");
						row.createCell(4).setCellValue("�� �ð�");
						
						row.createCell(6).setCellValue("��ȣ");
						row.createCell(7).setCellValue("�̸�");
						row.createCell(8).setCellValue("�¼���");
						
						row.createCell(10).setCellValue("��");
						row.createCell(11).setCellValue("��¥");
						row.createCell(12).setCellValue("�ð�");
						row.createCell(13).setCellValue("��ȭ");
					}
					
					// ������ ��ȭ ���
					for(int movie_cnt=0; movie_cnt<excelMovie.size(); movie_cnt++){
						if(j==4+movie_cnt){
							row.createCell(0).setCellValue(excelMovie.get(movie_cnt).getMovie_id());
							row.createCell(1).setCellValue(excelMovie.get(movie_cnt).getName());
							row.createCell(2).setCellValue(excelMovie.get(movie_cnt).getStart_date());
							row.createCell(3).setCellValue(excelMovie.get(movie_cnt).getEnd_date());
							row.createCell(4).setCellValue(excelMovie.get(movie_cnt).getRun_time());
						}
					}
					
					// �󿵰� ���
					for(int theater_cnt=0; theater_cnt<movieTheaterTab.theaterMain.theaterList.size(); theater_cnt++){
						if(j==4+theater_cnt){
							row.createCell(6).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getTheater_id());
							row.createCell(7).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getName()+"��");
							row.createCell(8).setCellValue(movieTheaterTab.theaterMain.theaterList.get(theater_cnt).getCount());
						}
					}
				}
			}
			
			// cell ��� ����
			/*
			CellStyle center=workbook.createCellStyle();
			center.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(center);
			*/
			
			FileOutputStream fos;
			try {
				fos=new FileOutputStream("res_manager/�󿵽ð�ǥ.xls");
				//fos=new FileOutputStream("http://192.168.0.8:8989/�󿵽ð�ǥ.xls");
				workbook.write(fos);
				fos.close();
				
				//JOptionPane.showMessageDialog(this, "�󿵽ð�ǥ ��� �ٿ� �Ϸ�");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				
			}
		}
		Main main=Main.getMain();
		main.upload("res_manager/�󿵽ð�ǥ.xls", "excel");

	}

	// Excel���Ϸκ��� �󿵽ð�ǥ ���
	public void setSchedule() throws ParseException{
		File excel;
		FileInputStream fis=null;
		JFileChooser chooser=new JFileChooser("res_manager/");
		//String[] columnName=new String[4];
		
		// cell �������� ������ ������ �� ����
		DataFormatter df=new DataFormatter();
		
		// ���ο� ������ ���� �� ���� ����
		excelData.removeAll(excelData);
		
		String value=null;
		
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			try {
				excel=chooser.getSelectedFile();
				fis=new FileInputStream(excel);
				System.out.println(FileUtil.getOnlyFileName(excel.toString()));
				boolean xls=FileUtil.getOnlyFileName(excel.toString()).equalsIgnoreCase(".xls");
				boolean xlsx=FileUtil.getOnlyFileName(excel.toString()).equalsIgnoreCase(".xlsx");
				
				// ������ Ȯ���ڰ� xls�� ���
				if(xls){
					System.out.println("xls Ȯ����");
					HSSFWorkbook workbook=new HSSFWorkbook(fis);
					
					// ��Ʈ ����ŭ
					for(int i=0; i<workbook.getNumberOfSheets(); i++){
						excelSheet=new ArrayList<ArrayList>();
						// �� ��Ʈ ���ϱ�
						HSSFSheet sheet=workbook.getSheetAt(i);
						
						// �� �� ���ϱ�
						int rows=sheet.getLastRowNum();
						
						if(rows>0){
							// �󿵽ð�ǥ ���� 4����� ����
							for(int j=4; j<=rows; j++){
								excelRow=new ArrayList<String>();
								HSSFRow row=sheet.getRow(j);
								// �� �࿡ ���� �ִ� ��쿡��
								if(row!=null){
									// cell �� ���ϱ�
									int cells=row.getPhysicalNumberOfCells();
									// �󿵽ð�ǥ ���� 11������ ����
									// �� �̸�
									HSSFCell cell=row.getCell(10);
									if(cell!=null){
										excelRow.add(cell.getStringCellValue());
									}
									// �� ��¥
									cell=row.getCell(11);
									if(cell!=null){
										if(cell.getCellType()==Cell.CELL_TYPE_STRING){
											excelRow.add(cell.getStringCellValue());
										}
										else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
											if(DateUtil.isCellDateFormatted(cell)) {
												Date date=cell.getDateCellValue();
												value=new SimpleDateFormat("yyyy-MM-dd").format(date);
												excelRow.add(value);
											}
										}
									}
									// ���� �ð�
									cell=row.getCell(12);
									if(cell!=null){
										DateFormat date=new SimpleDateFormat("HH:mm");
										Date time=cell.getDateCellValue();
										String st=date.format(time);
										excelRow.add(st);
									}
									// ��ȭ ����
									cell=row.getCell(13);
									if(cell!=null){
										excelRow.add(cell.getStringCellValue());
									}
									System.out.println("�� �� : "+excelRow);
									excelSheet.add(excelRow);
									//System.out.println(i+"��° sheet : "+excelSheet);
								}// �� ��(10-14��)
								//System.out.println("�� sheet : "+excelSheet);
							}//�� sheet(4��-������)
							System.out.println(i+"��° sheet : "+excelSheet);
						}
						excelData.add(excelSheet);
						//excelData.add(excelSheet);
					}//sheet ����
					System.out.println("excel������ sheet ���� : "+excelData.size());			
				}
				// ������ Ȯ���ڰ� xlsx�� ���
				else if(xlsx){
					System.out.println("xlsx Ȯ����");
					XSSFWorkbook workbook=new XSSFWorkbook(fis);
					
					for(int i=0; i<workbook.getNumberOfSheets(); i++){
						// �� ��Ʈ ���ϱ�
						XSSFSheet sheet=workbook.getSheetAt(i);
						
						// �� �� ���ϱ�
						int rows=sheet.getPhysicalNumberOfRows();
						if(rows>0){
							// �󿵽ð�ǥ ���� 4����� ����
							for(int j=3; j<rows; j++){
								XSSFRow row=sheet.getRow(j);
								// �� �࿡ ���� �ִ� ��쿡��
								if(row!=null){
									// cell �� ���ϱ�
									int cells=row.getPhysicalNumberOfCells();
									// �󿵽ð�ǥ ���� 11������ ����
									for(int k=10; k<cells; k++){
										/*
										// 10���� column : ��/��¥/�ð�/��ȭ
										if(j==10){
											HSSFCell cell=row.getCell(j);
											cell.getStringCellValue();
										}
										*/
										//XSSFCell cell=row.getCell(j);
										//excelCell.add(cell.getStringCellValue());
										//System.out.println(cell.getStringCellValue());
									}
								}
							}
						}
						//excelData.add(excelCell);
					}
				}
				JOptionPane.showMessageDialog(this, "�󿵽ð�ǥ ��� �Ϸ�");
				setProduct();		// product ���̺� �󿵽ð�ǥ��� ����
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// �󿵽ð�ǥ Excel ���Ϸκ��� ���� �����͸� DB�� ����
	public void setProduct(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sql=new StringBuffer();
		
		int theater_id = 0, movie_id =0;
		String screening_date=null, start_time=null;
	
		// sheet ����ŭ insert
		for(int i=0; i<excelData.size(); i++){
			// �� sheet�� row ����ŭ insert
			for(int j=0; j<excelData.get(i).size(); j++){
				// ��� : [8��, 2017-04-30, 08:10, ��Ʈ�δ���]
				//System.out.println("excelRow : "+excelData.get(i).get(j));
				String row=excelData.get(i).get(j).toString().substring(1, excelData.get(i).get(j).toString().length()-1);
				//System.out.println("�� �� ���� : "+row);
				String[] value=row.split(", ");
				
				// theater_id ���ϱ�
				sql.delete(0, sql.length());
				sql.append("select theater_id from theater where name=?");
				try {
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setString(1, value[0].substring(0, value[0].length()-1));
					rs=pstmt.executeQuery();
					rs.next();
					theater_id=rs.getInt("theater_id");
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
				
				// movie_id ���ϱ�
				sql.delete(0, sql.length());
				sql.append("select movie_id from movie where name=?");
				try {
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setString(1, value[3]);
					rs=pstmt.executeQuery();
					rs.next();
					movie_id=rs.getInt("movie_id");
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
				
				//System.out.println("�� �� theater_id : "+theater_id+", movie_id : "+movie_id);
				System.out.println(value[1]+","+value[2]);
				// �ߺ� üũ
				sql.delete(0, sql.length());
				sql.append("select * from product where movie_id=? and theater_id=? and screening_date=? and start_time=?");
				try {
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setString(1, Integer.toString(movie_id));
					pstmt.setString(2, Integer.toString(theater_id));
					pstmt.setString(3, value[1]);
					pstmt.setString(4, value[2]);
					rs=pstmt.executeQuery();
					if(!rs.next()){
						System.out.println("�ش� ���ڵ尡 �����ϴ�.");
						// insert
						sql.delete(0, sql.length());
						sql.append("insert into product(product_id, movie_id, theater_id, screening_date, start_time)");
						sql.append(" values(seq_product.nextval, ?, ?, ?, ?)");
						
						try {
							pstmt=con.prepareStatement(sql.toString());
							pstmt.setString(1, Integer.toString(movie_id));
							pstmt.setString(2, Integer.toString(theater_id));
							pstmt.setString(3, value[1]);
							pstmt.setString(4, value[2]);
							int result=pstmt.executeUpdate();
							
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
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	}
	// ��ȭ �г� ����(movieItem)
	public void setMovieList(ArrayList<Movie> movieList, ArrayList<MovieItem> movies, JPanel panel){
		movies.removeAll(movies);
		panel.removeAll();
		
		System.out.println("movieList ũ�� : "+movieList.size());
		
		// ���� �����ϴ� ��ȭ��ŭ �����ϰ� ���� �� ���
		for(int i=0; i<movieList.size(); i++){				
			Image img;
			try {
				//img = ImageIO.read(new File(path+movieList.get(i).getPoster()));
				// Image ��ο��� �޾ƿ���
				//url_image = new URL("http://211.238.142.112:8989/data/"+movieList.get(i).getPoster());
				//url_image = new URL("http://192.168.0.8:8989/"+movieList.get(i).getPoster());
				System.out.println("��ȭ �г� ����"+Calendar.getInstance().getTime());
				URL url_image = new URL("http://192.168.0.8:8989/"+movieList.get(i).getPoster());
				img=ImageIO.read(url_image.openStream());
				String name=movieList.get(i).getName();
				String start_date=movieList.get(i).getStart_date();
				String end_date=movieList.get(i).getEnd_date();
				
				Date startToDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(start_date);
				Date endToDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(end_date);
				Calendar c=Calendar.getInstance();
				Date today=new Date();		// ���� ��¥
				
				// the value 0 if the argument Date is equal to this Date; 
				// a value less than 0 if this Date is before the Date argument; 
				// and a value greater than 0 if this Date is after the Date argument.
				int end=endToDate.compareTo(today);
				int start=startToDate.compareTo(today);
				int upcoming=startToDate.compareTo(today);
				
				movieItem=new MovieItem(this, img, name, start_date, end_date);
				
				// ���� ����(���� ���ڰ� ���ú��� ���� ���)
				if(start<0 && end<0){
					System.out.println("����");
					movieItem.type="����";
					movieItem.index=i;
					// ������ �� ��ȭ�� index�� �Ѱ�����ϴµ��̤�
					//movieItem.index=
				}
				// ���� ����(���� ���ڰ� ���ú��� �۰ų� ���� ���� ���ڰ� ���ú��� ū ���)
				else if(start<=0 && end>0){
					System.out.println("����");
					movieItem.type="����";
					movieItem.index=i;
				}
				// �� ������(���� ���ڰ� ���ú��� ū ���)
				else if(start>0 && end>0){
					System.out.println("����");
					movieItem.type="����";
					movieItem.index=i;
				}
				
				panel.add(movieItem);
				
				movies.add(movieItem);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} 
		}
		System.out.println("��ȭ ���");
	}

	// ��ȭ �߰� ��ư
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_form){
			saveExcelForm();
			JOptionPane.showMessageDialog(this, "�󿵽ð�ǥ ��� �ٿ� �Ϸ�");
		}
		
		else if(obj==bt_load){
			try {
				setSchedule();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			System.out.println("�󿵽ð�ǥ ���");
		}
		
		else if(obj==bt_add){
			addMovie=new AddMovie(this);
			System.out.println("��ȭ �߰�");
		}
		else if(obj==list){	
			
			// ���� ����
			if(list.getSelectedIndex()==0){
				System.out.println(presentList.size());
				//p_present.setVisible(true);
				//p_past.setVisible(false);
				p_content.remove(p_past);
				p_content.add(p_present);
				p_content.remove(p_upcoming);
				p_content.updateUI();				
			}
			// �� ������
			else if(list.getSelectedIndex()==1){
				System.out.println(upcomingList.size());
				//p_present.setVisible(true);
				//p_past.setVisible(false);
				p_content.remove(p_past);
				p_content.remove(p_present);
				p_content.add(p_upcoming);
				p_content.updateUI();
			}
			// ���� ����
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
