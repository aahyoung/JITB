package com.jitb.client;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

import javax.imageio.ImageIO;

import org.omg.CORBA.Any;
import org.omg.CORBA.DataOutputStream;
import org.omg.CORBA.TypeCode;

public class ClientThread extends Thread{
	Socket socket;

	ClientMain clientMain;
	
	FileInputStream fis;
	BufferedWriter buffw;
	OutputStream img_os;
	
	boolean sendFlag=true;
	
	int size;
	
	public ClientThread(ClientMain clientMain, Socket socket) {
		this.clientMain=clientMain;
		this.socket=socket;
		
		try {
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*	
	// 듣기
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
	// 말하기
	public void send(){
		//fos=new FileOutputStream(clientMain.file);
		//fos.write(b);
		try {
			String fileName=clientMain.file.getName();
			fis=new FileInputStream(clientMain.file);
			System.out.println("보내는 파일명 : "+fileName);
			buffw.write(fileName+"\n");
			buffw.flush();
			
			img_os=socket.getOutputStream();
			BufferedImage img=ImageIO.read(clientMain.file);
			ImageIO.write(img, "jpg", img_os);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis!=null){
				try {
					fis.close();
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
