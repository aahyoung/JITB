/*
 * �����ڸ��� 1:1�� ������Ű�� ���� �������� ��ȭ�� ������
 * */
package com.jitb.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ServerThread extends Thread{
	Socket socket;
	ServerMain serverMain;

	/*
	 * Ŭ���̾�Ʈ -> ����
	 * ������ �о ������ ����
	 * ������ ���� ������ ���Ϸ� ����
	 * -> �д� ��ŭ ����
	 * Ŭ���̾�Ʈ������ ������ fis�� �б⸸, ���������� ������ �޾� fos�� �ޱ�
	 * */
	FileInputStream fis;
	FileOutputStream fos;
	
	InputStream is;
	OutputStream os;
	
	boolean listenFlag=true;
	
	String path="C:/Hyeona/myServer/data/";
	
	
	boolean flag;
	
	public ServerThread(ServerMain serverMain, Socket socket) {
		this.serverMain = serverMain;
		this.socket = socket;
		
		flag = true;
		
		try {
			is=socket.getInputStream();
			os=socket.getOutputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	// Ŭ���̾�Ʈ ������ ���� �̹��� ���� �ޱ�
	public void listen(){
		byte[] b=new byte[1024];
		File file;
		int length;
		try {
			BufferedImage img=ImageIO.read(is);
			fos=new FileOutputStream(path+"test.jpg");
			ImageIO.write(img, "jpg", fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					System.out.println("���� �б� �Ϸ�");
					fos.close();
					listenFlag=false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				listenFlag=true;
			}
		}
	}
	
	public void send(String msg){
		
	}
	
	public void run() {
		while(listenFlag){
			listen();
		}
	}
}
