package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Seat extends JPanel{
	boolean isSeat=true;
	
	// 각 좌석
	public Seat() {
		setBackground(Color.gray);
		setSize(new Dimension(10, 10));
		
		// 각 좌석을 선택할 때
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// 좌석인 경우
				if(isSeat){
					// 복도로 설정
					setBackground(Color.cyan);
					isSeat=false;
				}
				// 복도인 경우
				else{
					setBackground(Color.gray);
					isSeat=true;
				}
			}
		});
	}
}
