package com.user.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	Socket socket;
	ServerMain main;
	
	BufferedReader buffr;
	BufferedWriter buffw;
	
	boolean flag;
	
	public ServerThread(Socket socket, ServerMain main) {
		this.socket = socket;
		this.main = main;
		
		flag = true;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void listen(){
		
	}
	
	public void send(String msg){
		//�ǽð����� �¼������� ����ڿ��� ����ȭ
	}
	
	@Override
	public void run() {
		
	}
}
