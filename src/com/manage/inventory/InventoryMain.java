package com.manage.inventory;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class InventoryMain extends JPanel{
	JTabbedPane tabbedPane;
	JComponent item_add;   //�߰��� â
	Inventory inventory;
	
	JButton bt_add;
	Choice choice;
	public InventoryMain() {
		//super(new GridLayout(1,1));
		bt_add=new JButton("�߰�");
		inventory=new Inventory();
		tabbedPane = new JTabbedPane();
		
		// tab���� : ��ȭ��, ���� : theater
		item_add=makeInnerPanel(inventory);
		tabbedPane.addTab("��� Ȯ��", item_add);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		item_add.setLayout(new BorderLayout());
		
		add(tabbedPane);
		
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// ����Frame�� ��ư�� Ŭ������ ���� �������� �ϹǷ� ������ false
		setVisible(false);
	}
	
	/* 
	 * JTabbedPane���� tab�� content�� ����� �޼ҵ�
	 * ����� content�ȿ� JLabel�� ��������, ���� �̰͵� JPanel�� ������ ��!
	 * */
	protected JComponent makeInnerPanel(JPanel subPanel){
		JPanel panel=new JPanel(false);
		panel.setLayout(new BorderLayout());
		panel.add(subPanel);
		panel.setPreferredSize(new Dimension(1000, 750));
		
		return panel;
	}
	
}
