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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jitb.db.DBManager;
import com.manage.main.Main;

//상품을 추가하는 패널
public class Inventory extends JPanel implements ActionListener, ItemListener {

	JButton bt_add, bt_del,bt_add_sub;
	Choice choice;
	JPanel p_table, p_north;
	TablePanel tablepanel;
	Connection con;
	JTable table;
	JScrollPane scroll;
	DBManager manager = DBManager.getInstance();
	String name = null;
	public Inventory() {
		init();
		tablepanel = new TablePanel(con, "combo");
		table = new JTable(tablepanel);
		scroll = new JScrollPane(table);
		p_north = new JPanel();
		bt_add = new JButton("추가");
		bt_del = new JButton("삭제");
		bt_add_sub=new JButton("재고 추가");
		choice = new Choice();

		bt_add.addActionListener(this);
		bt_del.addActionListener(this);
		bt_add_sub.addActionListener(this);
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
		p_north.add(bt_add_sub);
		add(p_north, BorderLayout.NORTH);
		setBackground(Color.BLUE);
		getList("combo");

	}

	public void init() {
		con = manager.getConnect();
	}

	public void table() {
		table = new JTable(tablepanel);
		table.updateUI();
	}

	public void getList(String name) {
		table.setModel(tablepanel = new TablePanel(con, name));
		table.updateUI();
		this.add(scroll, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(1000, 650));
	}

	// 초이스용
	@Override
	public void itemStateChanged(ItemEvent e) {
		int index = choice.getSelectedIndex();
		if (index == 1) {
			name = "combo";
			getList(name);

		} else if (index == 2) {
			name = "top_opt";
			getList(name);
		} else if (index == 3) {
			name = "top_opt_size";
			getList(name);
		} else if (index == 4) {
			name = "sub_opt";
			getList(name);
		}

	}

	public void add() {
		int index = choice.getSelectedIndex();
		if (index == 1) {
			name = "combo";
			new Add_combo(this.table);
		} else if (index == 2) {
			name = "top_opt";
			new Add_top(this.table);
		} else if (index == 3) {
			name = "top_opt_size";
			new Add_top_size(this.table);
		} else if (index == 4) {
			name = "sub_opt";
			new Add_sub(this.table);
		}
	}

	public void del() {
		int row = table.getSelectedRow() + 1;
		int index = choice.getSelectedIndex();
		int id = Integer.parseInt(table.getValueAt(row - 1, 0).toString());
		System.out.println(row);
		if (index == 1) {
			name = "combo";
			new delMain(con, table, name, row, id);
		} else if (index == 2) {
			name = "top_opt";
			new delMain(con, table, name, row, id);

		} else if (index == 3) {
			name = "top_opt_size";
			new delMain(con, table, name, row, id);

		} else if (index == 4) {
			name = "sub_opt";
			new delMain(con, table, name, row, id);

		}
	}
	public void addStack(){
		new Add_stack(con,table);
	}
	// 버튼용
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_add) {
			add();
		} else if (obj == bt_del) {
			del();
		} else if (obj == bt_add_sub) {
			addStack();
		}
	}
}
