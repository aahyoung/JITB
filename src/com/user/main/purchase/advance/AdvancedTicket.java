package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Rectangle;

//예매된 정보들이 출력되는 객체
public class AdvancedTicket extends Rectangle{
	String movie_name = "킹스맨";
	String branch = "신촌점";
	String theater = "1관";
	String persons = "일반 2명";
	String movie_time = "2017년 4월 13일 12시 30분";
	String img = "kingsman.png";
	
	public AdvancedTicket(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
}
