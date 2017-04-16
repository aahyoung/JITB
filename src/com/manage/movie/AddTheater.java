package com.manage.movie;

import java.awt.Choice;
import java.awt.Dialog;
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
	JPanel p_select;
	JPanel p_button;
	
	JLabel lb_title;
	JLabel lb_name;
	JLabel lb_content;
	JLabel lb_row, lb_col;
	
	JTextField t_name;
	
	Choice ch_row, ch_col;
	
	JButton bt_cancel, bt_confirm;
	
	// 부모 패널
	TheaterMain theaterMain;
	
	DBManager manager;
	Connection con;
	
	public AddTheater(TheaterMain theaterMain) {
		this.theaterMain=theaterMain;
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_input=new JPanel();
		p_content=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("영화관 크기 설정");
		lb_name=new JLabel("영화관 이름 입력");
		t_name=new JTextField(10);
		lb_content=new JLabel("추가할 영화관의 행, 열을 선택해주세요.");
		
		lb_row=new JLabel("행");
		lb_col=new JLabel("열");
		
		ch_row=new Choice();
		ch_col=new Choice();
		
		bt_confirm=new JButton("확인");
		bt_cancel=new JButton("취소");
		
		for(int i=0; i<10; i++){
			ch_row.add(Integer.toString(i+1));
			ch_col.add(Integer.toString(i+1));
		}
		
		p_title.add(lb_title);
		p_input.add(lb_name);
		p_input.add(t_name);
		p_content.add(lb_content);
		p_select.add(lb_row);
		p_select.add(ch_row);
		p_select.add(lb_col);
		p_select.add(ch_col);
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		
		p_outer.add(p_title);
		p_outer.add(p_input);
		p_outer.add(p_content);
		p_outer.add(p_select);
		p_outer.add(p_button);
		
		add(p_outer);
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		setBounds(370, 220, 300, 200);
		setVisible(true);
		
		// DB 연결
		connect();
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
		sql.append("insert into theater(theater_id, branch_id, name, row_line, column_line)");
		sql.append(" values(seq_theater.nextval, 1, ?, ?, ?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());		// branch_id는 임의로 1 설정
			pstmt.setString(2, Integer.toString(ch_row.getSelectedIndex()+1));
			pstmt.setString(3, Integer.toString(ch_col.getSelectedIndex()+1));
			
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
	
	// 하나의 internalFrame을 사용하기 때문에 기존 값으로 항상 초기화
	public void setDefault(){
		t_name.setText("");
		ch_row.select(0);
		ch_col.select(0);
	}

	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		// 영화관 추가 확인
		if(obj==bt_confirm){
			insertTheater();
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
