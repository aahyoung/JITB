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
	
	public TicketConfirmScreen(ClientMain main) {
		super(main);
		
		panel = new JPanel();
		label = new JLabel("발권을 원하는 티켓을 선택해주세요");
		
		//db에서 꺼내온 정보만큼 AdvancedTicke을 생성, y축은 25간격 유지
		rect = new AdvancedTicket(25, 25, 750, 250);
		
		canvasFrame = new Canvas(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				if(color == false){
					g2.setColor(Color.WHITE);
				}else{
					g2.setColor(Color.YELLOW);
				}
				g2.draw(rect);
				g2.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
				g2.drawString(rect.movie_name, rect.x+10, rect.y+50);
				g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
				g2.drawString(rect.branch+"("+rect.theater+")", rect.x+10, rect.y+100);
				g2.drawString(rect.persons, rect.x+10, rect.y+150);
				g2.drawString(rect.movie_time, rect.x+10, rect.y+230);
				
				URL url = getClass().getResource("/"+rect.img);
				try {
					Image img = ImageIO.read(url);
					g2.drawImage(img, rect.x+550, rect.y, 200, 250, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		System.out.println("나드래그함");
		if(isDragged){
			//데이터베이스에서 불러온 객체들의 총 y값을 합해서 화면 크기보다 클 경우 가장 마지막 y값만큼만 드래그 되게 하기
			rect.y = e.getY()-offY;
		}
		canvasFrame.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
