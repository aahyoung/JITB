/*
 * 접속자마다 1:1로 대응시키기 위한 서버측의 대화용 쓰레드
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
	 * 클라이언트 -> 서버
	 * 파일을 읽어서 서버에 전송
	 * 서버는 읽은 내용을 파일로 저장
	 * -> 읽는 만큼 전송
	 * 클라이언트에서는 파일을 fis로 읽기만, 서버에서는 파일을 받아 fos로 받기
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
	
	// 클라이언트 측에서 보낸 이미지 파일 받기
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
					System.out.println("파일 읽기 완료");
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
