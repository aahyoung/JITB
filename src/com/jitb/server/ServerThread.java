/*
 * �����ڸ��� 1:1�� ������Ű�� ���� �������� ��ȭ�� ������
 * */
package com.jitb.server;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.Calendar;

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
	InputStream file_is;
	OutputStream os;
	BufferedInputStream buffis;
	BufferedOutputStream buffos;
	
	boolean listenFlag=true;
	
	//String path="C:/Hyeona/myServer/data/";
	String path="C:/jitb_server/";
	
	String fileName, folder;
	
	int size;
	boolean flag;
	
	public ServerThread(ServerMain serverMain, Socket socket) {
		this.serverMain = serverMain;
		this.socket = socket;
		
		flag = true;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffis=new BufferedInputStream(socket.getInputStream());
			img_is=socket.getInputStream();
			//os=socket.getOutputStream();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Ŭ���̾�Ʈ ������ ���� �̹��� ���� �ޱ�
	public void image_listen(){
		byte[] b=new byte[16384];
		
		try {
			fileName=buffr.readLine();
			System.out.println("���ϸ� : "+fileName);
			
			//size=Integer.parseInt(buffr.readLine());
			//System.out.println("���� ũ�� : "+size);
			
			folder=buffr.readLine();
			System.out.println("������ : "+folder);
			
			BufferedImage img=ImageIO.read(img_is);
			fos=new FileOutputStream(path+"/image/"+folder+fileName);
			ImageIO.write(img, "jpg", fos);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
			if(fos!=null){
				try {
					Calendar cal=Calendar.getInstance();
					System.out.println("���� �б� �Ϸ�"+cal.getTime());
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
	
	public void file_listen(){
		byte[] b=new byte[16384];
		
		try {
			fileName=buffr.readLine();
			System.out.println("���ϸ� : "+fileName);
			
			size=Integer.parseInt(buffr.readLine());
			System.out.println("���� ũ�� : "+size);
			
			fos=new FileOutputStream(path+fileName);
			file_is=socket.getInputStream();
			//file_is=new BufferedInputStream(file_is);
			
			//os=new BufferedOutputStream(fos);
			
			int readLength;
			while(true){
				readLength=file_is.read(b);
				if(readLength==-1){
					break;
				}
				fos.write(b, 0, readLength);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					Calendar cal=Calendar.getInstance();
					System.out.println("���� �б� �Ϸ�"+cal.getTime());
					fos.close();
					listenFlag=false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(file_is!=null){
				try {
					file_is.close();
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
			image_listen();
			//file_listen();
		}
	}
}
