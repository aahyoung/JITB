package com.manage.discountFinal;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.manage.sales.NowMovie;
import com.manage.sales.PastMovie;
import com.manage.sales.SalesMain;
import com.manage.sales.SalesTheater;

public class DiscountF extends JPanel {

	JTabbedPane tab;
	JComponent movie;
	JComponent theater;

	Discount_Final_Panel dfp;

	public DiscountF() {

		tab = new JTabbedPane();
		dfp = new Discount_Final_Panel();

		tab.addTab("할인관리", dfp);

		add(tab);
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		setVisible(false);

	}

	protected JComponent makeInnerPanel(JPanel subPanel) {
		JPanel panel = new JPanel(false);
		panel.setLayout(new BorderLayout());
		panel.add(subPanel);
		panel.setPreferredSize(new Dimension(1000, 750));
		return panel;
	}
}
