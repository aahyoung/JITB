package com.manage.movie;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

public class EditTheater extends JDialog implements ActionListener{
	JPanel p_outer;
	JPanel p_info;
	JPanel p_seat;
	JPanel p_button;
	
	JLabel lb_name, lb_count;
	
	JTextField t_name, t_count;

	JButton bt_cancel, bt_confirm, bt_delete;

	// DB연동에 필요
	DBManager manager;
	Connection con;
	
	// 현재 선택된 panel의 theater_id
	int index;	
	
	// 부모 패널
	TheaterMain theaterMain;
	
	public EditTheater(TheaterMain theaterMain, int index) {
		this.theaterMain=theaterMain;
		this.index=index;
		
		// DB 연동
		connect();
		
		p_outer=new JPanel();
		p_info=new JPanel();
		//p_seat=new JPanel();
		p_button=new JPanel();
		
		//lb_name=new JLabel(theaterMain.theaters.get(index).name);
		//lb_count=new JLabel("총 좌석수 : "+theaterMain.theaters.get(index).count);
		lb_name=new JLabel("영화관 이름 : ");
		lb_count=new JLabel("총 좌석수 : ");
		
		t_name=new JTextField(5);
		t_count=new JTextField(5);
		//p_seat.setSize(new Dimension(120, 120));
		
		// 좌석 출력
		//setSeat();
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		bt_delete=new JButton("삭제");
		
		t_name.setText(theaterMain.theaters.get(index).name);
		t_count.setText(Integer.toString(theaterMain.theaters.get(index).count));
		
		p_info.add(lb_name);
		p_info.add(t_name);
		p_info.add(lb_count);
		p_info.add(t_count);
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		//p_button.add(bt_delete);
		
		p_outer.add(p_info);
		//p_outer.add(p_seat);
		p_outer.add(p_button);
		
		add(p_outer);
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		bt_delete.addActionListener(this);
		
		setSize(500, 300);
		setLocationRelativeTo(theaterMain);
		setVisible(true);
		
	}
	
	// DB 연동
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	/*
	// 좌석 레이아웃 출력
	public void setSeat(){
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				Seat seat=new Seat();
				p_seat.add(seat);	
			}
		}
	}
	*/
	
	// Excel의 좌석 레이아웃 등록
	public void setExcelSeat(){
		
	}
	
	// 하나의 frame을 가지고 사용하므로 기존 값으로 항상 초기화
	public void setDefault(){
		
	}
	
	// 영화관 정보 수정
	public void updateTheater(){
		PreparedStatement pstmt=null;
		String sql="update theater set name=?, count=? where theater_id=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, t_name.getText());
			pstmt.setString(2, t_count.getText());
			pstmt.setString(3, Integer.toString(theaterMain.theaters.get(index).theater_id));
			int result=pstmt.executeUpdate();
			
			if(result!=0){
				JOptionPane.showMessageDialog(this, theaterMain.theaters.get(index).name+"관 수정 완료");
			}
			else{
				JOptionPane.showMessageDialog(this, theaterMain.theaters.get(index).name+"관 수정 실패");
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
	
	// 입력값이 유효한지 검사
	public void checkDataFormat(){
		// 좌석수가 숫자가 아닌 경우
		if(!DataValidTest.isNumber(t_count.getText())){
			JOptionPane.showMessageDialog(this, "좌석수는 숫자로 입력해주세요.");
			return;
		}
		// 좌석수에 음수 값을 넣은 경우
		if(Integer.parseInt(t_count.getText())<0){
			JOptionPane.showMessageDialog(this, "좌석수는 양수로 입력해주세요.");
			return;
		}
		updateTheater();		// 영화관 정보 수정
	}

	// 확인 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)(obj);
		
		// 확인 버튼을 누르면 
		if(bt==bt_confirm){
			System.out.println("확인 누름");
			checkDataFormat();
			this.setVisible(false);
			theaterMain.getTheaterList();
			theaterMain.setTheaterList();
			theaterMain.p_list.updateUI();
			//theaterMain.p_list.setVisible(true);
		}
		
		// 취소 버튼을 누르면
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			//theaterMain.p_list.setVisible(true);
		}
		
		// 삭제 버튼을 누르면
		else if(bt==bt_delete){
			JOptionPane.showMessageDialog(this, theaterMain.theaters.get(index).name+"을 삭제하시겠습니까?");
		}
	}
}
