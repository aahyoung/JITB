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
 * ��ȭ�� �߰� Dialog
 * ��Ģ) ��ȭ�� �̸��� ������ 1-2�ڸ� ���ڷ� �Է�
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
	
	// �θ� �г�
	TheaterMain theaterMain;
	
	DBManager manager;
	Connection con;
	
	public AddTheater(TheaterMain theaterMain) {
		this.theaterMain=theaterMain;
		
		// DB ����
		connect();
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_input=new JPanel();
		p_content=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("��ȭ�� ũ�� ����");
		lb_name=new JLabel("��ȭ�� �̸� �Է�");
		t_name=new JTextField(10);
		t_count=new JTextField(5);
		lb_content=new JLabel("�� �¼����� �Է����ּ���.");
		
		bt_confirm=new JButton("Ȯ��");
		bt_cancel=new JButton("���");
		
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
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// �Է��� �� ������ theater ���̺� ����
	public void insertTheater(){
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into theater(theater_id, branch_id, name, count)");
		sql.append(" values(seq_theater.nextval, 1, ?, ?)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());		// branch_id�� ���Ƿ� 1 ����
			pstmt.setInt(2, Integer.parseInt(t_count.getText()));
			
			// ���������� insert�� ��ȯ�� ������ 1 ��ȯ
			int result=pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "��ȭ�� �߰� �Ϸ�");
			}
			else{
				JOptionPane.showMessageDialog(this, "��ȭ�� �߰� ����");
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
	
	// �Է°��� ��ȿ���� �˻�
	public void checkDataFormat(){
		// �¼����� ���ڰ� �ƴ� ���
		if(!DataValidTest.isNumber(t_count.getText())){
			JOptionPane.showMessageDialog(this, "�¼����� ���ڷ� �Է����ּ���.");
			return;
		}
		// �¼����� ���� ���� ���� ���
		if(Integer.parseInt(t_count.getText())<0){
			JOptionPane.showMessageDialog(this, "�¼����� ����� �Է����ּ���.");
			return;
		}
		insertTheater();		// ��ȭ�� ���
	}

	
	// �ϳ��� internalFrame�� ����ϱ� ������ ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		t_name.setText("");
		t_count.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		// ��ȭ�� �߰� Ȯ��
		if(obj==bt_confirm){
			checkDataFormat();
			setVisible(false);
			theaterMain.getTheaterList();
			theaterMain.setTheaterList();
			theaterMain.p_list.updateUI();
			theaterMain.p_list.setVisible(true);
			System.out.println("��ȭ�� �߰� Ȯ��");
		}
		
		// ��ȭ�� �߰� ���
		else if(obj==bt_cancel){
			setVisible(false);
			theaterMain.p_list.setVisible(true);
			System.out.println("��ȭ�� �߰� ���");
		}
		
	}
}
