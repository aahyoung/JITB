package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;

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
	int price;
	public Combo(String name,int price) {
		this.name=name;
		this.price=price;
		
		combo_name=new JLabel(name);
		combo_price=new JLabel(Integer.toString(price));
		combo_img=new Canvas();
		
		this.add(combo_name);
		this.add(combo_price);
		
		
		setBackground(Color.BLACK);
	}
}
