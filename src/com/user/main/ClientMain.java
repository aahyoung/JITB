package com.user.main;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jitb.db.DBManager;
import com.user.main.purchase.ConfirmScreen;
import com.user.main.purchase.DiscountChoiceScreen;
import com.user.main.purchase.MenuChoiceScreen;
import com.user.main.purchase.PaymentChoiceScreen;
import com.user.main.purchase.advance.NoTicketScreen;
import com.user.main.purchase.advance.NumberCheckScreen;
import com.user.main.purchase.combo.ComboChoiceScreen;
import com.user.main.purchase.combo.GifticonChoiceScreen;
import com.user.main.purchase.combo.OptionChoiceScreen;
import com.user.main.purchase.ticket.MovieChoiceScreen;
import com.user.main.purchase.ticket.PersonsChoiceScreen;
import com.user.main.purchase.ticket.SeatsChoiceScreen;

public class ClientMain extends JFrame{
	private DBManager manager;
	private Connection con;
	ArrayList<JPanel> screen;
	
	private int port = 7777; //����ڿ��� ������� �ʰ� �ٷ� �����
	private String ip = "localhost"; //���� ��Ʈ��ũ�� �ٸ� �������� �׽�Ʈ �غ� ��
	private Socket socket;
	
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
		screen.add(new MovieChoiceScreen(this));
		screen.add(new PersonsChoiceScreen(this));
		screen.add(new SeatsChoiceScreen(this));
		screen.add(new ComboChoiceScreen(this));
		screen.add(new GifticonChoiceScreen(this));
		screen.add(new OptionChoiceScreen(this));
		screen.add(new ConfirmScreen(this));
		screen.add(new DiscountChoiceScreen(this));
		screen.add(new PaymentChoiceScreen(this));
		
		for(int i=0; i<screen.size(); i++){
			add(screen.get(i));
		}
		
		setPage(0);
		
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
	
	/*
	 * �����
	 */
	public static void main(String[] args){
		new ClientMain();
	}
}
