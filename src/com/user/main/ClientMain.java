package com.user.main;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jitb.db.DBManager;
import com.user.main.purchase.ChoiceConfirmScreen;
import com.user.main.purchase.MenuChoiceScreen;
import com.user.main.purchase.PaymentScreen;
import com.user.main.purchase.advance.NoTicketScreen;
import com.user.main.purchase.advance.NumberCheckScreen;
import com.user.main.purchase.advance.TicketConfirmScreen;
import com.user.main.purchase.combo.ComboChoiceScreen;
import com.user.main.purchase.combo.GifticonChoiceScreen;
import com.user.main.purchase.combo.OptionChoiceScreen;
import com.user.main.purchase.ticket.MovieChoiceScreen;
import com.user.main.purchase.ticket.PersonsChoiceScreen;
import com.user.main.purchase.ticket.SeatsChoiceScreen;

public class ClientMain extends JFrame implements Runnable{
	private DBManager manager;
	public Connection con;
	public ArrayList<JPanel> screen;
	
	Socket socket;
	
	String[] days = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
	
	Calendar cal;
	public int yy, mm, dd, h, m, day;
	String ap;
	Thread thread;
	public boolean flag = true;
	
	public SelectList selectList = new SelectList();
	public SelectCombo selectCombo = new SelectCombo();
	public boolean movie = false;
	public boolean combo = false;
	
	/*
	 * 생성자
	 * - 모든 화면 스크린 패널 생성
	 */
	public ClientMain() {
		setLayout(new FlowLayout());
		
		init();
		createScreen();
		
		thread = new Thread(this);
		thread.start();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				manager.disConnect(con);
				System.exit(0);
			}
		});
		setSize(800, 1200);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void createScreen(){
		
		screen = new ArrayList<JPanel>();
		
		//index=0
		screen.add(new InitScreen(this));
		//index=1
		screen.add(new MenuChoiceScreen(this));
		//index=2
		screen.add(new NumberCheckScreen(this));
		//index=3
		screen.add(new NoTicketScreen(this));
		//index=4
		screen.add(new MovieChoiceScreen(this));
		//index=5
		screen.add(new PersonsChoiceScreen(this));
		//index=6
		screen.add(new SeatsChoiceScreen(this));
		//index=7
		screen.add(new ComboChoiceScreen(this));
		//index=8
		screen.add(new GifticonChoiceScreen(this));
		//index=9
		screen.add(new OptionChoiceScreen(this));
		//index=10
		screen.add(new TicketConfirmScreen(this));
		//index=11
		screen.add(new ChoiceConfirmScreen(this));
		//index=12
		screen.add(new PaymentScreen(this));
		
		for(int i=0; i<screen.size(); i++){
			add(screen.get(i));
		}
		
		setPage(0);
	}
	
	public void removeScreen(){
		for(int i=0; i<screen.size(); i++){
			remove(screen.get(i));
		}
	}
	
	/*
	 * void init()
	 * - 데이터베이스 객체를 가져옴
	 * - 전체 스크린을 담기 위한 ArrayList배열 생성
	 */
	public void init(){
		manager = DBManager.getInstance();
		con = manager.getConnect();
	}
	
	/*
	 * void setPage(int index)
	 * - 파라미터 값으로 받은 index 페이지로 화면 전환
	 */
	public void setPage(int index){
		for(int i=0; i<screen.size(); i++){
			if(i == index){
				screen.get(i).setVisible(true);
			}else{
				screen.get(i).setVisible(false);
			}
		}
	}
	
	public void setTime(){
		cal = Calendar.getInstance();
		String str_h;
		String str_m;
		
		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH);
		dd = cal.get(Calendar.DATE);
		h = cal.get(Calendar.HOUR);
		m = cal.get(Calendar.MINUTE);
		day = cal.get(Calendar.DAY_OF_WEEK);
		
		if(cal.get(Calendar.AM_PM)==Calendar.AM){
			ap = "am";
		}else{
			ap = "pm";
		}
		
		if(h<10){
			str_h = "0"+Integer.toString(h);
		}else{
			str_h = Integer.toString(h);
		}
		if(m<10){
			str_m = "0"+Integer.toString(m);
		}else{
			str_m = Integer.toString(m);
		}
		
		String date = mm+"월 "+dd+"일 "+days[day-1];
		String time = str_h+":"+str_m+ap;
		
		((MenuChoiceScreen)screen.get(1)).la_date.setText(date);
		((MenuChoiceScreen)screen.get(1)).la_time.setText(time);
		((MovieChoiceScreen)screen.get(4)).la_time.setText(time);
	}
	
	@Override
	public void run() {
		while(flag){
			setTime();
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 실행부
	 */
	public static void main(String[] args){
		new ClientMain();
	}
}
