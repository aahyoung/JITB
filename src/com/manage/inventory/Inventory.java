package com.manage.inventory;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jitb.db.DBManager;

//상품을 추가하는 패널
public class Inventory extends JPanel implements ActionListener,ItemListener{
	
	JButton bt_add,bt_del;
	Choice choice;
	JPanel p_table,p_north;
	TablePanel tablepanel;
	Connection con;
	JTable table_up;
	DBManager manager = DBManager.getInstance();
	String name=null;
	public Inventory() {
		init();
		tablepanel=new TablePanel(con, "combo");
		table_up=new JTable(tablepanel);
		p_north=new JPanel();
		bt_add=new JButton("추가");
		bt_del=new JButton("삭제");
		choice=new Choice();
		
		bt_add.addActionListener(this);
		bt_del.addActionListener(this);
		
		setLayout(new BorderLayout());

		choice.add("목록을 선택해 주세요");
		choice.add("콤보");
		choice.add("상위 옵션");
		choice.add("상위 사이즈 옵션");
		choice.add("하위 옵션");
		
		choice.addItemListener(this);
		
		p_north.add(choice);
		p_north.add(bt_add);
		p_north.add(bt_del);
		add(p_north,BorderLayout.NORTH);
		setBackground(Color.BLUE);
		getList("combo");

	}
	public void init() {
		con = manager.getConnect();
	}
	public void table(){
		table_up=new JTable(tablepanel);
		table_up.updateUI();
	}
	public void getList(String name){
		table_up.setModel(tablepanel=new TablePanel(con,name));
		table_up.updateUI();
		this.add(table_up,BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(1000, 650));
	}
	//초이스용
	@Override
	public void itemStateChanged(ItemEvent e) {
		int index=choice.getSelectedIndex();
		if(index==1){
			name="combo";
			getList(name);
			
		}
		else if(index==2){
			name="top_opt";
			getList(name);
		}
		else if(index==3){
			name="top_opt_size";
			getList(name);
		}
		else if(index==4){
			name="sub_opt";
			getList(name);
		}
		
	}
	public void add(){
		int index=choice.getSelectedIndex();
		if(index==1){
			name="combo";
			new Add_combo(this.table_up);
		}
		else if(index==2){
			name="top_opt";
			new Add_top(this.table_up);
		}
		else if(index==3){
			name="top_opt_size";
			new Add_top_size(this.table_up);
		}
		else if(index==4){
			name="sub_opt";
			new Add_sub(this.table_up);
		}
	}
	public void del(){
		int row=table_up.getSelectedRow()+1;
		int index=choice.getSelectedIndex();
		System.out.println(row);
		if(index==1){
			name="combo";
			new delMain(con, table_up, name, row);
		}
		else if(index==2){
			name="top_opt";
			new delMain(con, table_up, name, row);
			
		}
		else if(index==3){
			name="top_opt_size";
			new delMain(con, table_up, name, row);
			
		}
		else if(index==4){
			name="sub_opt";
			new delMain(con, table_up, name, row);
			
		}
	}
	
	//버튼용 
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_add){
			add();
		}
		else if(obj==bt_del){
			del();
		}
	}
}
