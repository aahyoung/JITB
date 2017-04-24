package com.manage.movie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Seat extends JPanel{
	boolean isSeat=true;
	
	// �� �¼�
	public Seat() {
		setBackground(Color.gray);
		setSize(new Dimension(10, 10));
		
		// �� �¼��� ������ ��
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// �¼��� ���
				if(isSeat){
					// ������ ����
					setBackground(Color.cyan);
					isSeat=false;
				}
				// ������ ���
				else{
					setBackground(Color.gray);
					isSeat=true;
				}
			}
		});
	}
}
