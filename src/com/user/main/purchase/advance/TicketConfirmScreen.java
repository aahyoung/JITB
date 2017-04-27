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
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;
import com.user.main.SelectCombo;
import com.user.main.SelectList;

public class TicketConfirmScreen extends ScreenFrame implements MouseMotionListener{
	JPanel panel;
	JLabel label;
	Canvas canvasFrame;
	AdvancedTicket rect;
	Canvas print;
	
	boolean color = false;
	boolean isDragged = false;
	int offY;
	
	Image buffrImg;
	Graphics2D buffr;
	
	public TicketConfirmScreen(ClientMain main) {
		super(main);
		
		panel = new JPanel();
		label = new JLabel("발권을 원하는 티켓을 선택해주세요");
		
		//db에서 꺼내온 정보만큼 AdvancedTicke을 생성, y축은 25간격 유지
		rect = new AdvancedTicket(25, 25, 750, 250);
		
		canvasFrame = new Canvas(){
			@Override
			public void paint(Graphics g) {
				buffrImg = createImage(800, 800);
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
				
				try {
					URL url = new URL("http://localhost:9090/image/movie/"+rect.poster);
					Image img;
					try {
						img = ImageIO.read(url);
						buffr.drawImage(img, rect.x+550, rect.y, 200, 250, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				//URL url = getClass().getResource("/"+rect.poster);
				g.drawImage(buffrImg, 0, 0, 800, 800, this);
			}
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
		};
		
		print = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/print.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 280, 5, 250, 120, this);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		
		panel.setPreferredSize(new Dimension(800, 50));
		canvasFrame.setPreferredSize(new Dimension(800, 800));
		print.setPreferredSize(new Dimension(800, 150));
		
		print.setBackground(new Color(33,33,33));
		panel.setBackground(new Color(33,33,33));
		
		panel.add(label);
		
		add(panel);
		add(canvasFrame);
		add(print);
		
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
		
		print.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.removeScreen();
				main.createScreen();
				main.selectList = new SelectList();
				main.selectCombo = new SelectCombo();
				main.movie = false;
				main.combo = false;
				main.setPage(0);
			}
		});
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("나드래그함");
		if(isDragged){
			//데이터베이스에서 불러온 객체들의 총 y값을 합해서 화면 크기보다 클 경우 가장 마지막 y값만큼만 드래그 되게 하기
			rect.y = e.getY()-offY;
			rect.translate(0, 10);
		}
		canvasFrame.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
