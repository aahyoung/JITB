package com.user.main.purchase.combo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class Combo extends JPanel{ //Combo는 Jpanel을 상속받음
	//라벨 2개, 각각 콤보이름, 가격을 출력
	//이미지, 이미지는 Combo테이블에 적혀있는 이미지 파일 이름을 가지고 서버에서 가져옴 
	//(아직 서버가 없으므로 res_manager에 콤보이미지를 넣어두고 가져오면 됨!)
	//이 3가지 컴포넌트를 여기에 add하고 ComboChoiceScreen에 부착
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
