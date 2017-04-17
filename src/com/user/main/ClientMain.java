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
import com.user.main.purchase.ConfirmScreen;
import com.user.main.purchase.DiscountChoiceScreen;
import com.user.main.purchase.MenuChoiceScreen;
import com.user.main.purchase.PaymentChoiceScreen;
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
	
	private int port = 7777; //����ڿ��� ������� �ʰ� �ٷ� �����
	private String ip = "localhost"; //���� ��Ʈ��ũ�� �ٸ� �������� �׽�Ʈ �غ� ��
	private Socket socket;
	
	String[] days = {"�Ͽ���", "������", "ȭ����", "������", "�����", "�ݿ���", "�����"};
	
	Calendar cal;
	public int yy, mm, dd, h, m, day;
	String ap;
	Thread thread;
	
	/*
	 * ������
	 * - ��� ȭ�� ��ũ�� �г� ����
	 */
	public ClientMain() {
		setLayout(new FlowLayout());
		
		init();
		
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
		screen.add(new ConfirmScreen(this));
		screen.add(new DiscountChoiceScreen(this));
		screen.add(new PaymentChoiceScreen(this));
		
		for(int i=0; i<screen.size(); i++){
			add(screen.get(i));
		}
		
		setPage(0);
		
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
	
	/*
	 * void init()
	 * - �����ͺ��̽� ��ü�� ������
	 * - ��ü ��ũ���� ��� ���� ArrayList�迭 ����
	 */
	public void init(){
		manager = DBManager.getInstance();
		con = manager.getConnect();
		
		screen = new ArrayList<JPanel>();
	}
	
	/*
	 * void setPage(int index)
	 * - �Ķ���� ������ ���� index �������� ȭ�� ��ȯ
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
	
	public void connect(){
		try {
			socket = new Socket(ip, port);
			ClientThread ct;
		} catch (IOException e) {
			e.printStackTrace();
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
		
		String date = mm+"�� "+dd+"�� "+days[day-1];
		String time = str_h+":"+str_m+ap;
		
		((MenuChoiceScreen)screen.get(1)).la_date.setText(date);
		((MenuChoiceScreen)screen.get(1)).la_time.setText(time);
		
		((MovieChoiceScreen)screen.get(4)).la_time.setText(time);
	}
	
	@Override
	public void run() {
		while(true){
			setTime();
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * �����
	 */
	public static void main(String[] args){
		new ClientMain();
	}
}
