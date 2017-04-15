package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class TicketConfirmScreen extends ScreenFrame implements MouseMotionListener{
	JPanel panel;
	JLabel label;
	Canvas canvasFrame;
	AdvancedTicket rect;
	ArrayList<AdvancedTicket> boxs = new ArrayList<AdvancedTicket>();
	
	boolean color = false;
	boolean isDragged = false;
	int offY;
	
	Image buffrImg;
	Graphics2D buffr;
	
	public TicketConfirmScreen(ClientMain main) {
		super(main);
		
		panel = new JPanel();
		label = new JLabel("�߱��� ���ϴ� Ƽ���� �������ּ���");
		
		//db���� ������ ������ŭ AdvancedTicke�� ����, y���� 25���� ����
		rect = new AdvancedTicket(25, 25, 750, 250);
		
		canvasFrame = new Canvas(){
			@Override
			public void paint(Graphics g) {
				buffrImg = createImage(800, 1150);
				buffr = (Graphics2D)buffrImg.getGraphics();
				//Graphics2D g2 = (Graphics2D)g;
				if(color == false){
					buffr.setColor(Color.WHITE);
				}else{
					buffr.setColor(Color.YELLOW);
				}
				buffr.draw(rect);
				buffr.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
				buffr.drawString(rect.movie_name, rect.x+10, rect.y+50);
				buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
				buffr.drawString(rect.branch+"("+rect.theater+")", rect.x+10, rect.y+100);
				buffr.drawString(rect.persons, rect.x+10, rect.y+150);
				buffr.drawString(rect.movie_time, rect.x+10, rect.y+230);
				
				URL url = getClass().getResource("/"+rect.img);
				try {
					Image img = ImageIO.read(url);
					buffr.drawImage(img, rect.x+550, rect.y, 200, 250, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				g.drawImage(buffrImg, 0, 0, 800, 1150, this);
			}
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
		};
		
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		
		panel.setPreferredSize(new Dimension(800, 50));
		canvasFrame.setPreferredSize(new Dimension(800, 1150));
		panel.setBackground(new Color(33,33,33));
		
		panel.add(label);
		
		add(panel);
		add(canvasFrame);
		
		canvasFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isDragged = true;
				offY = e.getY() - rect.y;
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(rect.contains(new Point(e.getX(), e.getY()))){
					color = !color;
					canvasFrame.repaint();
				}
				isDragged = false;
			}
			
		});
		canvasFrame.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("���巡����");
		if(isDragged){
			//�����ͺ��̽����� �ҷ��� ��ü���� �� y���� ���ؼ� ȭ�� ũ�⺸�� Ŭ ��� ���� ������ y����ŭ�� �巡�� �ǰ� �ϱ�
			rect.y = e.getY()-offY;
			rect.translate(0, 10);
		}
		canvasFrame.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}