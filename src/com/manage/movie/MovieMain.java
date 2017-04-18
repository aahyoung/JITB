package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jitb.db.DBManager;

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
	JPanel p_list;
	JPanel p_present, p_past;
	JButton bt_back, bt_add;
	
	JComboBox<String> list;
	
	JLabel lb_title;
	
	JScrollPane scroll;
	
	// ���� ��ȭ �����Ͱ� �����ϴ��� ���� Ȯ��
	boolean exist=false;
	
	// DB ����
	DBManager manager;
	Connection con;
	
	// end_date�� ���� ��¥�� ���Ͽ� ���� ���� ���� ��ȭ id
	ArrayList presentId=new ArrayList();

	// end_date�� ���� ��¥�� ���Ͽ� ���� ���� ���� ��ȭ id
	ArrayList pastId=new ArrayList();
	
	// DB�κ��� ���� ���� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> presentList=new ArrayList<Movie>();

	// DB�κ��� ���� ���� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> pastList=new ArrayList<Movie>();
	
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
	
	PresentMovie present;
	
	PastMovie past;
	
	public MovieMain() {
		// DB ����
		connect();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_present=new JPanel();
		p_past=new JPanel();
		//present=new PresentMovie();
		//past=new PastMovie();
		
		p_list=new JPanel();
		list=new JComboBox<String>();
		list.addItem("���� ����");
		list.addItem("���� ����");
		
		lb_title=new JLabel("��ȭ ���");
		
		bt_add=new JButton("��ȭ �߰�");
		
		//bt_back=new JButton("�ǵ��ư���");
		
		// ��ȭ ��� �гο� scroll ���̱�
		//scroll=new JScrollPane(p_list);
		
		p_list.add(list);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(p_list);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_present.setBackground(Color.green);
		p_past.setBackground(Color.cyan);
		
		p_content.setLayout(new BorderLayout());
		//p_content.add(present);
		//p_content.add(past);
		p_content.add(p_present);
		//p_content.add(p_past);
		
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
		
	// ���� �����ϴ� ��ȭ�� DB���� ��������
	public void getMovieList(){
		PreparedStatement pstmt_count=null;		// ���� DB�� �����ϴ� ��ȭ ���� ���
		PreparedStatement pstmt_result=null;	// select * from movie where movie_id=? ���, Movie DTO
		ResultSet rs_count=null;
		ResultSet rs_result=null;
		
		int count=0;
		
		// ����, ���� ���� ���� ��� �����
		presentList.removeAll(presentList);
		pastList.removeAll(pastList);
		
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
						dto.setRun_time(rs_result.getInt("run_time"));
						
						pastList.add(dto);
					}
				}

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
	
	// ���� ���� id �ҷ�����
	public void getPresentList(){
		presentId.removeAll(presentId);
		
		PreparedStatement pstmt_present=null;	// ���� �� ��ȭ ��� id��
		ResultSet rs_present=null;
		
		StringBuffer present_sql=new StringBuffer();
		present_sql.append("select m.movie_id from movie m, screening_date s");
		present_sql.append(" where m.movie_id=s.movie_id group by m.movie_id");
		present_sql.append(" having(max(screening_date)>=to_char(sysdate, 'YYYY-MM-DD'))");
		
		try {
			// ���� ���� id ����
			pstmt_present=con.prepareStatement(present_sql.toString());
			rs_present=pstmt_present.executeQuery();
			
			while(rs_present.next()){			
				presentId.add(rs_present.getInt("movie_id"));
				System.out.println(presentId);
			}
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
		past_sql.append("select m.movie_id  from movie m, screening_date s");
		past_sql.append(" where m.movie_id=s.movie_id group by m.movie_id");
		past_sql.append(" having(max(screening_date)<to_char(sysdate, 'YYYY-MM-DD'))");
		
		try {
			// ���� ���� id ����
			pstmt_past=con.prepareStatement(past_sql.toString());
			rs_past=pstmt_past.executeQuery();
			
			while(rs_past.next()){
				pastId.add(rs_past.getInt("movie_id"));
			}
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

	
	// ��ȭ �г� ����
	public void setMovieList(ArrayList<Movie> movieList, ArrayList<MovieItem> movies, JPanel panel){
		movies.removeAll(movies);
		panel.removeAll();
		
		System.out.println("movieList ũ�� : "+movieList.size());
		// ���� �����ϴ� ��ȭ��ŭ �����ϰ� ���� �� ���
		for(int i=0; i<movieList.size(); i++){				
			Image img;
			try {
				img = ImageIO.read(new File(path+movieList.get(i).getPoster()));
				String name=movieList.get(i).getName();
				
				movieItem=new MovieItem(img, name);
				panel.add(movieItem);
				
				movies.add(movieItem);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("��ȭ ���");
	}

	// ��ȭ �߰� ��ư
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_add){
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
				p_content.updateUI();
			}
			// ���� ����
			else if(list.getSelectedIndex()==1){
				System.out.println(pastList.size());
				//p_present.setVisible(false);
				//p_past.setVisible(true);
				p_content.remove(p_present);
				p_content.add(p_past);
				p_content.updateUI();
			}
			
		}
		
	}
	
	// ��ȭ ��� ���
}
