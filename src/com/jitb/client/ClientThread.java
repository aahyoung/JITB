package com.jitb.client;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;

import javax.imageio.ImageIO;

import com.jitb.file.FileUtil;

public class ClientThread extends Thread{
	Socket socket;

	ClientMain clientMain;
	
	InputStream is;
	FileInputStream fis;
	BufferedWriter buffw;
	OutputStream img_os;
	OutputStream file_os;
	BufferedOutputStream buffos;
	
	boolean img_send=false;
	boolean file_send=false;
	
	String path=null;
	
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
	public void sendImage(){
		//fos=new FileOutputStream(clientMain.file);
		//fos.write(b);
		byte[] b=new byte[16384];
		String fileType=null;
		try {
			String fileName=clientMain.file.getName();
			System.out.println("보내는 파일명 : "+fileName);
			
			// 파일명 보내기
			buffw.write(fileName+"\n");
			buffw.flush();
			
			fis=new FileInputStream(clientMain.file);
			//System.out.println("보내는 경로 : "+clientMain.file);
			/*
			int size=(int) (clientMain.file.length()/b.length);
			
			if(clientMain.file.length()%b.length!=0){
				size++;
			}
			System.out.println("보내는 파일 크기 : "+size);
			buffw.write(size+"\n");
			buffw.flush();
			*/
			
			// 상위 폴더 내 하위 폴더 보내기
			buffw.write(path+"\n");
			buffw.flush();
			
			if(FileUtil.getOnlyFileName(fileName).equalsIgnoreCase(".jpg")){
				fileType="jpg";
			}
			else if(FileUtil.getOnlyFileName(fileName).equalsIgnoreCase(".png")){
				fileType="png";
			}
			System.out.println("현재 파일 확장자 : "+fileType);
			img_os=socket.getOutputStream();
			BufferedImage img=ImageIO.read(clientMain.file);
			ImageIO.write(img, fileType, img_os);
			
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
			Calendar cal=Calendar.getInstance();
			System.out.println("파일 전송 완료"+cal.getTime());
			/*
			else{
				sendFlag=true;
			}
			*/
		}
	
	}
	
	public void sendExcel(){
		//fos=new FileOutputStream(clientMain.file);
		//fos.write(b);
		byte[] b=new byte[16384];
		int readLength;
		try {
			String fileName=clientMain.file.getName();
			System.out.println("보내는 파일명 : "+fileName);
			
			buffw.write(fileName+"\n");
			buffw.flush();
			
			fis=new FileInputStream(clientMain.file);
			/*
			int size=(int) (clientMain.file.length()/b.length);
			
			if(clientMain.file.length()%b.length!=0){
				size++;
			}
			
			System.out.println("보내는 파일 크기 : "+size);
			buffw.write(size+"\n");
			buffw.flush();
			*/
			
			//is=fis;
			file_os=socket.getOutputStream();
			while(true){
				readLength=fis.read(b);
				if(readLength==-1){
					break;
				}
				file_os.write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			/*
			if(file_os!=null){
				try {
					file_os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Calendar cal=Calendar.getInstance();
				System.out.println("파일 전송 완료"+cal.getTime());
				
			}
			*/
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/*
			else{
				sendFlag=true;
			}
			*/
		}
		
	}
	
	public void run() {
		while(img_send){
			sendImage();
		}
		while(file_send){
			sendExcel();
		}
	}

}
