package com.jitb.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain implements Runnable{
	JPanel p_north;
	JLabel label;
	JTextField t_port;
	JButton bt_start;
	JTextArea console;
	JScrollPane scroll;
	
	ServerSocket server;
	int port = 9090;
	
	// 다중 사용자 접속을 받는 용도
	Thread connectThread;
	Vector<ServerThread> threadList = new Vector<ServerThread>();	// 멀티쓰레드 담기
	
	public ServerMain() {
		// 서버 생성
		try {
			server=new ServerSocket(port);
			
			connectThread=new Thread(this);
			connectThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		// 접속자 무한 감지
		while(true){
			try {
				// 접속자 감지
				Socket socket=server.accept();
				ServerThread st=new ServerThread(this, socket);
				st.start();
				// 접속자 명단에 추가
				threadList.addElement(st);
				System.out.println(threadList.size()+" 명 접속");
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}
