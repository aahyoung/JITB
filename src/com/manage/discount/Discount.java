package com.manage.discount;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
	import javax.swing.event.ChangeEvent;
	import javax.swing.event.ListSelectionEvent;
	import javax.swing.event.TableModelEvent;
	import javax.swing.event.TableModelListener;

	import com.jitb.db.DBManager;

	//상품을 추가하는 패널
	public class Discount extends JPanel implements ActionListener,ItemListener{
		
		JButton bt_add,bt_del;
		Choice choice;
		JPanel p_table,p_north;
		table_model tablemodel;
		Connection con;
		JTable table_up;
		JScrollPane scroll;
		DBManager manager = DBManager.getInstance();
		String name=null;
		public Discount() {
			init();
			tablemodel=new table_model(con, "discount_info");
			table_up=new JTable(tablemodel);
			scroll=new JScrollPane(table_up);
			p_north=new JPanel();
			bt_add=new JButton("추가");
			bt_del=new JButton("삭제");
			choice=new Choice();
			
			bt_add.addActionListener(this);
			bt_del.addActionListener(this);
			
			setLayout(new BorderLayout());

			choice.add("목록을 선택해 주세요");
			choice.add("할인 정보");
			choice.add("할인 회사");
			choice.add("상품권 정보");
			choice.add("상품권 회사");
			
			choice.addItemListener(this);
			
			p_north.add(choice);
			p_north.add(bt_add);
			p_north.add(bt_del);
			add(p_north,BorderLayout.NORTH);
			setBackground(Color.BLUE);
			getList("discount_info");

		}
		public void init() {
			con = manager.getConnect();
		}
		public void table(){
			table_up=new JTable(tablemodel);
			table_up.updateUI();
		}
		public void getList(String name){
			table_up.setModel(tablemodel=new table_model(con,name));
			table_up.updateUI();
			this.add(scroll,BorderLayout.CENTER);
			this.setPreferredSize(new Dimension(1000, 650));
		}
		//초이스용
		@Override
		public void itemStateChanged(ItemEvent e) {
			int index=choice.getSelectedIndex();
			if(index==1){
				name="discount_info";
				getList(name);
				
			}
			else if(index==2){
				name="discount_type";
				getList(name);
			}
			else if(index==3){
				name="gift_info";
				getList(name);
			}
			else if(index==4){
				name="gift_type";
				getList(name);
			}
			
		}
		public void add(){
			int index=choice.getSelectedIndex();
			if(index==1){
				name="discount_info";
				new Add_Discount_info(this.table_up);
			}
			else if(index==2){
				name="discount_type";
				new Add_Discount_type(this.table_up);
			}
			else if(index==3){
				name="gift_info";
				new add_gift_info(this.table_up);
			}
			else if(index==4){
				name="gift_type";
				new Add_gift_type(this.table_up);
			}
		}
		public void del(){
			int row=table_up.getSelectedRow()+1;
			int index=choice.getSelectedIndex();
			System.out.println(row);
			if(index==1){
				name="discount_info";
				new del_discount(con, table_up, name, row);
			}
			else if(index==2){
				name="discount_type";
				new del_discount(con, table_up, name, row);
				
			}
			else if(index==3){
				name="gift_type";
				new del_discount(con, table_up, name, row);
				
			}
			else if(index==4){
				name="gift_info";
				new del_discount(con, table_up, name, row);
				
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

