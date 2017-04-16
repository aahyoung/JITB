package com.manage.movie;

import java.awt.BorderLayout;
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
	JPanel p_list, p_warning;
	JButton bt_back, bt_add;
	
	JLabel lb_title;
	
	JScrollPane scroll;
	
	// ���� ��ȭ �����Ͱ� �����ϴ��� ���� Ȯ��
	boolean exist=false;
	
	// DB ����
	DBManager manager;
	Connection con;
	
	// DB�κ��� ���� �����ϴ� ��ȭ ������ ��Ƴ��� collection framework
	ArrayList<Movie> movieList=new ArrayList<Movie>();
	
	// ��ȭ �г��� ��Ƴ��� collection framework
	ArrayList<MovieItem> movies=new ArrayList<MovieItem>();
	
	// �� ��ȭ �г�
	MovieItem movieItem;
	
	// ��ȭ �߰� Dialog
	AddMovie addMovie;

	// default �̹��� ���� ���
	String path="res_manager/";
	
	public MovieMain() {
		// DB ����
		connect();
		
		p_north=new JPanel();
		p_content=new JPanel();
		
		p_list=new JPanel();
		p_warning=new JPanel();
		
		lb_title=new JLabel("��ȭ ���");
		
		bt_add=new JButton("��ȭ �߰�");
		//bt_back=new JButton("�ǵ��ư���");
		
		// ��ȭ ��� �гο� scroll ���̱�
		//scroll=new JScrollPane(p_list);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(bt_add, BorderLayout.EAST);
		
		p_content.setLayout(new BorderLayout());
		p_content.add(p_list);
		
		bt_add.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
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
		PreparedStatement pstmt_list=null;
		PreparedStatement pstmt_count=null;
		ResultSet rs_count=null;
		ResultSet rs_list=null;
		
		int count=0;
		movieList.removeAll(movieList);
		
		String count_sql="select count(movie_id) from movie";
		String list_sql="select * from movie order by movie_id asc";
		
		try {
			pstmt_count=con.prepareStatement(count_sql);
			rs_count=pstmt_count.executeQuery();
			rs_count.next();
			count=rs_count.getInt("count(movie_id)");
			
			// ���� ��ȭ�� ������ 0���� ũ��, ��ȭ�� �����ϸ�
			if(count>0){
				
				pstmt_list=con.prepareStatement(list_sql);
				rs_list=pstmt_list.executeQuery();
				
				while(rs_list.next()){			
					Movie dto=new Movie();
					dto.setMovie_id(rs_list.getInt("movie_id"));
					dto.setPoster(rs_list.getString("poster"));
					dto.setName(rs_list.getString("name"));
					dto.setDirector(rs_list.getString("director"));
					dto.setMain_actor(rs_list.getString("main_actor"));
					dto.setStory(rs_list.getString("story"));
					dto.setRun_time(rs_list.getInt("run_time"));
					
					movieList.add(dto);
				}
				
				setMovieList();
				
			}
			// ��ȭ�� �������� ������
			else{
				p_list.setVisible(false);
				//p_warning.setVisible(true);
				
				System.out.println("��ȭ ����");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ��ȭ �г� ����
	public void setMovieList(){
		movies.removeAll(movies);
		p_list.removeAll();
		
		// ���� �����ϴ� ��ȭ��ŭ �����ϰ� ���� �� ���
		for(int i=0; i<movieList.size(); i++){				
			Image img;
			try {
				img = ImageIO.read(new File(path+movieList.get(i).getPoster()));
				String name=movieList.get(i).getName();
				
				movieItem=new MovieItem(img, name);
				p_list.add(movieItem);
				
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
		
	}
	
	// ��ȭ ��� ���
}
