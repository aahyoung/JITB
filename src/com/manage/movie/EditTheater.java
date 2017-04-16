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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.DBManager;

public class EditTheater extends JDialog implements ActionListener, ItemListener{
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
	
	ArrayList<MovieItem> movie=new ArrayList<MovieItem>();
	
	// �θ� �г�
	TheaterMain theaterMain;
	
	// ǥ���ϱ� ���� string ��ȯ
	String[] start_time=new String[7];
	
	public EditTheater(TheaterMain theaterMain) {
		this.theaterMain=theaterMain;
		
		// DB ����
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("���� ��ȭ���� ���� ��ȭ�� �������ּ���.");
		ch_movie=new Choice();
		
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
	
	// ���� ���۵��� choice�� ����
	public void setMovieChoice(){
		
	}
	
	// ���� ��ȭ�� �����Ǹ� theater_operate ���̺� ���� ����
	public void setTheaterOperate(){
		// ���� ���õ� ��ȭ���� id�� ���� ������ ��ȭ�� movie_id�� �̿��Ͽ� movie->screening_date->start_time
		// start_time_id�� ����
		
	}
	
	// ���� ��ȭ�� �����Ǹ� seat���̺� theater_operate_id�� �̿��Ͽ� ���� ����
	public void setSeat(){
		// ���� ���õ� ��ȭ���� �࿭ ������ �̿��� �¼� �̸� ����
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
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
			
		}

		
		// ��� ��ư�� ������
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			theaterMain.p_list.setVisible(true);
		}
	}
}
