package com.jitb.client;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ClientThread extends Thread{
	Socket socket;

	ClientMain clientMain;
	
	FileInputStream fis;
	FileOutputStream fos;
	
	OutputStream os;
	
	boolean sendFlag=true;
	
	int size;
	
	public ClientThread(ClientMain clientMain, Socket socket) {
		this.clientMain=clientMain;
		this.socket=socket;
		
	}
/*	
	// ���
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();
			area.append(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/	
	// ���ϱ�
	public void send(){

		//fos=new FileOutputStream(clientMain.file);
		//fos.write(b);
		try {
			BufferedImage img=ImageIO.read(clientMain.file);
			/*
			fis=new FileInputStream(clientMain.file);
			System.out.println(clientMain.file.getName());
			size=fis.available();
			
			byte[] b=new byte[size];
			int flag;
			while(true){
				flag=fis.read(b);
				if(flag==-1){
					sendFlag=false;
					break;
				}
			}
			*/
			os=socket.getOutputStream();
			ImageIO.write(img, "jpg", os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(os!=null){
				try {
					os.close();
					sendFlag=false;
					System.out.println("Ŭ���̾�Ʈ : �̹��� ���� �Ϸ�");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				sendFlag=true;
			}
		}
	
	}
	
	public void run() {
		while(sendFlag){
			send();
		}
	}

}
