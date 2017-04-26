package com.user.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;

public class InitScreen extends ScreenFrame{
	public JPanel touch;
	Image poster_img;
	Image touch_on_img;
	Image touch_off_img;
	Image temp;
	public URL poster_url; 
	
	boolean flag = false;
	
	
	public InitScreen(ClientMain main) {
		super(main);
		
		poster_url = getClass().getResource("/eternal_sunshine.png");
		URL touch_url_off = getClass().getResource("/touch_off.png");
		URL touch_url_on = getClass().getResource("/touch_on.png");
		
		
		touch = new JPanel(){
			@Override
			public void paint(Graphics g) {
				try {
					poster_img = ImageIO.read(poster_url);
					touch_off_img = ImageIO.read(touch_url_off);
					touch_on_img = ImageIO.read(touch_url_on);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				g.drawImage(poster_img, 0, 0, 800, 1050, this);
				
				if(flag==false){
					g.drawImage(touch_on_img, 280, 300, 249, 179, this);
				}else{
					g.drawImage(touch_off_img, 280, 300, 249, 179, this);
				}
			}
		};
		touch.setPreferredSize(new Dimension(800, 1200));
		add(touch);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.setPage(1);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				flag = false;
				touch.repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				flag = true;
				touch.repaint();
			}
		});
	}
	
	
}
