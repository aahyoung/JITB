package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jitb.db.DBManager;

/*
 * 1. 레이아웃 구성
 * p_north		영화관 추가 버튼 구현
 * p_content	- 영화관 목록이 존재하면 			p_list 추가된 영화관 목록 출력
 * 				- 영화관 목록이 존재하지 않으면 		p_warning 영화관 추가 요청 문구 출력
 * 2. 기능 구현
 * 1) 영화관 추가 버튼은 JDialog으로 구현
 * 2) p_list의 영화관 리스트는 영화관 이름/행열 정보 구현
 * 3) 각 영화관 패널을 누르면 JDialog으로 수정/삭제/영화 선택 구현
 * */

public class TheaterMain extends JPanel implements ActionListener{
	JPanel p_north, p_load, p_content;
	JPanel p_list, p_warning;
	JButton bt_add, bt_form, bt_seat;
	
	JLabel lb_title, lb_warning;
	
	JScrollPane scroll;
	
	// 영화관 추가 Dialog
	AddTheater addTheater;
	
	// 영화관 수정/삭제/영화 선택 Dialog
	EditTheater editTheater;
	
	// 각 영화관 패널
	TheaterItem theaterItem;
	
	// 현재 영화관 데이터가 존재하는지 여부 확인
	boolean exist=false;
	
	// 현재 DB에 존재하는 영화관 개수
	int count;
	
	// DB연동에 필요
	DBManager manager;
	Connection con;
	
	// DB로부터 현재 존재하는 영화관 정보를 담아놓을 collection framework
	ArrayList<Theater> theaterList=new ArrayList<Theater>();
	
	// 영화관 패널을 담아놓을 collection framework
	ArrayList<TheaterItem> theaters=new ArrayList<TheaterItem>();
	
	// 선택된 영화관의 id
	int id;
	
	public TheaterMain() {
		// DB 연동
		connect();
		
		// 현재 존재하는 영화관 갯수 계산 및 출력
		getTheaterList();
		
		p_north=new JPanel();
		p_load=new JPanel();
		p_content=new JPanel();
		
		p_list=new JPanel();
		p_warning=new JPanel();
		
		lb_title=new JLabel("영화관 목록");
		lb_warning=new JLabel("현재 등록된 영화관이 없습니다.\n 영화관을 등록해주세요.");
		
		bt_add=new JButton("영화관 추가");
		bt_form=new JButton("좌석표 양식 다운");
		bt_seat=new JButton("좌석표 일괄 등록");
		
		// 영화관 목록 패널에 scroll 붙이기
		//scroll=new JScrollPane(p_list);
		showTheaterList();
		
		p_load.add(bt_form);
		p_load.add(bt_seat);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(p_load);
		p_north.add(bt_add, BorderLayout.EAST);
		
		bt_add.addActionListener(this);
		bt_form.addActionListener(this);
		bt_seat.addActionListener(this);
		
		//p_warning.add(lb_warning);
		//p_warning.setBackground(Color.red);
		
		p_content.setBackground(Color.white);
		p_content.setLayout(new BorderLayout());
		p_content.add(p_list);
		//p_content.add(p_warning);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
		//addFrame();

	}
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 현재 존재하는 영화관을 DB에서 가져오기
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		theaterList.removeAll(theaterList);
		
		String sql="select * from theater order by theater_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
	
			while(rs.next()){			
				Theater dto=new Theater();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setName(rs.getString("name"));
				dto.setCount(rs.getInt("count"));
				
				theaterList.add(dto);
			}
			
			// 현재 존재하는 영화 개수 구하기
			sql="select count(*) from theater";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			count=rs.getInt("count(*)");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 영화관 패널 설정
	public void setTheaterList(){
		theaters.removeAll(theaters);
		p_list.removeAll();
		
		// 현재 존재하는 영화관만큼 생성하고 설정 및 출력
		for(int i=0; i<theaterList.size(); i++){
			String name=theaterList.get(i).getName();
			int count=theaterList.get(i).getCount();

			theaterItem=new TheaterItem(name, count);
			theaterItem.theater_id=theaterList.get(i).getTheater_id();
					
			p_list.add(theaterItem);
			
			theaters.add(theaterItem);
		}
		
		for(int j=0; j<theaters.size(); j++){
			theaters.get(j).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object obj=e.getSource();
					
					for(int k=0; k<theaters.size(); k++){
						if(obj==theaters.get(k)){
							System.out.println(theaters.get(k).theater_id+"관 선택");
							//id=theaters.get(k).theater_id;
							editTheater=new EditTheater(TheaterMain.this, k);
							System.out.println(k);
							return;
						}
					}
				}
			});
		}
		
		System.out.println("영화관 세팅");
	}
	
	// 영화관 목록 출력
	public void showTheaterList(){
		if(theaterList.size()==0){
			p_warning.setVisible(true);
			p_list.setVisible(false);
		}
		else{
			setTheaterList();
			p_warning.setVisible(false);
			p_list.setVisible(true);
			System.out.println("영화관 출력");
		}
	}
	
	// 영화관 좌석표 양식 저장 및 다운(일단은 저장만 가능한 상태, 서버 구축되면 서버에 올릴 예정)
	public void saveExcelForm(){
		System.out.println("좌석표 양식");
		if(theaterList.size()==0){
			JOptionPane.showMessageDialog(this, "아직 영화관이 추가되지 않았습니다.");
			return;
		}
		HSSFRow row;
		HSSFCell cell = null;
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		
		// sheet명 설정
		// 영화관마다 새로운 sheet 생성
		HSSFSheet[] sheet=new HSSFSheet[theaterList.size()];
		for(int i=0; i<sheet.length; i++){
			sheet[i]=workbook.createSheet(theaterList.get(i).getName()+"관");		
			
			// 10*10, 마지막 줄은 총 좌석수 표시
			for(int j=0; j<10; j++){
				// 출력 row 생성
				row=sheet[i].createRow(j);
				for(int k=0; k<10; k++){
					row.createCell(k).setCellValue("O");
				}
			}
			row=sheet[i].createRow(10);
			row.createCell(0).setCellValue("총 좌석수");
			row.createCell(1).setCellValue(theaterList.get(i).getCount());
			
			// cell 가운데 정렬
			/*
			CellStyle center=workbook.createCellStyle();
			center.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(center);
			*/
			
		}
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream("res_manager/영화관 좌석표.xls");
			workbook.write(fos);
			JOptionPane.showMessageDialog(this, "좌석표 양식 다운 완료");
			
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
		}
	}
	
	// 서버 구축하면 올릴 예정
	// 영화관 좌석표 등록
	public void loadSeatExcel(){
		
	}

	// 영화관 추가, 영화관 좌석표 등록 버튼
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		// 영화관 추가
		if(obj==bt_add){
			if(count>=10){
				JOptionPane.showMessageDialog(this, "영화관의 최대 개수는 10개입니다. 더 이상 추가할 수 없습니다.");
			}
			else{
				addTheater=new AddTheater(this);
				System.out.println("영화관 추가");
			}
		}
		
		// 영화관 좌석표 양식 다운
		else if(obj==bt_form){
			saveExcelForm();
		}
		// 영화관 좌석표 등록
		else if(obj==bt_seat){
			loadSeatExcel();
		}
	}
}
