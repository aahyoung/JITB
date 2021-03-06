package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.manage.main.Main;

// 영화관/영화 관리 패널
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
		movieMain=new MovieMain(this);
		
		// tab제목 : 영화관, 내용 : theater
		theater=makeInnerPanel(theaterMain);
		tabbedPane.addTab("영화관", theater);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		movie=makeInnerPanel(movieMain);
		tabbedPane.addTab("영화", movie);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		add(tabbedPane);
		
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		setBackground(Color.white);
		
		// 메인Frame의 버튼을 클릭했을 때만 보여져야 하므로 지금은 false
		setVisible(false);
	}
	
	/* 
	 * JTabbedPane에서 tab과 content를 만드는 메소드
	 * 현재는 content안에 JLabel만 들어가있지만, 추후 이것도 JPanel로 구현할 것!
	 * */
	protected JComponent makeInnerPanel(JPanel subPanel){
		JPanel panel=new JPanel(false);
		//JLabel type=new JLabel(text);
		// label 크기 지정 안하면 작게 나와요! 꼭 지정해주세요 :)
		//type.setPreferredSize(new Dimension(100, 40));
		//type.setHorizontalAlignment(JLabel.CENTER);
		//panel.setLayout(new GridLayout(1, 1));
		panel.setLayout(new BorderLayout());
		panel.add(subPanel);
		// panel 크기 지정 안하면 작게 나와요! 꼭 지정해주세요 :)
		panel.setPreferredSize(new Dimension(1000, 750));
		
		return panel;
	}
}
