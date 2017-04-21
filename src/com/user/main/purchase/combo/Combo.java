package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class Combo extends JPanel{ //Combo�� Jpanel�� ��ӹ���
	//�� 2��, ���� �޺��̸�, ������ ���
	//�̹���, �̹����� Combo���̺� �����ִ� �̹��� ���� �̸��� ������ �������� ������ 
	//(���� ������ �����Ƿ� res_manager�� �޺��̹����� �־�ΰ� �������� ��!)
	//�� 3���� ������Ʈ�� ���⿡ add�ϰ� ComboChoiceScreen�� ����
	JLabel combo_name,combo_price;
	Canvas combo_img;
	String name;
	String img;
	int price,id;
	BufferedImage image = null;
	JFileChooser chooser;
	
	public Combo(int id,String name,int price,String img) {
		this.id=id;
		this.name=name;
		this.price=price;
		this.img=img;
		
		combo_name=new JLabel(name,JLabel.CENTER);
		combo_price=new JLabel(Integer.toString(price),JLabel.CENTER);
		combo_img=new Canvas();
		
		combo_name.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
		combo_price.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
		
		combo_name.setForeground(Color.white);
		combo_price.setForeground(Color.WHITE);
		
		combo_name.setPreferredSize(new Dimension(240, 35));
		combo_price.setPreferredSize(new Dimension(240, 35));
		
		try {
			URL url = this.getClass().getResource("/"+img);
			image = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		combo_img = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, 240,300,this);
			}
		};
		combo_img.setPreferredSize(new Dimension(240, 300));
		
		combo_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				select();
			}
		});
		
		combo_name.setText(name);
		combo_price.setText(Integer.toString(price));
		
		this.add(combo_name);
		this.add(combo_price);
		
		this.add(combo_img);
		setBackground(new Color(33, 33, 33));
		setPreferredSize(new Dimension(250, 400));
	}
	public void select(){
		System.out.println(name);
	}
}
