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
	
	// ���� ����� ������ �޴� �뵵
	Thread connectThread;
	Vector<ServerThread> threadList = new Vector<ServerThread>();	// ��Ƽ������ ���
	
	public ServerMain() {
		// ���� ����
		try {
			server=new ServerSocket(port);
			
			connectThread=new Thread(this);
			connectThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		// ������ ���� ����
		while(true){
			try {
				// ������ ����
				Socket socket=server.accept();
				ServerThread st=new ServerThread(this, socket);
				st.start();
				// ������ ��ܿ� �߰�
				threadList.addElement(st);
				System.out.println(threadList.size()+" �� ����");
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}
