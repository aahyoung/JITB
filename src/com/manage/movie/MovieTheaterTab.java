package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

// ��ȭ��/��ȭ ���� �г�
public class MovieTheaterTab extends JPanel{
	JTabbedPane tabbedPane;
	JComponent theater;
	JComponent movie;
	
	TheaterMain theaterMain;
	MovieMain movieMain;
	
	public MovieTheaterTab() {
		//super(new GridLayout(1,1));
		
		tabbedPane = new JTabbedPane();
		
		theaterMain=new TheaterMain();
		movieMain=new MovieMain();
		
		// tab���� : ��ȭ��, ���� : theater
		theater=makeInnerPanel(theaterMain);
		tabbedPane.addTab("��ȭ��", theater);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		movie=makeInnerPanel(movieMain);
		tabbedPane.addTab("��ȭ", movie);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
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
		//JLabel type=new JLabel(text);
		// label ũ�� ���� ���ϸ� �۰� ���Ϳ�! �� �������ּ��� :)
		//type.setPreferredSize(new Dimension(100, 40));
		//type.setHorizontalAlignment(JLabel.CENTER);
		//panel.setLayout(new GridLayout(1, 1));
		panel.setLayout(new BorderLayout());
		panel.add(subPanel);
		// panel ũ�� ���� ���ϸ� �۰� ���Ϳ�! �� �������ּ��� :)
		panel.setPreferredSize(new Dimension(1000, 750));
		
		return panel;
	}
}
