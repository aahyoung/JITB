package com.user.frame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.user.main.ClientMain;

public class PurchasePanelFrame extends JPanel{
	protected ClientMain main;
	
	public PurchasePanelFrame(ClientMain main) {
		this.main = main;
		
		setBackground(new Color(33, 33, 33));
		setPreferredSize(new Dimension(755, 750));
	}
}
