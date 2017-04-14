package com.manage.movie;

import java.awt.Choice;
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
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jitb.db.DBManager;

// ��ȭ�� �߰� ���̾ƿ�
public class EditTheater extends JInternalFrame implements ActionListener, ItemListener{
	JPanel p_outer;
	JPanel p_title;
	JPanel p_select;
	JPanel p_button;
	
	JLabel lb_title;

	Choice ch_movie;

	JButton bt_cancel, bt_confirm;

	// DB������ �ʿ�
	DBManager manager;
	Connection con;
	
	// ���� ���õ� panel�� index
	int index;	
	
	ArrayList<MovieData> movie=new ArrayList<MovieData>();
	
	TheaterList theaterList;
	
	// ǥ���ϱ� ���� string ��ȯ
	String[] start_time=new String[7];
	
	public EditTheater(TheaterList theaterList, String title, boolean resizable, boolean closable, boolean maximizable, int index) {
		this.theaterList=theaterList;
		this.title=title;
		this.resizable=resizable;
		this.closable=closable;
		this.maximizable=maximizable;
		this.index=index;
		
		// DB ����
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("���� ��ȭ���� ���� ��ȭ�� �������ּ���.");
		ch_movie=new Choice();
		getMovieList();
		
		bt_confirm=new JButton("Ȯ��");
		bt_cancel=new JButton("���");
		
		// choice�� ItemListener ����
		ch_movie.addItemListener(this);
		
		p_title.add(lb_title);
		
		p_select.add(ch_movie);
		
		p_button.add(bt_confirm);
		p_button.add(bt_cancel);
		
		p_outer.add(p_title);
		p_outer.add(p_select);
		p_outer.add(p_button);
		
		add(p_outer);
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		setBounds(370, 220, 500, 200);
		setVisible(true);
		
	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// movie ���̺� �����ϴ� ��ȭ ����Ʈ ��������
	public void getMovieList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select * from movie";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			movie.removeAll(movie);
			ch_movie.removeAll();
			
			while(rs.next()){
				MovieData movieData=new MovieData();
				movieData.setMovie_id(rs.getInt("movie_id"));
				movieData.setPoster(rs.getString("poster"));
				movieData.setName(rs.getString("name"));
				movieData.setDirector(rs.getString("director"));
				movieData.setMain_actor(rs.getString("main_actor"));
				movieData.setStory(rs.getString("story"));
				movieData.setStart_date(rs.getString("start_date"));
				movieData.setEnd_date(rs.getString("end_date"));
				movieData.setRun_time(rs.getInt("run_time"));
				
				movie.add(movieData);
				
				ch_movie.add(movieData.getName());
			}
			
			System.out.println("��ȭ ��� �޾ƿ�");
			
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
	
	// ������ ��ȭ�� movie_id ���� ����
	// -> theater ���̺� ������ ����
	public void setMovieID(){
		
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("update theater set movie_id=? where theater_id=?");
		
		String id=Integer.toString(movie.get(ch_movie.getSelectedIndex()).getMovie_id());
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, Integer.toString(index));
			
			// ���������� insert�� ��ȯ�� ������ 1 ��ȯ
			int result=pstmt.executeUpdate();
			if(result!=0){
				setStartTime();
				System.out.println("movie_id ���� ����");
			}
			else{
				System.out.println("movie_id ���� ����");
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
	
	// ���� �󿵽ð��� ���ϴ� �޼ҵ�
	public void calculateTime(){
		int run_time;
		int hour;		// ���� �ð��� �� ������ ���
		int min;		// ���� ��
		int result;		// ���� ���� �ð� : �� ������ ���۽ð� + �󿵽ð� + 30

		// ǥ���ϱ� ���� string ��ȯ
		//String[] start_time=new String[7];
		start_time[0]="09:00";	// �׻� ��ȭ�� ù��° �� �ð��� ���� 9��
		
		// �� �ð��� 7���� ����
		for(int i=1; i<7; i++){
			for(int j=0; j<movie.size(); j++){
				// ���õ� ��ȭ�� id�� �̿��ؼ� start_time���̺� ���� �����ؾ� ��
				// ���� ��ȭ choice���� ���õ� index��°�� movie �󿵽ð� ��������
				//movie.get(ch_movie.getSelectedIndex()).getMovie_id()
				run_time=movie.get(ch_movie.getSelectedIndex()).getRun_time();
				/*
				 * ���� ���� �ð� - 09:32
				 * ��ȭ ���� Ÿ�� - 165��
				 * ���� ���� �ð� - 09:32 + 135�� => 
				 * -> ���۽ð�*60 + ���۽ð� �� => 9*60+32=572
				 * -> �д����� ��ȯ�� ���۽ð� + �󿵽ð� + 30�� ���½ð� = ���� ���۽ð� => 767��
				 * -> �д����� ���� ���۽ð�/60 => 12(��)
				 * -> �д����� ���� ���۽ð�%60 => 47(��)
				 * => ���� ���� �ð��� 12�� 47��
				 * */
				String[] divide=start_time[i-1].split(":");
				hour=Integer.parseInt(divide[0])*60;
				min=Integer.parseInt(divide[1]);
				result=hour+min+run_time+30;
				
				start_time[i]=result/60+":"+result%60;
				System.out.println(start_time);
			}
		}
	}
	
	// ���� ��ȭ�� �����Ǹ� start_time ���̺� ���� ����
	public void setStartTime(){
		calculateTime();
		
		PreparedStatement pstmt=null;
		
		// ��ȭ ���� �ð��� ���̸�ŭ(7��)
		for(int i=0; i<start_time.length; i++){
			StringBuffer sql=new StringBuffer();
			sql.append("insert into start_time(start_time_id, start_time, movie_id)");
			sql.append(" values(seq_start_time.nextval, ?, ?)");
			
			String id=Integer.toString(movie.get(ch_movie.getSelectedIndex()).getMovie_id());
			
			try {
				pstmt=con.prepareStatement(sql.toString());
				pstmt.setString(1, start_time[i]);
				pstmt.setString(2, id);
				
				int result=pstmt.executeUpdate();
				if(result!=0){
					System.out.println("start_time ���� ����");
				}
				else{
					System.out.println("start_time ���� ����");
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
	}
	
	// �ϳ��� frame�� ������ ����ϹǷ� ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		ch_movie.select(0);
	}
	
	// ��ȭ ����
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		Choice ch=(Choice)obj;

	}

	// Ȯ�� ��ư�� ������
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)(obj);
		
		// Ȯ�� ��ư�� ������ 
		if(bt==bt_confirm){
			System.out.println("Ȯ�� ����");
			setMovieID();
			this.setVisible(false);
			theaterList.p_theater.setVisible(true);
			
		}

		
		// ��� ��ư�� ������
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			theaterList.p_theater.setVisible(true);
		}
	}

}

