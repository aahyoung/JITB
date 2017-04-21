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

	// DB������ �ʿ�
	DBManager manager;
	Connection con;
	
	// ���� ���õ� panel�� index
	int index;	
	
	// �θ� �г�
	TheaterMain theaterMain;
	
	public EditTheater(TheaterMain theaterMain, int index) {
		this.theaterMain=theaterMain;
		this.index=index;
		
		// DB ����
		connect();
		
		p_outer=new JPanel();
		p_info=new JPanel();
		//p_seat=new JPanel();
		p_button=new JPanel();
		
		lb_name=new JLabel(theaterMain.theaters.get(index).name);
		lb_count=new JLabel("�� �¼��� : "+theaterMain.theaters.get(index).count);
		
		//p_seat.setSize(new Dimension(120, 120));
		
		// �¼� ���
		//setSeat();
		
		bt_confirm=new JButton("Ȯ��");
		bt_cancel=new JButton("���");
		bt_delete=new JButton("����");
		
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
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	/*
	// �¼� ���̾ƿ� ���
	public void setSeat(){
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				Seat seat=new Seat();
				p_seat.add(seat);	
			}
		}
	}
	*/
	
	// Excel�� �¼� ���̾ƿ� ���
	public void setExcelSeat(){
		
	}
	
	// �ϳ��� frame�� ������ ����ϹǷ� ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		
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
		
		// ���� ��ư�� ������
		else if(bt==bt_delete){
			JOptionPane.showMessageDialog(this, theaterMain.theaters.get(index).name+"�� �����Ͻðڽ��ϱ�?");
		}
	}
}
