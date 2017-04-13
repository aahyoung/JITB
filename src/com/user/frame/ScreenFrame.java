package com.user.frame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.main.ClientMain;

public class ScreenFrame extends JPanel{
	protected ClientMain main;
	private JLabel label;
	
	/*
	 * ������
	 * - �ӽ÷� ȭ���� �����ϱ� ���� �����ڷ� Ŭ�������� �����
	 * - �ӽ� ���� ����
	 * - ��ü ��ũ�� ũ�� ����
	 */
	public ScreenFrame(ClientMain main) {
		this.main = main;
		
		label = new JLabel(this.getClass().getName());
		add(label);
		
		setVisible(false);
		setBackground(new Color(33,33,33));
		setPreferredSize(new Dimension(800, 1200));
	}
}
