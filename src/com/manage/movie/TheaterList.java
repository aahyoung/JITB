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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	JPanel p_north, p_content, p_theater, p_none;
	JLabel lb_title, lb_none;
	JButton bt_add;
	
	AddTheater addTheater;
	
	JDesktopPane desktop1=new JDesktopPane();
	JDesktopPane desktop2=new JDesktopPane();
	
	DBManager manager;
	Connection con;
	
	// innerFrame�� �����ִ� ���¸� �߰� ��ư ��Ȱ��ȭ
	boolean add=false;
	
	// ���� �����ϴ� ��ȭ�� ������ ��Ƴ��� collection framework
	ArrayList<TheaterData> theaterList=new ArrayList<TheaterData>();
	
	// ��ȭ�� �г��� ��Ƴ��� collection framework
	ArrayList<Theater> theaters=new ArrayList<Theater>();
	
	// �� ��ȭ�� �г�
	Theater theater;
	
	// ���õ� ��ȭ���� id
	int id;
	
	EditTheater selectMovie;
	
	public TheaterList() {
		// DB ����
		connect();
						
		// ��ȭ�� ��������
		getTheaterList();
		
		p_north=new JPanel();
		p_content=new JPanel();
		p_none=new JPanel();	// ��ȭ���� �������� ���� �� ������ �г�
		p_theater=new JPanel();	// ��ȭ���� ������ �� ������ �г�
		lb_title=new JLabel("��ȭ�� ���");
		lb_none=new JLabel("��ϵ� ��ȭ���� �����ϴ�. �߰� ��ư�� ���� ��ȭ���� �����ϼ���.");
		bt_add=new JButton("�߰�");
		
		showTheaterList();
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_none);
		p_content.add(p_theater);
		
		bt_add.addActionListener(this);
		
		if(add){
			bt_add.setEnabled(false);
		}
		else{
			bt_add.setEnabled(true);
		}
		
		setLayout(new BorderLayout());
		add(p_north,BorderLayout.NORTH);
		add(p_content);
		
		setBackground(Color.red);

	}
	
	// DB ����
		public void connect(){
			manager=DBManager.getInstance();
			con=manager.getConnect();
		}
		
	// ���� �����ϴ� ��ȭ���� DB���� ��������
	public void getTheaterList(){
		theaterList.removeAll(theaterList);
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from theater order by theater_id";
		
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
				
				theaterList.add(dto);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// �߰� ��ư�� ������ �����Ǵ� InnerFrame
	public void makeAddFrame(){
		//JDesktopPane desktop=new JDesktopPane();
		
		Dimension outerSize=this.getSize();
		
		addTheater=new AddTheater(this,"��ȭ�� �߰�", true, false, true);
		//addTheater.setBounds(outerSize.width/2, outerSize.height/2, 300, 200);
		
		desktop1.add(addTheater);
		
		this.p_content.add(desktop1);
		
		p_theater.setVisible(false);
		
	}
	
	// ��ȭ�� �г��� �����ϸ� ��ȭ�� ������ �� �ִ� innerFrame ����
	public void makeSelectFrame(){
		Dimension outerSize=this.getSize();
		selectMovie=new EditTheater(this, "��ȭ ����", true, false, true, id);
		
		desktop1.add(selectMovie);
		
		this.p_content.add(desktop1);
		
		p_theater.setVisible(false);

	}
	
	// ��ȭ�� �г� ���� -> �� Theater�� theater_id�� �����ϰ� ����
	public void setTheaterList(){
		theaters.removeAll(theaters);
		p_theater.removeAll();
		
		for(int i=0; i<theaterList.size(); i++){
			theater=new Theater();
			String name=theaterList.get(i).getName();
			theater.lb_name.setText(name+"��");
			int row=theaterList.get(i).getRow_line();
			int col=theaterList.get(i).getColumn_line();
			theater.lb_number.setText(row+"�� "+col+" ��");
			theater.theater_id=theaterList.get(i).getTheater_id();
			p_theater.add(theater);
			
			theaters.add(theater);
			
		}
		
		for(int j=0; j<theaters.size(); j++){
			theaters.get(j).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object obj=e.getSource();
					
					for(int k=0; k<theaters.size(); k++){
						if(obj==theaters.get(k)){
							System.out.println(theaters.get(k).theater_id+"�� ����");
							id=theaters.get(k).theater_id;
							makeSelectFrame();
						}
					}
				}
			});
		}
	}
	
	// ��ȭ�� ��� ���
	public void showTheaterList(){
		if(theaterList.size()==0){
			p_none.add(lb_none);
			p_none.setVisible(true);
			p_theater.setVisible(false);
		}
		else{
			setTheaterList();
			p_none.setVisible(false);
			p_theater.setVisible(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)obj;
		
		// innerFrame�� ������ ���� ������
		if(!add){
			// ��ȭ�� �߰� ��ư
			if(bt==bt_add){
				makeAddFrame();
				System.out.println("�߰� ����");
				add=true;
			}
		}
		else{
			addTheater.setDefault();
			addTheater.setVisible(true);
		}
	}
}
