package com.user.main.purchase;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.user.frame.PurchasePanelFrame;
import com.user.main.ClientMain;

public class PointOutputMessage extends PurchasePanelFrame{
	JPanel content;
	JPanel can_default;
	Canvas bt_cancle;
	
	public PointOutputMessage(ClientMain main) {
		super(main);
		
		content = new JPanel(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/point_output_message.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 50, 527, 402, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		can_default = new JPanel();
		bt_cancle = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_cancle.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, -10, 0, 200, 50, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		bt_cancle.setBackground(Color.WHITE);
		content.setBackground(new Color(33,33,33));
		
		can_default.setPreferredSize(new Dimension(550, 350));
		bt_cancle.setPreferredSize(new Dimension(200, 50));
		content.setPreferredSize(new Dimension(550, 700));
		
		content.add(can_default);
		content.add(bt_cancle);
		add(content);
	}
}
