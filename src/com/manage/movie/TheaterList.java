/*
 * ��ȭ�� ��� ��� �� �߰� Ŭ����
 * ���� ��Ȳ)
 * getTheaterList()�� �̿��Ͽ� theater ���̺� �������� �޼ҵ� ������
 * */
package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.DBManager;

// �� ��� ���̾ƿ�
public class TheaterList extends JPanel implements ActionListener{
	JPanel p_north, p_content;
	JLabel lb_title;
	JButton bt_add;
	
	AddTheater addTheater;
	
	JDesktopPane desktop=new JDesktopPane();
	
	DBManager manager;
	Connection con;
	
	// innerFrame�� �����ִ� ���¸� �߰� ��ư ��Ȱ��ȭ
	boolean inner=false;
	
	// ���� �����ϴ� ��ȭ���� ��Ƴ��� collection framework
	ArrayList<JPanel> theaterList=new ArrayList<JPanel>();
	
	public TheaterList() {
		p_north=new JPanel();
		p_content=new JPanel();
		lb_title=new JLabel("��ȭ�� ���");
		bt_add=new JButton("�߰�");
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		//p_content.setBackground(Color.CYAN);
		p_content.setLayout(new BorderLayout());
		
		bt_add.addActionListener(this);
		
		if(inner){
			bt_add.setEnabled(false);
		}
		else{
			bt_add.setEnabled(true);
		}
		
		setLayout(new BorderLayout());
		add(p_north,BorderLayout.NORTH);
		add(p_content);
		
		setBackground(Color.red);
		
		// DB ����
		connect();
				
		// ��ȭ�� ��������
		//getTheaterList();
	}
	
	// DB ����
		public void connect(){
			manager=DBManager.getInstance();
			con=manager.getConnect();
		}
		
	// ���� �����ϴ� ��ȭ���� DB���� ��������
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from theater";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				TheaterData dto=new TheaterData();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setName(rs.getString("name"));
				dto.setRow_line(rs.getInt("row_line"));
				dto.setColumn_line(rs.getInt("column_line"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setMovie_id(rs.getString("movie_id"));
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void makeInnerFrame(){
		//JDesktopPane desktop=new JDesktopPane();
		
		Dimension outerSize=this.getSize();
		
		addTheater=new AddTheater("��ȭ�� �߰�", true, false, true);
		//addTheater.setBounds(outerSize.width/2, outerSize.height/2, 300, 200);
		
		desktop.add(addTheater);
		
		this.p_content.add(desktop);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)obj;
		
		// innerFrame�� ������ ���� ������
		if(!inner){
			// ��ȭ�� �߰� ��ư
			if(bt==bt_add){
				makeInnerFrame();
				System.out.println("�߰� ����");
				inner=true;
			}
		}
		else{
			addTheater.setDefault();
			addTheater.setVisible(true);
		}
	}
}
