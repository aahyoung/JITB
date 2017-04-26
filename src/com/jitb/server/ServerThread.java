/*
 * �����ڸ��� 1:1�� ������Ű�� ���� �������� ��ȭ�� ������
 * */
package com.jitb.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
	
	BufferedReader buffr;
	InputStream img_is;
	OutputStream os;
	
	boolean listenFlag=true;
	
	String path="C:/Hyeona/myServer/data/";
	
	String fileName;
	
	boolean flag;
	
	public ServerThread(ServerMain serverMain, Socket socket) {
		this.serverMain = serverMain;
		this.socket = socket;
		
		flag = true;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			img_is=socket.getInputStream();
			os=socket.getOutputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Ŭ���̾�Ʈ ������ ���� �̹��� ���� �ޱ�
	public void listen(){
		byte[] b=new byte[1024];
		
		try {
			fileName=buffr.readLine();
			System.out.println("���ϸ� : "+fileName);
			
			BufferedImage img=ImageIO.read(img_is);

			fos=new FileOutputStream(path+fileName);
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
