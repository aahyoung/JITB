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

import com.jitb.db.DBManager;

public class EditTheater extends JDialog implements ActionListener{
	JPanel p_outer;
	JPanel p_info;
	JPanel p_seat;
	JPanel p_button;
	
	JLabel lb_name, lb_count;

	JButton bt_cancel, bt_confirm, bt_delete;

	// DB연동에 필요
	DBManager manager;
	Connection con;
	
	// 현재 선택된 panel의 index
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
		
		lb_name=new JLabel(theaterMain.theaters.get(index).name);
		lb_count=new JLabel("총 좌석수 : "+theaterMain.theaters.get(index).count);
		
		//p_seat.setSize(new Dimension(120, 120));
		
		// 좌석 출력
		//setSeat();
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		bt_delete=new JButton("삭제");
		
		p_info.add(lb_name);
		p_info.add(lb_count);
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		p_button.add(bt_delete);
		
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

	// 확인 버튼을 누르면
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)(obj);
		
		// 확인 버튼을 누르면 
		if(bt==bt_confirm){
			System.out.println("확인 누름");
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
			
		}
		
		// 취소 버튼을 누르면
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
		}
		
		// 삭제 버튼을 누르면
		else if(bt==bt_delete){
			JOptionPane.showMessageDialog(this, theaterMain.theaters.get(index).name+"을 삭제하시겠습니까?");
		}
	}
}
