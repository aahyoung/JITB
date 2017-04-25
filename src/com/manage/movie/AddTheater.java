package com.manage.movie;

import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

/*
 * 영화관 추가 Dialog
 * 규칙) 영화관 이름은 무조건 1-2자리 숫자로 입력
 * */

public class AddTheater extends JDialog implements ActionListener{
	JPanel p_outer;
	JPanel p_title;
	JPanel p_input;
	JPanel p_content;
	JPanel p_button;
	
	JLabel lb_title;
	JLabel lb_name;
	JLabel lb_content;
	JLabel lb_row, lb_col;
	
	JTextField t_name, t_count;
	
	JButton bt_cancel, bt_confirm;
	
	// 부모 패널
	TheaterMain theaterMain;
	
	DBManager manager;
	Connection con;
	
	public AddTheater(TheaterMain theaterMain) {
		this.theaterMain=theaterMain;
		
		// DB 연결
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_input=new JPanel();
		p_content=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("영화관 크기 설정");
		lb_name=new JLabel("영화관 이름 입력");
		t_name=new JTextField(10);
		t_count=new JTextField(5);
		lb_content=new JLabel("총 좌석수를 입력해주세요.");
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		
		p_title.add(lb_title);
		p_input.add(lb_name);
		p_input.add(t_name);
		p_content.add(lb_content);
		p_content.add(t_count);		
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		
		p_outer.add(p_title);
		p_outer.add(p_input);
		p_outer.add(p_content);
		p_outer.add(p_button);
		
		add(p_outer);
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		//setBounds(370, 220, 300, 200);
		setSize(new Dimension(300, 200));
		setVisible(true);
		setLocationRelativeTo(theaterMain);
	
	}
	
	// DB 연결
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// 입력한 관 정보를 theater 테이블에 저장
	public void insertTheater(){
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into theater(theater_id, branch_id, name, count)");
		sql.append(" values(seq_theater.nextval, 1, ?, ?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());		// branch_id는 임의로 1 설정
			pstmt.setInt(2, Integer.parseInt(t_count.getText()));
			
			// 성공적으로 insert문 반환시 무조건 1 반환
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "영화관 추가 완료");
			}
			else{
				JOptionPane.showMessageDialog(this, "영화관 추가 실패");
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
		insertTheater();		// 영화관 등록
	}

	
	// 하나의 internalFrame을 사용하기 때문에 기존 값으로 항상 초기화
	public void setDefault(){
		t_name.setText("");
		t_count.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		// 영화관 추가 확인
		if(obj==bt_confirm){
			checkDataFormat();
			setVisible(false);
			theaterMain.getTheaterList();
			theaterMain.setTheaterList();
			theaterMain.p_list.updateUI();
			theaterMain.p_list.setVisible(true);
			System.out.println("영화관 추가 확인");
		}
		
		// 영화관 추가 취소
		else if(obj==bt_cancel){
			setVisible(false);
			theaterMain.p_list.setVisible(true);
			System.out.println("영화관 추가 취소");
		}
		
	}
}
