/*
 * ��ȭ�� �߰� Ŭ����
 * ��Ģ) ��ȭ�� �̸��� ������ 1-2�ڸ� ���ڷ� �Է��� ��!
 * ���� ��Ȳ)
 * ������ ���� �Ϸ�
 * - ��ȭ�� ����Ʈ�� �����ִ� �������� ���� �ʿ�
 * */
package com.manage.movie;

import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

// ��ȭ�� �߰� ���̾ƿ�
public class AddTheater extends JInternalFrame implements ActionListener, ItemListener{
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
	
	int row, col;
	
	// DB������ �ʿ�
	DBManager manager;
	Connection con;
	
	public AddTheater(String title, boolean resizable, boolean closable, boolean maximizable) {
		this.title=title;
		this.resizable=resizable;
		this.closable=closable;
		this.maximizable=maximizable;
		
		p_outer=new JPanel();
		p_title=new JPanel();
		p_input=new JPanel();
		p_content=new JPanel();
		p_select=new JPanel();
		p_button=new JPanel();
		
		lb_title=new JLabel("��ȭ�� ũ�� ����");
		lb_name=new JLabel("��ȭ�� �̸� �Է�");
		t_name=new JTextField(10);
		lb_content=new JLabel("�߰��� ��ȭ���� ��, ���� �������ּ���.");
		
		lb_row=new JLabel("��");
		lb_col=new JLabel("��");
		
		ch_row=new Choice();
		ch_col=new Choice();
		
		bt_confirm=new JButton("Ȯ��");
		bt_cancel=new JButton("���");
		
		for(int i=0; i<10; i++){
			ch_row.add(Integer.toString(i+1));
			ch_col.add(Integer.toString(i+1));
		}
		
		// choice�� ItemListener ����
		ch_row.addItemListener(this);
		ch_col.addItemListener(this);
		
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
		//setSize(300, 200);
		setVisible(true);
		setBackground(Color.YELLOW);
		
		// DB ����
		connect();
	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// ������ �� ���� ����
	// -> theater ���̺� ������ ����
	public void insertTheater(){
		
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into theater(theater_id, name, row_line, column_line, branch_id, movie_id)");
		sql.append(" values(SEQ_THEATER.nextval, ?, ?, ?, 1, null)");
		
		try {
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, t_name.getText());
			pstmt.setString(2, Integer.toString(ch_row.getSelectedIndex()+1));
			pstmt.setString(3, Integer.toString(ch_col.getSelectedIndex()+1));
			
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
	
	// �ϳ��� frame�� ������ ����ϹǷ� ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		t_name.setText("");
		ch_row.select(0);
		ch_col.select(0);
	}
	
	// choice ����(���� ��� ����)
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		Choice ch=(Choice)obj;
		//int index=ch.getSelectedIndex();
		int row_index=ch_row.getSelectedIndex();
		int col_index=ch_col.getSelectedIndex();
	}

	// Ȯ�� ��ư�� ������
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		JButton bt=(JButton)(obj);
		
		// Ȯ�� ��ư�� ������ 
		if(bt==bt_confirm){
			System.out.println("Ȯ�� ����");
			//this.setDefaultCloseOperation(AddTheater.DISPOSE_ON_CLOSE);
			
			// ��ȭ�� �̸��� ����� �Է����� ���� ���
			if(t_name.getText().equals("")){
				JOptionPane.showMessageDialog(this, "��ȭ�� �̸��� ����� �Է����ּ���.");
				return;
			}
			else{
				// Ȯ�� ��ư�� ������ �����Ͱ� theater ���̺� ����ǰ� â ����
				insertTheater();
				//this.dispose();
				this.setVisible(false);
			}
		}
		
		// ��� ��ư�� ������
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
		}
	}

}
